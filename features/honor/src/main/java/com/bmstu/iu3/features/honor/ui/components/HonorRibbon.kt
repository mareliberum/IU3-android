package com.bmstu.iu3.features.honor.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bmstu.iu3.core.designsystem.AppBlack
import com.bmstu.iu3.core.designsystem.AppHonorGoldGradient
import com.bmstu.iu3.core.designsystem.AppHonorSteelGradient

@Composable
internal fun HonorRibbon(
    text: String,
    gold: Boolean,
    modifier: Modifier,
) {
    Box(
        modifier.clip(RibbonShape).background(
            if (gold) Brush.verticalGradient(AppHonorGoldGradient)
            else Brush.horizontalGradient(AppHonorSteelGradient)
        ), contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text,
            color = AppBlack,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            lineHeight = 15.sp,
            maxLines = 1,
            modifier = Modifier.padding(start = 10.dp)
        )
    }
}
