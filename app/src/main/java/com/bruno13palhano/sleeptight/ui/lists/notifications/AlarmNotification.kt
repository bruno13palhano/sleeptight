package com.bruno13palhano.sleeptight.ui.lists.notifications

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.os.Build
import com.bruno13palhano.model.Notification

private const val PRIMARY_CHANNEL_ID = "primary_notification_channel"

class AlarmNotification(
    private val notification: Notification,
    private val notificationManager: NotificationManager,
    private val alarmManager: AlarmManager
) {

    fun updateAlarmManager(notifyPendingIntent: PendingIntent) {
        alarmManager.cancel(notifyPendingIntent)
        setAlarmManager(notifyPendingIntent)
    }

    fun setAlarmManager(notifyPendingIntent: PendingIntent) {
        val dateAndTime = getNotificationDateAndTime(notification.time, notification.date)

        if (notification.repeat) {
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                dateAndTime,
                AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                notifyPendingIntent
            )
        } else {
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                dateAndTime,
                notifyPendingIntent
            )
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

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                PRIMARY_CHANNEL_ID, "SleepTight notification", NotificationManager.IMPORTANCE_HIGH)

            notificationChannel.enableLights(true)
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Notifies users"
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}