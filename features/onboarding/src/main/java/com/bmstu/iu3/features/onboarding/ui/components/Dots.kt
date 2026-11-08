package com.bmstu.iu3.features.onboarding.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bmstu.iu3.core.designsystem.AppBlue
import com.bmstu.iu3.core.designsystem.AppCard

@Composable
internal fun Dots(index: Int) {
    Row(
        Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
    ) {
        repeat(4) { dot ->
            Surface(
                Modifier.size(8.dp),
                shape = CircleShape,
                color = if (dot == index) AppBlue else AppCard
            ) {}
        }
    }
}
