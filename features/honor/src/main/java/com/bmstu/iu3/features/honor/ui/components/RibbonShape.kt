package com.bmstu.iu3.features.honor.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.GenericShape

internal val RibbonShape = GenericShape { size, _ ->
    moveTo(0f, 0f); lineTo(size.width, 0f); lineTo(size.width * .89f, size.height / 2)
    lineTo(size.width, size.height); lineTo(0f, size.height); close()
}
