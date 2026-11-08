package com.bmstu.iu3.features.honor.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bmstu.iu3.core.designsystem.AppBlue
import com.bmstu.iu3.core.designsystem.AppCard
import com.bmstu.iu3.core.model.Honor

@Composable
internal fun LegendCard(
    person: Honor,
    modifier: Modifier,
    onClick: () -> Unit,
) {
    val accent = person.accent
    Card(
        onClick = onClick,
        modifier = modifier.heightIn(min = 311.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = if (accent) AppBlue else AppCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            if (accent) LegendText(person = person, accent = true)
            LegendPortrait(person = person)
            if (!accent) LegendText(person = person, accent = false)
        }
    }
}
