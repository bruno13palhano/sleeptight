package com.bruno13palhano.sleeptight.ui.lists.notifications.work

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.bruno13palhano.core.data.di.DefaultNotificationRep
import com.bruno13palhano.core.data.repository.NotificationRepository
import com.bruno13palhano.sleeptight.ui.lists.notifications.AlarmNotification
import com.bruno13palhano.sleeptight.ui.lists.notifications.receivers.NotificationReceiver
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class NotificationWork @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted params: WorkerParameters,
    @DefaultNotificationRep private val notificationRepository: NotificationRepository
) : CoroutineWorker(context, params) {

    private lateinit var notificationManager: NotificationManager

    override suspend fun doWork(): Result {
        notificationRepository.getAllNotificationsStream().collect {
            notificationManager =
                context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

            it.forEach { notification ->
                if (notification.repeat) {
                    val notifyIntent = Intent(context, NotificationReceiver::class.java)
                    notifyIntent.apply {
                        putExtra("id", notification.id.toInt())
                        putExtra("title", notification.title)
                        putExtra("description", notification.description)
                    }

                    val notifyPendingIntent = PendingIntent.getBroadcast(
                        context,
                        notification.id.toInt(),
                        notifyIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )

                    val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager

                    val alarmNotification = AlarmNotification(
                        notificationManager = notificationManager,
                        alarmManager = alarmManager
                    )

                    alarmNotification.setAlarmManager(
                        notifyPendingIntent = notifyPendingIntent,
                        time = notification.time,
                        date = notification.date,
                        repeat = true
                    )
                }
            }
        }

        return Result.success()
    }
}