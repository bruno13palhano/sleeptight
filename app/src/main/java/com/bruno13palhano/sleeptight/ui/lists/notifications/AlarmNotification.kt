package com.bruno13palhano.sleeptight.ui.lists.notifications

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.os.Build

private const val PRIMARY_CHANNEL_ID = "primary_notification_channel"
private const val PRIMARY_CHANNEL_NAME = "SleepTight notification"
private const val PRIMARY_CHANNEL_DESCRIPTION = "Notifies users"

class AlarmNotification(
    private val notificationManager: NotificationManager,
    private val alarmManager: AlarmManager
) {

    fun cancelNotification(notifyPendingIntent: PendingIntent, notificationId: Int) {
        notificationManager.cancel(notificationId)
        alarmManager.cancel(notifyPendingIntent)
    }

    fun updateAlarmManager(
        notifyPendingIntent: PendingIntent,
        notificationId: Int,
        time: Long,
        date: Long,
        repeat: Boolean
    ) {
        cancelNotification(notifyPendingIntent, notificationId)
        setAlarmManager(notifyPendingIntent, time, date, repeat)
    }

    fun setAlarmManager(
        notifyPendingIntent: PendingIntent,
        time: Long,
        date: Long,
        repeat: Boolean
    ) {
        val dateAndTime = getNotificationDateAndTime(time, date)

        if (repeat) {
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                dateAndTime,
                AlarmManager.INTERVAL_DAY,
                notifyPendingIntent
            )
        } else {
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                dateAndTime,
                notifyPendingIntent
            )
        }

        createNotificationChannel()
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

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                PRIMARY_CHANNEL_ID, PRIMARY_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)

            notificationChannel.enableLights(true)
            notificationChannel.enableVibration(true)
            notificationChannel.description = PRIMARY_CHANNEL_DESCRIPTION
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}