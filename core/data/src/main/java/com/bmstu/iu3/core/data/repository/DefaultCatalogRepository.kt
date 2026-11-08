package com.bmstu.iu3.core.data.repository

import android.util.Log
import com.bmstu.iu3.core.data.api.CatalogRepository
import com.bmstu.iu3.core.data.api.CatalogState
import com.bmstu.iu3.core.data.source.CachedCatalog
import com.bmstu.iu3.core.data.source.CatalogCacheDataSource
import com.bmstu.iu3.core.data.source.CatalogDataSource
import com.bmstu.iu3.core.data.withDisplayDefaults
import java.time.Clock
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@Singleton
internal class DefaultCatalogRepository @Inject constructor(
    private val source: CatalogDataSource,
    private val cache: CatalogCacheDataSource,
    private val clock: Clock,
) : CatalogRepository {
    private val mutex = Mutex()
    private var savedAt = 0L
    private val mutableState = MutableStateFlow<CatalogState>(CatalogState.Loading)
    override val state = mutableState.asStateFlow()
    override suspend fun refresh(force: Boolean, maxAgeMillis: Long) = mutex.withLock {
        var previous = (state.value as? CatalogState.Ready)?.catalog
        if (previous == null) {
            val cached = cache.read()
            previous = cached?.catalog
            savedAt = cached?.savedAt ?: 0L
        }
        if (previous != null) {
            mutableState.value = CatalogState.Ready(previous)
            if (!force && clock.millis() - savedAt in 0..maxAgeMillis) return@withLock
        } else mutableState.value = CatalogState.Loading
        try {
            val catalog = source.load().withDisplayDefaults()
            savedAt = clock.millis()
            mutableState.value = CatalogState.Ready(catalog)
            cache.write(CachedCatalog(savedAt = savedAt, catalog = catalog))
        } catch (cancelled: CancellationException) {
            throw cancelled
        } catch (error: Exception) {
            Log.e(TAG, LOAD_FAILURE, error)
            mutableState.value =
                previous?.let { CatalogState.Ready(it, offline = true) } ?: CatalogState.Failed
        }
    }

    private companion object {
        const val TAG = "CatalogRepository"
        const val LOAD_FAILURE = "Cannot load department catalog"
    }
}
