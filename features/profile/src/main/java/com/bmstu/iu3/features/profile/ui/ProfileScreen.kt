package com.bmstu.iu3.features.profile.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bmstu.iu3.core.designsystem.AppGray
import com.bmstu.iu3.core.designsystem.AppLink
import com.bmstu.iu3.core.designsystem.FacultyImage
import com.bmstu.iu3.core.designsystem.Page
import com.bmstu.iu3.core.designsystem.contentKey
import com.bmstu.iu3.core.model.Catalog
import com.bmstu.iu3.core.model.Profile
import com.bmstu.iu3.core.model.ReminderSettings
import com.bmstu.iu3.core.model.Role
import com.bmstu.iu3.core.model.StudentGroups
import com.bmstu.iu3.core.navigation.Route
import com.bmstu.iu3.core.navigation.Screen
import com.bmstu.iu3.features.profile.R
import com.bmstu.iu3.features.profile.ui.components.ProfileField
import com.bmstu.iu3.features.profile.ui.state.ProfileEditField

@Composable
fun ProfileScreen(
    profile: Profile,
    catalog: Catalog,
    reminders: ReminderSettings,
    back: () -> Unit,
    save: (Profile) -> Unit,
    reset: () -> Unit,
    choosePhoto: () -> Unit,
    navigate: (Route) -> Unit,
) {
    var field by rememberSaveable { mutableStateOf<ProfileEditField?>(null) };
    var value by rememberSaveable { mutableStateOf("") }
    var confirmReset by rememberSaveable { mutableStateOf(false) };
    var groupPicker by rememberSaveable { mutableStateOf(false) }
    var groupQuery by rememberSaveable { mutableStateOf("") }
    val teacher = catalog.staff.firstOrNull { it.name == profile.teacher }
    val role = when (profile.role) {
        Role.Student -> stringResource(R.string.profile_student); Role.Teacher -> stringResource(R.string.profile_teacher); Role.Applicant -> stringResource(
            R.string.profile_applicant
        ); Role.Graduate -> stringResource(R.string.profile_graduate); null -> stringResource(R.string.profile_guest)
    }
    val nameLabel = stringResource(R.string.profile_name);
    val phoneLabel = stringResource(R.string.profile_phone);
    val emailLabel = stringResource(R.string.profile_email)
    val photoLabel = stringResource(R.string.profile_photo)
    Page(title = role, back = back) {
        item {
            Row(
                Modifier.fillMaxWidth().padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                FacultyImage(
                    profile.photo.ifBlank { teacher?.photo },
                    Modifier.size(60.dp).clickable(onClick = choosePhoto)
                        .semantics { contentDescription = photoLabel },
                    shape = CircleShape
                )
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Text(profile.name.ifBlank {
                        teacher?.name ?: stringResource(R.string.profile_add_name)
                    }, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                    teacher?.let { Text(it.position, color = AppGray, fontSize = 12.sp) }
                }
                IconButton({ field = ProfileEditField.Name; value = profile.name }) {
                    Icon(
                        Icons.Default.Edit,
                        nameLabel,
                        tint = AppLink
                    )
                }
            }
        }
        if (profile.role == Role.Student) item {
            ProfileField(
                label = stringResource(R.string.profile_group),
                value = profile.group.ifBlank { stringResource(R.string.profile_not_set) }) {
                groupPicker = true
            }
        }
        if (profile.role == Role.Teacher) item {
            ProfileField(
                label = stringResource(R.string.profile_department),
                value = StudentGroups.Department
            )
        }
        item {
            ProfileField(
                label = phoneLabel,
                value = profile.phone.ifBlank { stringResource(R.string.profile_not_specified) }) {
                field = ProfileEditField.Phone; value = profile.phone
            }
        }
        item {
            ProfileField(
                label = emailLabel,
                value = profile.email.ifBlank { stringResource(R.string.profile_not_specified) }) {
                field = ProfileEditField.Email; value = profile.email
            }
        }
        item {
            ProfileField(
                label = stringResource(R.string.profile_notifications),
                value = if (reminders.enabled) stringResource(R.string.profile_enabled) else stringResource(
                    R.string.profile_disabled
                )
            ) { navigate(Route(screen = Screen.Notifications)) }
        }
        item {
            TextButton({ confirmReset = true }, modifier = Modifier.fillMaxWidth()) {
                Text(
                    stringResource(R.string.profile_reset),
                    color = MaterialTheme.colorScheme.error
                )
            }
            Text(stringResource(R.string.profile_local_data), color = AppGray, fontSize = 12.sp)
        }
    }
    if (field != null) AlertDialog(
        onDismissRequest = { field = null }, title = {
            Text(
                when (field) {
                    ProfileEditField.Name -> nameLabel; ProfileEditField.Phone -> phoneLabel; else -> emailLabel
                }
            )
        },
        text = {
            OutlinedTextField(
                value,
                { value = it },
                keyboardOptions = KeyboardOptions(
                    keyboardType = when (field) {
                        ProfileEditField.Phone -> KeyboardType.Phone; ProfileEditField.Email -> KeyboardType.Email; else -> KeyboardType.Text
                    }
                )
            )
        },
        confirmButton = {
            TextButton({
                save(
                    when (field) {
                        ProfileEditField.Name -> profile.copy(name = value); ProfileEditField.Phone -> profile.copy(
                        phone = value
                    ); else -> profile.copy(
                        email = value
                    )
                    }
                ); field = null
            }) { Text(stringResource(R.string.profile_save)) }
        },
        dismissButton = {
            TextButton({
                field = null
            }) { Text(stringResource(R.string.profile_cancel)) }
        })
    if (confirmReset) AlertDialog(
        onDismissRequest = { confirmReset = false },
        title = { Text(stringResource(R.string.profile_reset_title)) },
        text = { Text(stringResource(R.string.profile_reset_description)) },
        confirmButton = {
            TextButton({
                confirmReset = false; reset()
            }) { Text(stringResource(R.string.profile_reset_confirm)) }
        },
        dismissButton = {
            TextButton({
                confirmReset = false
            }) { Text(stringResource(R.string.profile_cancel)) }
        })
    if (groupPicker) AlertDialog(
        onDismissRequest = { groupPicker = false },
        title = { Text(stringResource(R.string.profile_group)) },
        text = {
            Column {
                OutlinedTextField(
                    groupQuery,
                    { groupQuery = it },
                    label = { Text(stringResource(R.string.profile_group_hint)) })
                androidx.compose.foundation.lazy.LazyColumn(Modifier.heightIn(max = 320.dp)) {
                    items(
                        catalog.timetable.groups.keys.sorted()
                            .filter { it.contains(groupQuery, true) },
                        key = { contentKey<String>(id = it) },
                        contentType = { String::class }) { group ->
                        TextButton({
                            save(profile.copy(group = group)); groupPicker = false
                        }) { Text(group) }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton({
                groupPicker = false
            }) { Text(stringResource(R.string.profile_cancel)) }
        })
}
