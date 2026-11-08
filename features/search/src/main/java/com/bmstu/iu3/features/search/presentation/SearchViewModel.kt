package com.bmstu.iu3.features.search.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmstu.iu3.core.common.di.DefaultDispatcher
import com.bmstu.iu3.core.data.api.CatalogRepository
import com.bmstu.iu3.core.data.api.CatalogState
import com.bmstu.iu3.core.data.api.UserRepository
import com.bmstu.iu3.core.model.AcademicTime
import com.bmstu.iu3.core.model.Role
import com.bmstu.iu3.core.navigation.Screen
import com.bmstu.iu3.features.search.data.SearchLexicon
import com.bmstu.iu3.features.search.domain.SearchEngine
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Clock
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.any
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

@HiltViewModel
class SearchViewModel @Inject internal constructor(
    catalog: CatalogRepository,
    private val users: UserRepository,
    private val savedState: SavedStateHandle,
    private val lexicon: SearchLexicon,
    private val clock: Clock,
    @DefaultDispatcher private val computationDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val writeError = MutableStateFlow(false)
    val hasWriteError = writeError.asStateFlow()
    fun clearWriteError() {
        writeError.value = false
    }

    private fun change(action: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                action()
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (_: Exception) {
                writeError.value = true
            }
        }
    }

    private val query = savedState.getStateFlow(QUERY_KEY, "")
    val uiState = combine(
        catalog.state,
        users.profile,
        users.recent,
        query
    ) { state, profile, recent, query ->
        val ready = state as? CatalogState.Ready
        val sections =
            lexicon.sections.filter { it.screen != Screen.Admissions || profile.role == Role.Applicant }
        val date = LocalDate.now(clock.withZone(AcademicTime.Zone))
        val popular =
            if (ready == null) emptyList() else listOf(lexicon.popularTeacher) + lexicon.popularPool
                .filter { profile.role == Role.Applicant || it !in lexicon.applicantQueries }
                .filter { value ->
                    sections.any { section ->
                        (listOf(section.title) + section.keywords).any {
                            it.contains(
                                value,
                                true
                            )
                        }
                    } ||
                        SearchEngine.search(
                            ready.catalog,
                            profile,
                            value,
                            today = date,
                            includeTransient = false
                        ).isNotEmpty()
                }.shuffled(kotlin.random.Random(date.toEpochDay())).take(POPULAR_LIMIT)
        val normalized = query.trim().lowercase().replace('ё', 'е')
        SearchUiState(
            query = query,
            recent = recent,
            popular = popular,
            results = ready?.let { SearchEngine.search(it.catalog, profile, query, today = date) }
                .orEmpty(),
            sections = sections.filter { section ->
                normalized.length >= MIN_SECTION_QUERY && (listOf(
                    section.title
                ) + section.keywords).any { it.lowercase().replace('ё', 'е').contains(normalized) }
            }.map { it.screen to it.title },
        )
    }.flowOn(computationDispatcher)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), SearchUiState())

    fun query(value: String) {
        savedState[QUERY_KEY] = value
    }

    fun remember(value: String) {
        change { users.remember(value) }
    }

    fun forget(value: String) {
        change { users.forget(value) }
    }

    private companion object {
        const val QUERY_KEY = "query"
        const val POPULAR_LIMIT = 5
        const val MIN_SECTION_QUERY = 2
    }
}
