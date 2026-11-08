package com.bmstu.iu3.features.department.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmstu.iu3.core.data.api.CatalogRepository
import com.bmstu.iu3.core.data.api.CatalogState
import com.bmstu.iu3.core.data.api.UserRepository
import com.bmstu.iu3.core.model.Staff
import com.bmstu.iu3.core.model.StaffAssignments
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class DepartmentViewModel @Inject constructor(
    private val repository: CatalogRepository,
    private val users: UserRepository,
) : ViewModel() {
    val catalog = repository.state
    val profile = users.profile.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun contacts(): List<Staff> =
        (catalog.value as? CatalogState.Ready)?.let { StaffAssignments.contacts(it.catalog.staff) }
            .orEmpty()
}
