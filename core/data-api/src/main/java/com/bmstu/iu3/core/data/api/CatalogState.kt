package com.bmstu.iu3.core.data.api

import com.bmstu.iu3.core.model.Catalog

sealed interface CatalogState {
    data object Loading : CatalogState
    data class Ready(val catalog: Catalog, val offline: Boolean = false) : CatalogState
    data object Failed : CatalogState
}
