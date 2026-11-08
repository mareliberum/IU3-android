package com.bmstu.iu3.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmstu.iu3.core.data.api.CatalogRepository
import com.bmstu.iu3.core.data.api.UserRepository
import com.bmstu.iu3.core.notifications.SynchronizeReminders
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class AppViewModel @Inject constructor(
    private val repository: CatalogRepository,
    users: UserRepository,
    private val synchronize: SynchronizeReminders,
) : ViewModel() {
    val catalog = repository.state
    val profile = users.profile.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch { repository.refresh(force = true) }
    }

    fun foreground() {
        viewModelScope.launch {
            repository.refresh(maxAgeMillis = CatalogRepository.FOREGROUND_MAX_AGE_MILLIS)
            synchronize()
        }
    }
}
