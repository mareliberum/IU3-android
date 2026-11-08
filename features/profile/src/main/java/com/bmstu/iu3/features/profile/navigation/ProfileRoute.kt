package com.bmstu.iu3.features.profile.navigation

import android.Manifest
import android.app.NotificationManager
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bmstu.iu3.core.designsystem.DataWriteErrorDialog
import com.bmstu.iu3.core.designsystem.LoadingContent
import com.bmstu.iu3.core.model.LinkScheme
import com.bmstu.iu3.core.navigation.Route
import com.bmstu.iu3.core.navigation.Screen
import com.bmstu.iu3.features.profile.presentation.ProfileViewModel
import com.bmstu.iu3.features.profile.ui.NotificationScreen
import com.bmstu.iu3.features.profile.ui.ProfileScreen

@Composable
fun ProfileRoute(
    route: Route,
    back: () -> Unit,
    navigate: (Route) -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val writeError by viewModel.hasWriteError.collectAsStateWithLifecycle()
    if (writeError) DataWriteErrorDialog(onDismiss = viewModel::clearWriteError)
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val ready = state
    if (ready == null) {
        LoadingContent(); return
    }
    val profile = ready.profile
    val reminders = ready.reminders
    val context = LocalContext.current
    val photo =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let {
                viewModel.photo(it.toString())
            }
        }
    var granted by remember {
        mutableStateOf(
            context.getSystemService(NotificationManager::class.java).areNotificationsEnabled()
        )
    }
    var exactAlarms by remember { mutableStateOf(viewModel.canScheduleExact()) }
    val permission =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { allowed ->
            granted = allowed
            if (allowed) viewModel.save(reminders.copy(enabled = true))
        }
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        granted =
            context.getSystemService(NotificationManager::class.java).areNotificationsEnabled()
        exactAlarms = viewModel.canScheduleExact()
        viewModel.reschedule()
    }
    val openSettings: () -> Unit = {
        context.startActivity(
            Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).putExtra(
                Settings.EXTRA_APP_PACKAGE,
                context.packageName
            )
        )
    }
    when (route.screen) {
        Screen.Profile -> ProfileScreen(
            profile = profile,
            catalog = ready.catalog,
            reminders = reminders,
            back = back,
            save = viewModel::save,
            reset = viewModel::reset,
            choosePhoto = { photo.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
            navigate = navigate
        )

        Screen.Notifications -> NotificationScreen(
            settings = reminders,
            role = profile.role,
            granted = granted,
            back = back,
            save = viewModel::save,
            planned = ready.planned,
            requestPermission = {
                if (Build.VERSION.SDK_INT >= 33) permission.launch(Manifest.permission.POST_NOTIFICATIONS) else openSettings()
            },
            openSettings = openSettings,
            alumniMeetingYear = ready.alumniMeetingYear,
            exactAlarms = exactAlarms,
            openExactAlarms = {
                if (Build.VERSION.SDK_INT >= 31) context.startActivity(
                    Intent(
                        Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM,
                        Uri.fromParts(LinkScheme.Package.code, context.packageName, null)
                    )
                )
            })

        else -> Unit
    }
}
