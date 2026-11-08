package com.bmstu.iu3.core.data.source

import android.content.Context
import android.util.Log
import com.bmstu.iu3.core.common.di.IoDispatcher
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

@Singleton
internal class CatalogCacheDataSource @Inject constructor(
    @ApplicationContext context: Context,
    private val json: Json,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) {
    private val cache = File(context.cacheDir, CACHE_FILE)
    suspend fun read(): CachedCatalog? = withContext(dispatcher) {
        try {
            json.decodeFromString<CachedCatalog>(cache.readText())
        } catch (cancelled: CancellationException) {
            throw cancelled
        } catch (_: Exception) {
            null
        }
    }

    suspend fun write(value: CachedCatalog) = withContext(dispatcher) {
        try {
            val pending = File(cache.parentFile, PENDING_FILE)
            pending.writeText(json.encodeToString(CachedCatalog.serializer(), value))
            check(pending.renameTo(cache)) { CACHE_REPLACE_FAILURE }
        } catch (cancelled: CancellationException) {
            throw cancelled
        } catch (error: Exception) {
            Log.w(TAG, CACHE_WRITE_FAILURE, error)
        }
    }

    private companion object {
        const val CACHE_FILE = "catalog-v2.json"
        const val PENDING_FILE = "catalog.pending"
        const val TAG = "CatalogCache"
        const val CACHE_REPLACE_FAILURE = "Cannot replace catalog cache"
        const val CACHE_WRITE_FAILURE = "Cannot save catalog cache"
    }
}
