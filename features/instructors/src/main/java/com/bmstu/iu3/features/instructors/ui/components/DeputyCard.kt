package com.bmstu.iu3.features.instructors.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bmstu.iu3.core.designsystem.AppCard
import com.bmstu.iu3.core.designsystem.AppGray
import com.bmstu.iu3.core.designsystem.FacultyImage
import com.bmstu.iu3.core.model.Staff
import com.bmstu.iu3.core.model.StaffAssignments
import com.bmstu.iu3.features.instructors.R
import com.bmstu.iu3.features.instructors.ui.format.image

@Composable
internal fun DeputyCard(
    person: Staff,
    modifier: Modifier,
    onClick: () -> Unit,
) {
    Card(
        onClick,
        modifier.height(256.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = AppCard)
    ) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
            FacultyImage(name = person.image(), modifier = Modifier.fillMaxWidth().height(128.dp))
            Text(
                if (StaffAssignments.isEducationDeputy(person)) stringResource(R.string.instructors_education_deputy) else stringResource(
                    R.string.instructors_research_deputy
                ),
                color = AppGray, fontSize = 11.sp, lineHeight = 13.sp, minLines = 2
            )
            Text(
                person.name,
                fontSize = 17.sp,
                lineHeight = 21.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
