package com.bruno13palhano.sleeptight.ui.lists.notifications.work

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.os.Build
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.bruno13palhano.core.data.di.DefaultNotificationRep
import com.bruno13palhano.core.data.repository.NotificationRepository
import com.bruno13palhano.sleeptight.ui.lists.notifications.receivers.NotificationReceiver
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

private const val PRIMARY_CHANNEL_ID = "primary_notification_channel"

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

                    alarmManager.setRepeating(
                        AlarmManager.RTC_WAKEUP,
                        getNotificationDateAndTime(notification.time, notification.date),
                        AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                        notifyPendingIntent
                    )

                    createNotificationChannel(context)
                }
            }
        }

        return Result.success()
    }

    private fun createNotificationChannel(context: Context) {
        notificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                PRIMARY_CHANNEL_ID, "SleepTight notification", NotificationManager.IMPORTANCE_HIGH)

            notificationChannel.enableLights(true)
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Notifies user for test"
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun getNotificationDateAndTime(time: Long, date: Long): Long {
        val calendarTime = Calendar.getInstance()
        val calendarDate = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendarTime.timeInMillis = time
        calendarDate.timeInMillis = date

        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = calendarTime[Calendar.HOUR_OF_DAY]
        calendar[Calendar.MINUTE] = calendarTime[Calendar.MINUTE]
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.YEAR] = calendarDate[Calendar.YEAR]
        calendar[Calendar.MONTH] = calendarDate[Calendar.MONTH]
        calendar[Calendar.DAY_OF_MONTH] = calendarDate[Calendar.DAY_OF_MONTH]

        return calendar.timeInMillis
    }
}