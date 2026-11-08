package com.bmstu.iu3.core.data.source

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.util.AtomicFile
import com.bmstu.iu3.core.common.di.IoDispatcher
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.time.Clock
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class ProfilePhotoDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val clock: Clock,
) {
    suspend fun import(source: String): String = withContext(dispatcher) {
        val bitmap = ImageDecoder.decodeBitmap(
            ImageDecoder.createSource(
                context.contentResolver,
                Uri.parse(source)
            )
        ) { decoder, info, _ ->
            val scale =
                (MAX_SIZE.toFloat() / maxOf(info.size.width, info.size.height)).coerceAtMost(1f)
            decoder.setTargetSize(
                (info.size.width * scale).toInt().coerceAtLeast(1),
                (info.size.height * scale).toInt().coerceAtLeast(1)
            )
            decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
        }
        try {
            val target = File(context.filesDir, FILE_NAME)
            val atomic = AtomicFile(target)
            val stream = atomic.startWrite()
            try {
                check(bitmap.compress(Bitmap.CompressFormat.JPEG, JPEG_QUALITY, stream))
                atomic.finishWrite(stream)
            } catch (error: Exception) {
                atomic.failWrite(stream)
                throw error
            }
            target.toURI().toString() + VERSION_QUERY + clock.millis()
        } finally {
            bitmap.recycle()
        }
    }

    suspend fun delete() =
        withContext(dispatcher) { AtomicFile(File(context.filesDir, FILE_NAME)).delete() }

    private companion object {
        const val FILE_NAME = "profile-photo.jpg"
        const val VERSION_QUERY = "?version="
        const val MAX_SIZE = 512
        const val JPEG_QUALITY = 85
    }
}
