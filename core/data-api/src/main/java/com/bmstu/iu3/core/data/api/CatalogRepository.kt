package com.bmstu.iu3.core.data.api

import kotlinx.coroutines.flow.StateFlow

interface CatalogRepository {
    val state: StateFlow<CatalogState>
    suspend fun refresh(force: Boolean = false, maxAgeMillis: Long = DEFAULT_MAX_AGE_MILLIS)

    companion object {
        const val DEFAULT_MAX_AGE_MILLIS = 120_000L
        const val FOREGROUND_MAX_AGE_MILLIS = 600_000L
    }
}
