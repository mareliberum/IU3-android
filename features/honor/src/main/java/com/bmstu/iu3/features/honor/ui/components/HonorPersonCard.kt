package com.bmstu.iu3.features.honor.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bmstu.iu3.core.model.Honor
import com.bmstu.iu3.features.honor.ui.format.isLegend

@Composable
internal fun HonorPersonCard(
    person: Honor,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    if (person.isLegend()) LegendCard(
        person = person,
        modifier = modifier,
        onClick = onClick
    ) else GraduateCard(
        person = person,
        modifier = modifier,
        onClick = onClick
    )
}
