package com.bmstu.iu3.features.honor.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bmstu.iu3.core.designsystem.AppGray
import com.bmstu.iu3.core.designsystem.AppText
import com.bmstu.iu3.core.model.Honor

@Composable
internal fun LegendText(person: Honor, accent: Boolean) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            person.subtitle, fontSize = 11.sp, lineHeight = 13.sp, maxLines = 2,
            color = if (accent) Color.White.copy(alpha = .75f) else AppGray
        )
        Text(
            person.name, fontSize = 16.sp, lineHeight = 20.sp, fontWeight = FontWeight.SemiBold,
            color = if (accent) Color.White else AppText, maxLines = 3
        )
    }
}
