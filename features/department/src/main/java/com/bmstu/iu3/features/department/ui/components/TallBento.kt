package com.bmstu.iu3.features.department.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bmstu.iu3.core.designsystem.AppSeparator
import com.bmstu.iu3.core.designsystem.FacultyImage
import com.bmstu.iu3.core.model.ImageAssets

@Composable
internal fun TallBento(
    title: String,
    image: String,
    color: Color,
    onClick: () -> Unit,
) {
    Card(
        onClick, Modifier.fillMaxWidth().height(250.dp), shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                title,
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
            if (image == ImageAssets.BENTO_TROPHY) Box(Modifier.fillMaxWidth().weight(1f)) {
                Surface(
                    Modifier.fillMaxWidth().fillMaxHeight(.75f),
                    color = AppSeparator,
                    shape = RoundedCornerShape(14.dp)
                ) {}
                FacultyImage(
                    image,
                    Modifier.fillMaxSize().padding(top = 16.dp),
                    crop = false,
                    background = Color.Transparent,
                    shape = RoundedCornerShape(0.dp)
                )
            } else FacultyImage(name = image, modifier = Modifier.fillMaxWidth().weight(1f))
        }
    }
}
