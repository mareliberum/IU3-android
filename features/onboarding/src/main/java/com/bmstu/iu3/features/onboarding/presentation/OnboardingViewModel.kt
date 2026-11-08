package com.bmstu.iu3.features.onboarding.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmstu.iu3.core.data.api.CatalogRepository
import com.bmstu.iu3.core.data.api.UserRepository
import com.bmstu.iu3.core.model.Profile
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val repository: CatalogRepository,
    private val users: UserRepository,
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

    val catalog = repository.state
    fun complete(profile: Profile) {
        change { users.save(profile) }
    }
}
