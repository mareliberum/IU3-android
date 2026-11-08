package com.bmstu.iu3.features.instructors.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bmstu.iu3.core.designsystem.AppBlue
import com.bmstu.iu3.core.designsystem.FacultyImage
import com.bmstu.iu3.core.model.Staff
import com.bmstu.iu3.features.instructors.ui.format.image

@Composable
internal fun HeadCard(person: Staff, onClick: () -> Unit) {
    Card(
        onClick,
        Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = AppBlue)
    ) {
        Row(
            Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            FacultyImage(name = person.image(), modifier = Modifier.width(116.dp).height(136.dp))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                Text(
                    person.position.substringBefore(','),
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = .72f)
                )
                Text(
                    person.name,
                    fontSize = 18.sp,
                    lineHeight = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
                if (',' in person.position) Text(
                    person.position.substringAfter(',').trim().replaceFirstChar(Char::titlecase),
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = .72f)
                )
            }
        }
    }
}
