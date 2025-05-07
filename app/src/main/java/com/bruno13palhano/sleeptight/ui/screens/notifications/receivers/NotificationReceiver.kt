package com.bruno13palhano.sleeptight.ui.screens.notifications.receivers

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.bruno13palhano.sleeptight.R

private const val PRIMARY_CHANNEL_ID = "primary_notification_channel"

class NotificationReceiver : BroadcastReceiver() {
    private lateinit var notificationManager: NotificationManager

    override fun onReceive(context: Context?, intent: Intent?) {
        notificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        deliverNotification(context, intent?.extras)
    }

    private fun deliverNotification(context: Context, extras: Bundle?) {
        val notificationId = extras?.getInt("id") ?: 0
        val title = extras?.getString("title") ?: ""
        val description = extras?.getString("description") ?: ""

        val baseUrl = "sleeptight://notifications/notification"
        val deepLinkIntent = Intent(Intent.ACTION_VIEW).apply {
            data = "$baseUrl/$notificationId".toUri()
            setPackage(context.packageName)
        }
        val contentPendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            deepLinkIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE,
        )

        val builder = NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setContentTitle(title)
            .setContentText(description)
            .setContentIntent(contentPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)

        notificationManager.notify(notificationId, builder.build())
    }
}
