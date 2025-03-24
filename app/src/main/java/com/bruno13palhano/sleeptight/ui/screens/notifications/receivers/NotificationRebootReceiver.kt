package com.bruno13palhano.sleeptight.ui.screens.notifications.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.bruno13palhano.sleeptight.ui.screens.notifications.work.NotificationWork

class NotificationRebootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            val workManager = WorkManager.getInstance(context)

            val notificationWork = OneTimeWorkRequestBuilder<NotificationWork>()
                .build()
            workManager.enqueueUniqueWork(
                "restart notifications",
                ExistingWorkPolicy.REPLACE,
                notificationWork,
            )
        }
    }
}
