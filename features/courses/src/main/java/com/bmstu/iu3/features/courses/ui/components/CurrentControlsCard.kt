package com.bmstu.iu3.features.courses.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bmstu.iu3.core.designsystem.AppBlue
import com.bmstu.iu3.core.designsystem.AppGray
import com.bmstu.iu3.core.designsystem.AppSurface
import com.bmstu.iu3.core.model.Plan
import com.bmstu.iu3.core.model.StudyControl
import com.bmstu.iu3.features.courses.R
import com.bmstu.iu3.features.courses.ui.format.weekRange

@Composable
internal fun CurrentControlsCard(plan: Plan, week: Int) {
    val events = plan.disciplines.flatMap { discipline ->
        discipline.events.filter { it.week == week }
            .map { StudyControl(discipline = discipline, event = it) }
    }
    Surface(
        color = AppSurface,
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.5.dp, AppBlue),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                stringResource(
                    R.string.courses_current_controls,
                    week,
                    weekRange(plan = plan, week = week)
                ),
                style = MaterialTheme.typography.titleMedium
            )
            if (events.isEmpty()) Text(
                stringResource(R.string.courses_no_planned_controls),
                color = AppGray,
                style = MaterialTheme.typography.bodyMedium
            )
            else events.forEach { ControlEventRow(item = it) }
        }
    }
}
