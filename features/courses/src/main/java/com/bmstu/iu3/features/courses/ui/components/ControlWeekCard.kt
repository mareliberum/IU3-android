package com.bmstu.iu3.features.courses.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bmstu.iu3.core.designsystem.AppBlue
import com.bmstu.iu3.core.designsystem.AppCard
import com.bmstu.iu3.core.designsystem.AppLink
import com.bmstu.iu3.core.designsystem.AppText
import com.bmstu.iu3.core.model.Plan
import com.bmstu.iu3.core.model.StudyControl
import com.bmstu.iu3.features.courses.R
import com.bmstu.iu3.features.courses.ui.format.weekRange

@Composable
internal fun ControlWeekCard(
    plan: Plan,
    week: Int,
    events: List<StudyControl>,
    current: Int?,
) {
    Card(
        Modifier.fillMaxWidth().alpha(if (current != null && week < current) .55f else 1f),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = AppCard)
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    stringResource(
                        R.string.courses_week_range,
                        week,
                        weekRange(plan = plan, week = week)
                    ),
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (week == current) AppLink else AppText,
                    modifier = Modifier.weight(1f)
                )
                if (week == current) Surface(color = AppBlue, shape = RoundedCornerShape(50)) {
                    Text(
                        stringResource(R.string.courses_now),
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }
            events.forEach { ControlEventRow(item = it) }
        }
    }
}
