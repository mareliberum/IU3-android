package com.bmstu.iu3.core.data

import android.Manifest
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import com.bmstu.iu3.core.notifications.NotificationContract

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val manager = context.getSystemService(NotificationManager::class.java)
        if (Build.VERSION.SDK_INT >= 33 && context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) return
        val launch = context.packageManager.getLaunchIntentForPackage(context.packageName) ?: return
        val content = PendingIntent.getActivity(
            context,
            0,
            launch,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notification = Notification.Builder(context, NotificationContract.CHANNEL)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(intent.getStringExtra(NotificationContract.TITLE))
            .setContentText(intent.getStringExtra(NotificationContract.DETAIL))
            .setStyle(
                Notification.BigTextStyle()
                    .bigText(intent.getStringExtra(NotificationContract.DETAIL))
            )
            .setContentIntent(content).setAutoCancel(true).build()
        manager.notify(intent.getIntExtra(NotificationContract.ID, 0), notification)
    }
}
