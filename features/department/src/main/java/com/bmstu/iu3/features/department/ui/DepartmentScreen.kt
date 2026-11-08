package com.bmstu.iu3.features.department.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bmstu.iu3.core.designsystem.AppBlack
import com.bmstu.iu3.core.designsystem.AppBlue
import com.bmstu.iu3.core.designsystem.AppCard
import com.bmstu.iu3.core.designsystem.AppLightCard
import com.bmstu.iu3.core.designsystem.MainPage
import com.bmstu.iu3.core.model.ImageAssets
import com.bmstu.iu3.core.model.Profile
import com.bmstu.iu3.core.model.Role
import com.bmstu.iu3.core.navigation.Route
import com.bmstu.iu3.core.navigation.Screen
import com.bmstu.iu3.features.department.R
import com.bmstu.iu3.features.department.ui.components.SmallBento
import com.bmstu.iu3.features.department.ui.components.TallBento

@Composable
fun DepartmentScreen(profile: Profile, navigate: (Route) -> Unit) {
    MainPage(
        stringResource(R.string.department_department),
        profile = { navigate(Route(screen = Screen.Profile)) },
        branded = profile.role in listOf(Role.Student, Role.Teacher, null)
    ) {
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    TallBento(
                        title = stringResource(R.string.department_teachers),
                        image = ImageAssets.BENTO_TEACHERS,
                        color = AppBlue
                    ) { navigate(Route(screen = Screen.Instructors)) }
                    SmallBento(
                        title = stringResource(R.string.department_projects),
                        image = ImageAssets.BENTO_PROJECTS,
                        color = AppLightCard
                    ) { navigate(Route(screen = Screen.Projects)) }
                }
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    SmallBento(
                        title = stringResource(R.string.department_stacks),
                        image = ImageAssets.BENTO_COURSES,
                        color = AppCard
                    ) { navigate(Route(screen = Screen.Stacks)) }
                    TallBento(
                        title = stringResource(R.string.department_honor),
                        image = ImageAssets.BENTO_TROPHY,
                        color = AppBlack
                    ) { navigate(Route(screen = Screen.Honor)) }
                }
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                SmallBento(
                    title = stringResource(R.string.department_contacts),
                    image = ImageAssets.BENTO_CONTACTS,
                    color = AppBlue,
                    modifier = Modifier.weight(.35f)
                ) { navigate(Route(screen = Screen.Contacts)) }
                SmallBento(
                    title = stringResource(R.string.department_achievements),
                    image = ImageAssets.BENTO_ACHIEVEMENTS,
                    color = AppLightCard,
                    modifier = Modifier.weight(.65f)
                ) { navigate(Route(screen = Screen.Achievements)) }
            }
        }
        item {
            if (profile.role == Role.Applicant) Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                SmallBento(
                    title = stringResource(R.string.department_admissions),
                    image = ImageAssets.BENTO_ADMISSIONS,
                    color = AppCard,
                    modifier = Modifier.weight(1f)
                ) { navigate(Route(screen = Screen.Admissions)) }
                SmallBento(
                    title = stringResource(R.string.department_plan),
                    image = ImageAssets.BENTO_STUDY_PLAN,
                    color = AppBlue,
                    modifier = Modifier.weight(1f)
                ) { navigate(Route(screen = Screen.StudyPlan)) }
            } else SmallBento(
                title = stringResource(R.string.department_plan),
                image = ImageAssets.BENTO_STUDY_PLAN,
                color = AppBlue
            ) { navigate(Route(screen = Screen.StudyPlan)) }
        }
    }
}
