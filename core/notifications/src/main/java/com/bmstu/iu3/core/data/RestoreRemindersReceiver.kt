package com.bmstu.iu3.core.data

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

@dagger.hilt.android.AndroidEntryPoint
class RestoreRemindersReceiver : BroadcastReceiver() {
    @javax.inject.Inject
    lateinit var jobs: com.bmstu.iu3.core.notifications.ReminderJobs
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action !in setOf(
                Intent.ACTION_BOOT_COMPLETED,
                Intent.ACTION_MY_PACKAGE_REPLACED,
                AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED
            )
        ) return
        jobs.schedule()
        jobs.enqueueNow()
    }
}
