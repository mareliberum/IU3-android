package com.bmstu.iu3.core.notifications

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.bmstu.iu3.core.data.ReminderWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ReminderJobs @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    fun enqueueNow() {
        WorkManager.getInstance(context).enqueueUniqueWork(
            RESTORE_WORK,
            ExistingWorkPolicy.REPLACE,
            OneTimeWorkRequestBuilder<ReminderWorker>().build()
        )
    }

    fun schedule() {
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            PERIODIC_WORK,
            ExistingPeriodicWorkPolicy.KEEP,
            PeriodicWorkRequestBuilder<ReminderWorker>(1, TimeUnit.HOURS).build()
        )
    }

    private companion object {
        const val RESTORE_WORK = "iu3.reminders.restore"
        const val PERIODIC_WORK = "iu3.reminders"
    }
}
