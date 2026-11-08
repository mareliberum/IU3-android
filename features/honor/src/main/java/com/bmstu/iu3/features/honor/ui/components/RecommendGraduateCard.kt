package com.bmstu.iu3.features.honor.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bmstu.iu3.core.designsystem.AppCard
import com.bmstu.iu3.core.designsystem.AppGray
import com.bmstu.iu3.core.designsystem.AppSeparator
import com.bmstu.iu3.core.designsystem.FacultyImage
import com.bmstu.iu3.core.model.ImageAssets
import com.bmstu.iu3.features.honor.R

@Composable
internal fun RecommendGraduateCard(modifier: Modifier, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(.7.dp, AppSeparator),
        colors = CardDefaults.cardColors(containerColor = AppCard)
    ) {
        Column(
            Modifier.padding(13.dp).fillMaxWidth().aspectRatio(137f / 169f)
                .clip(RoundedCornerShape(14.dp)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FacultyImage(
                ImageAssets.RECOMMEND_PLUS,
                Modifier.fillMaxWidth().height(120.dp),
                crop = false,
                background = Color.Transparent
            )
            Text(
                stringResource(R.string.honor_recommend),
                color = AppGray,
                fontSize = 11.sp,
                lineHeight = 14.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
            )
        }
    }
}
