package com.bmstu.iu3.features.department.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bmstu.iu3.core.designsystem.AppGray
import com.bmstu.iu3.core.designsystem.FacultyImage
import com.bmstu.iu3.core.designsystem.ItemCard
import com.bmstu.iu3.core.designsystem.Page
import com.bmstu.iu3.core.designsystem.Section
import com.bmstu.iu3.core.designsystem.contentKey
import com.bmstu.iu3.core.model.Catalog
import com.bmstu.iu3.core.model.DepartmentLinks
import com.bmstu.iu3.core.model.ImageAssets
import com.bmstu.iu3.core.model.LinkScheme
import com.bmstu.iu3.core.model.Profile
import com.bmstu.iu3.core.model.Role
import com.bmstu.iu3.core.model.Staff
import com.bmstu.iu3.core.model.StaffAssignments
import com.bmstu.iu3.core.navigation.Route
import com.bmstu.iu3.core.navigation.Screen
import com.bmstu.iu3.core.navigation.external.ExternalUris
import com.bmstu.iu3.features.department.R

@Composable
fun ContactsScreen(
    catalog: Catalog,
    keyStaff: List<Staff>,
    profile: Profile,
    back: () -> Unit,
    open: (String) -> Unit,
    navigate: (Route) -> Unit,
) {
    Page(title = stringResource(R.string.department_contacts), back = back) {
        item {
            FacultyImage(
                name = ImageAssets.CONTACTS_BUILDING,
                modifier = Modifier.fillMaxWidth().height(200.dp)
            )
        }
        item {
            ItemCard(
                title = stringResource(R.string.department_faculty),
                subtitle = catalog.contacts.address + "\n" + catalog.contacts.building
            )
        }
        item {
            ItemCard(
                catalog.contacts.email,
                stringResource(R.string.department_email),
                onClick = { open(ExternalUris.email(catalog.contacts.email)) })
        }
        item {
            ItemCard(
                catalog.contacts.phone,
                stringResource(R.string.department_phone),
                onClick = { open(ExternalUris.phone(catalog.contacts.phone)) })
        }
        item {
            ItemCard(
                catalog.contacts.website.removePrefix(LinkScheme.Https.prefix + "//"),
                stringResource(R.string.department_website),
                onClick = { open(catalog.contacts.website) })
        }
        item {
            ItemCard(
                title = catalog.contacts.officeHours,
                subtitle = stringResource(R.string.department_hours_label)
            )
        }
        item { Text(stringResource(R.string.department_weekend_hours), color = AppGray) }
        item {
            ItemCard(
                stringResource(R.string.department_vk_title),
                stringResource(R.string.department_vk_group),
                onClick = { open(DepartmentLinks.VK) })
        }
        item {
            ItemCard(
                stringResource(R.string.department_admission_office),
                onClick = { open(DepartmentLinks.ADMISSIONS) })
        }
        if (profile.role == Role.Applicant) item {
            ItemCard(
                stringResource(R.string.department_programs),
                onClick = { navigate(Route(screen = Screen.Admissions)) })
        }
        item { Section(title = stringResource(R.string.department_key_staff)) }
        items(
            keyStaff,
            key = { contentKey<Staff>(id = it.name) },
            contentType = { Staff::class }) { person ->
            ItemCard(
                person.name,
                if (StaffAssignments.isCurator(person)) stringResource(R.string.department_curator) else person.position,
                person.photo,
                onClick = { navigate(Route(screen = Screen.Instructor, id = person.name)) })
            val email =
                StaffAssignments.contactEmail(person, catalog.contacts)
            if (!email.isNullOrBlank()) TextButton({ open(ExternalUris.email(email)) }) { Text(email) }
        }
        item {
            Button(onClick = {
                open(
                    ExternalUris.map(
                        catalog.contacts.longitude,
                        catalog.contacts.latitude
                    )
                )
            }) {
                Text(
                    stringResource(R.string.department_map)
                )
            }
        }
    }
}
