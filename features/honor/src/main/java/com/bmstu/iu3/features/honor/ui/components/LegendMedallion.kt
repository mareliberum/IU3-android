package com.bmstu.iu3.features.honor.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bmstu.iu3.core.designsystem.AppHonorBronze
import com.bmstu.iu3.core.designsystem.AppHonorMedallionGradient
import com.bmstu.iu3.core.designsystem.AppLegendGold
import com.bmstu.iu3.features.honor.R

@Composable
internal fun LegendMedallion(name: String) {
    Box(
        Modifier.fillMaxSize().background(Brush.linearGradient(AppHonorMedallionGradient)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Surface(
                shape = androidx.compose.foundation.shape.CircleShape,
                color = AppLegendGold,
                border = BorderStroke(2.dp, AppHonorBronze),
                modifier = Modifier.fillMaxWidth(.78f).aspectRatio(1f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        name.substringBefore(' '), fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = AppHonorBronze
                    )
                }
            }
            Text(
                stringResource(R.string.honor_medallion),
                fontFamily = FontFamily.Serif,
                fontSize = 8.sp,
                color = AppHonorBronze
            )
        }
    }
}
