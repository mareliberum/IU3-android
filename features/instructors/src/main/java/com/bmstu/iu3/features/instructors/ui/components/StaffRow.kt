package com.bmstu.iu3.features.instructors.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bmstu.iu3.core.designsystem.AppCard
import com.bmstu.iu3.core.designsystem.AppGray
import com.bmstu.iu3.core.designsystem.AppLightCard
import com.bmstu.iu3.core.designsystem.FacultyImage
import com.bmstu.iu3.core.model.Staff
import com.bmstu.iu3.features.instructors.ui.format.image
import com.bmstu.iu3.features.instructors.ui.format.shortName

@Composable
internal fun StaffRow(person: Staff, onClick: () -> Unit) {
    Card(
        onClick,
        colors = CardDefaults.cardColors(containerColor = AppLightCard),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(.5.dp, AppCard)
    ) {
        Row(
            Modifier.fillMaxWidth().padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            FacultyImage(person.image(), Modifier.size(40.dp), shape = RoundedCornerShape(10.dp))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(person.shortName(), fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                Text(person.position, fontSize = 11.sp, lineHeight = 14.sp, color = AppGray)
            }
        }
    }
}
