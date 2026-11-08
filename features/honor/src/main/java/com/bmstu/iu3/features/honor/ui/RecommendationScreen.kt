package com.bmstu.iu3.features.honor.ui

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bmstu.iu3.core.designsystem.FacultyImage
import com.bmstu.iu3.core.designsystem.Page
import com.bmstu.iu3.features.honor.R

@Composable
fun RecommendationScreen(
    back: () -> Unit, send: (
        String,
        String,
        String?,
    ) -> Unit
) {
    var name by rememberSaveable { mutableStateOf("") };
    var year by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") };
    var email by rememberSaveable { mutableStateOf("") }
    var achievements by rememberSaveable { mutableStateOf("") }
    var photoUri by rememberSaveable { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val picker =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) runCatching {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }
            photoUri = uri?.toString()
        }
    val subject = stringResource(R.string.honor_subject, name.trim())
    val unspecified = stringResource(R.string.honor_unspecified)
    val body = stringResource(
        R.string.honor_body,
        name.trim(),
        year.ifBlank { unspecified },
        phone.ifBlank { unspecified },
        email.ifBlank { unspecified },
        achievements.ifBlank { unspecified })
    Page(title = stringResource(R.string.honor_recommendation), back = back) {
        item {
            if (photoUri != null) FacultyImage(name = photoUri, modifier = Modifier.size(120.dp))
            TextButton({ picker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }) {
                Text(
                    stringResource(R.string.honor_choose_photo)
                )
            }
        }
        item {
            OutlinedTextField(
                name,
                { name = it },
                label = { Text(stringResource(R.string.honor_name)) },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                year,
                { year = it.filter(Char::isDigit).take(4) },
                label = { Text(stringResource(R.string.honor_year)) },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                phone,
                { phone = it },
                label = { Text(stringResource(R.string.honor_phone)) },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                email,
                { email = it },
                label = { Text(stringResource(R.string.honor_email)) },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                achievements,
                { achievements = it },
                label = { Text(stringResource(R.string.honor_achievements)) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4
            )
        }
        item {
            Button(
                enabled = name.isNotBlank(),
                onClick = { send(subject, body, photoUri) },
                modifier = Modifier.fillMaxWidth()
            ) { Text(stringResource(R.string.honor_send)) }
        }
    }
}
