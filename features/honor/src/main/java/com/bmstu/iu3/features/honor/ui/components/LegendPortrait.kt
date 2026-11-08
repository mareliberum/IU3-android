package com.bmstu.iu3.features.honor.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.bmstu.iu3.core.designsystem.AppLegendGold
import com.bmstu.iu3.core.designsystem.FacultyImage
import com.bmstu.iu3.core.model.Honor
import com.bmstu.iu3.features.honor.R
import kotlin.math.roundToInt

@Composable
internal fun LegendPortrait(person: Honor) {
    Box(
        Modifier.fillMaxWidth().aspectRatio(137f / 184f).clip(RoundedCornerShape(12.dp))
    ) {
        if (person.photoAsset.isBlank()) LegendMedallion(name = person.name)
        else FacultyImage(
            person.photoAsset,
            Modifier.fillMaxSize(),
            crop = false,
            shape = RoundedCornerShape(12.dp),
            background = AppLegendGold
        )
        HonorRibbon(
            text = stringResource(R.string.honor_legend_ribbon), gold = true,
            modifier = Modifier.align(Alignment.BottomStart).fillMaxWidth(.74f)
                .layout { measurable, constraints ->
                    val height = (constraints.maxHeight * .135f).roundToInt()
                    val padding = (constraints.maxHeight * .025f).roundToInt()
                    val ribbon = measurable.measure(Constraints.fixed(constraints.maxWidth, height))
                    layout(ribbon.width, ribbon.height + padding) { ribbon.placeRelative(0, 0) }
                }
        )
    }
}
