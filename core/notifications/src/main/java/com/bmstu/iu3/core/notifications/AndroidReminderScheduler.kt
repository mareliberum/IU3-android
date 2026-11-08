package com.bmstu.iu3.core.notifications

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import com.bmstu.iu3.core.data.ReminderReceiver
import com.bmstu.iu3.core.model.PlannedReminder
import kotlinx.coroutines.sync.withLock

internal class AndroidReminderScheduler(
    private val context: Context,
    private val formatter: ReminderFormatter,
    private val dispatcher: kotlinx.coroutines.CoroutineDispatcher,
) : ReminderScheduler {
    private val manager = context.getSystemService(NotificationManager::class.java)
    private val alarms = context.getSystemService(AlarmManager::class.java)
    override fun canScheduleExact(): Boolean =
        Build.VERSION.SDK_INT < 31 || alarms.canScheduleExactAlarms()

    private val mutex = kotlinx.coroutines.sync.Mutex()
    override suspend fun replace(reminders: List<PlannedReminder>) =
        kotlinx.coroutines.withContext(dispatcher) {
            mutex.withLock { replaceAlarms(reminders) }
        }

    private fun replaceAlarms(reminders: List<PlannedReminder>) {
        (0 until NotificationContract.CAPACITY).forEach { index ->
            alarms.cancel(
                pending(
                    index,
                    Intent(context, ReminderReceiver::class.java)
                )
            )
        }
        manager.createNotificationChannel(
            NotificationChannel(
                NotificationContract.CHANNEL,
                context.getString(R.string.notifications_reminder_channel),
                NotificationManager.IMPORTANCE_DEFAULT
            )
        )
        if (!manager.areNotificationsEnabled() || Build.VERSION.SDK_INT >= 33 && context.checkSelfPermission(
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) return
        reminders.take(NotificationContract.CAPACITY).forEachIndexed { index, reminder ->
            val intent = Intent(context, ReminderReceiver::class.java)
                .putExtra(NotificationContract.TITLE, formatter.title(reminder))
                .putExtra(NotificationContract.DETAIL, formatter.body(reminder))
                .putExtra(NotificationContract.ID, reminder.key.hashCode())
            val operation = pending(index, intent)
            if (canScheduleExact()) {
                try {
                    alarms.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        reminder.instant.toEpochMilli(),
                        operation
                    )
                } catch (_: SecurityException) {
                    alarms.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        reminder.instant.toEpochMilli(),
                        operation
                    )
                }
            } else alarms.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                reminder.instant.toEpochMilli(),
                operation
            )
        }
    }

    private fun pending(index: Int, intent: Intent): PendingIntent = PendingIntent.getBroadcast(
        context, index, intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
}
