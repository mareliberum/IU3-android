package com.bmstu.iu3.features.honor.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bmstu.iu3.core.designsystem.AppCard
import com.bmstu.iu3.core.designsystem.AppGray
import com.bmstu.iu3.core.designsystem.AppSeparator
import com.bmstu.iu3.core.designsystem.AppText
import com.bmstu.iu3.core.designsystem.FacultyImage
import com.bmstu.iu3.core.model.Honor
import com.bmstu.iu3.features.honor.R
import kotlin.math.roundToInt

@Composable
internal fun GraduateCard(
    person: Honor,
    modifier: Modifier,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(.7.dp, AppSeparator),
        colors = CardDefaults.cardColors(containerColor = AppCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Box(Modifier.padding(13.dp).fillMaxWidth().aspectRatio(137f / 169f)) {
            Layout(
                content = {
                    FacultyImage(
                        person.photoAsset,
                        Modifier.fillMaxSize(),
                        crop = false,
                        shape = RoundedCornerShape(0.dp),
                        background = Color.Transparent
                    )
                },
                modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(14.dp))
            ) { measurables, constraints ->
                val scale = constraints.maxWidth / 137f
                val photo = (person.photoSize * scale).roundToInt()
                val placeable = measurables.single().measure(Constraints.fixed(photo, photo))
                layout(constraints.maxWidth, constraints.maxHeight) {
                    placeable.place(
                        (person.photoOffset.width * scale).roundToInt(),
                        (person.photoOffset.height * scale).roundToInt()
                    )
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    person.subtitle,
                    color = AppGray,
                    fontSize = 11.sp,
                    lineHeight = 13.sp,
                    maxLines = 1
                )
                Text(
                    person.name.split(' ').take(2).joinToString(" "),
                    modifier = Modifier.fillMaxWidth(80f / 137f),
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = AppText,
                    maxLines = 3
                )
            }
            HonorRibbon(
                text = stringResource(R.string.honor_pride_ribbon), gold = false,
                modifier = Modifier.align(Alignment.BottomStart).fillMaxWidth(87f / 137f)
                    .aspectRatio(87f / 24f)
                    .clip(RoundedCornerShape(bottomStart = 14.dp))
            )
        }
    }
}
