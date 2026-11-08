package com.bmstu.iu3.features.alumni.ui

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.bmstu.iu3.core.designsystem.ItemCard
import com.bmstu.iu3.core.designsystem.Page
import com.bmstu.iu3.core.designsystem.Section
import com.bmstu.iu3.core.model.AcademicTime
import com.bmstu.iu3.core.model.DepartmentLinks
import com.bmstu.iu3.core.model.StudyCalendar
import com.bmstu.iu3.core.navigation.Route
import com.bmstu.iu3.core.navigation.Screen
import com.bmstu.iu3.features.alumni.R
import java.time.LocalDate

@Composable
fun AlumniScreen(
    navigate: (Route) -> Unit,
    open: (String) -> Unit,
    mail: (String, String) -> Unit,
) {
    val today = LocalDate.now(AcademicTime.Zone)
    val year = if (today > StudyCalendar.alumniMeeting(today.year)) today.year + 1 else today.year
    val internshipSubject = stringResource(R.string.alumni_internship_subject)
    val internshipBody = stringResource(R.string.alumni_internship_body)
    val lectureSubject = stringResource(R.string.alumni_lecture_subject)
    val lectureBody = stringResource(R.string.alumni_lecture_body)
    val diplomaSubject = stringResource(R.string.alumni_diploma_subject)
    val contactSubject = stringResource(R.string.alumni_contact_subject)
    Page(
        stringResource(R.string.alumni_alumni),
        profile = { navigate(Route(screen = Screen.Profile)) }) {
        item {
            ItemCard(
                stringResource(R.string.alumni_meeting),
                stringResource(R.string.alumni_meeting_dates, year),
                onClick = { open(DepartmentLinks.ALUMNI_MEETING) })
        }
        item {
            ItemCard(
                stringResource(R.string.alumni_chat),
                onClick = { open(DepartmentLinks.ALUMNI_CHAT) })
        }
        item {
            Section(title = stringResource(R.string.alumni_mentoring)); TextButton({
            open(
                DepartmentLinks.MENTOR_APPLICATION
            )
        }) {
            Text(
                stringResource(R.string.alumni_become)
            )
        }; TextButton({ open(DepartmentLinks.MENTOR_REQUEST) }) {
            Text(
                stringResource(R.string.alumni_find)
            )
        }
        }
        item {
            Section(title = stringResource(R.string.alumni_students_help))
            TextButton({
                mail(
                    internshipSubject,
                    internshipBody
                )
            }) { Text(stringResource(R.string.alumni_internship)) }
            TextButton({ mail(lectureSubject, lectureBody) }) { Text(lectureSubject) }
            TextButton({ mail(diplomaSubject, lectureBody) }) { Text(diplomaSubject) }
        }
        item {
            ItemCard(
                stringResource(R.string.alumni_department_help),
                onClick = { mail(contactSubject, "") })
        }
        item {
            ItemCard(
                stringResource(R.string.alumni_recommendation),
                stringResource(R.string.alumni_tell),
                onClick = { navigate(Route(screen = Screen.Recommendation)) })
        }
        item {
            Section(title = stringResource(R.string.alumni_brotherhood))
            TextButton({ open(DepartmentLinks.ALUMNI_CENTER) }) { Text(stringResource(R.string.alumni_center)) }
            TextButton({ open(DepartmentLinks.ALUMNI_CHANNEL) }) { Text(stringResource(R.string.alumni_telegram)) }
            TextButton({ open(DepartmentLinks.ALUMNI_VK) }) { Text(stringResource(R.string.alumni_vk)) }
        }
    }
}
