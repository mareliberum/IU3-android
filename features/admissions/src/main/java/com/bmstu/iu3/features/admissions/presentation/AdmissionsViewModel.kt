package com.bmstu.iu3.features.admissions.presentation

import androidx.lifecycle.ViewModel
import com.bmstu.iu3.core.data.api.CatalogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AdmissionsViewModel @Inject constructor(
    private val repository: CatalogRepository,
) : ViewModel() {
    val catalog = repository.state

}
