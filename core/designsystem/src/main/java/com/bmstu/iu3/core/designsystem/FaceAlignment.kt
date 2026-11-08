package com.bmstu.iu3.core.designsystem

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Alignment

internal class FaceAlignment(private val x: Float, private val y: Float) : Alignment {
    override fun align(
        size: androidx.compose.ui.unit.IntSize,
        space: androidx.compose.ui.unit.IntSize,
        layoutDirection: androidx.compose.ui.unit.LayoutDirection,
    ): androidx.compose.ui.unit.IntOffset {
        fun offset(
            viewport: Int,
            image: Int,
            focal: Float,
        ): Int =
            (viewport / 2f - image * focal).toInt()
                .coerceIn(minOf(0, viewport - image), maxOf(0, viewport - image))
        return androidx.compose.ui.unit.IntOffset(
            offset(space.width, size.width, x),
            offset(space.height, size.height, y)
        )
    }
}
