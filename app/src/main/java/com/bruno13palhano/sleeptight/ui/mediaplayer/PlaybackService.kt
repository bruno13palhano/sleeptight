package com.bruno13palhano.sleeptight.ui.mediaplayer

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Binder
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.media3.common.AudioAttributes
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.ui.PlayerNotificationManager
import com.bruno13palhano.sleeptight.MainActivity
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.dependencies.DependenciesApplication

@UnstableApi
class PlaybackService : Service() {
    private var mediaSession: MediaSession? = null
    private lateinit var player: ExoPlayer
    private var notificationManager: PlayerNotificationManager? = null
    private val binder = PlaybackBinder()
    private val mainHandler = Handler(Looper.getMainLooper())

    inner class PlaybackBinder : Binder() {
        fun getMediaSession(): MediaSession? = mediaSession
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()

        val tempIntent = Intent(this, MainActivity::class.java)
        val tempPendingIntent = PendingIntent.getActivity(
            this,
            0,
            tempIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val tempNotification = Notification.Builder(this, CHANNEL_ID)
                .setContentTitle("SleepTight Playback")
                .setContentText("Initializing playback service...")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(tempPendingIntent)
                .build()

            startForeground(NOTIFICATION_ID, tempNotification)
        }

        val mediaItems = (application as DependenciesApplication).getPreloadedMediaItems()

        mainHandler.post {
            initializePlayerAndSession(mediaItems = mediaItems)
            setupNotificationManager()
        }
    }

    private fun initializePlayerAndSession(mediaItems: List<MediaItem>) {
        player = ExoPlayer.Builder(this)
            .setAudioAttributes(AudioAttributes.DEFAULT, true)
            .build()
        mediaSession = MediaSession.Builder(this, player).build()

        if (mediaItems.isNotEmpty()) {
            player.addMediaItems(mediaItems)
            player.prepare()
        }
    }

    private fun setupNotificationManager() {
        if (notificationManager == null) {
            notificationManager = PlayerNotificationManager.Builder(
                this,
                NOTIFICATION_ID,
                CHANNEL_ID,
            )
                .setMediaDescriptionAdapter(mediaDescription)
                .setNotificationListener(notificationListener)
                .build()

            notificationManager?.setPlayer(player)
            notificationManager?.setUseStopAction(true)
            notificationManager?.setSmallIcon(
                androidx.media3.session.R.drawable.media3_notification_small_icon,
            )
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW,
            )
            channel.setShowBadge(false)
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onDestroy() {
        mainHandler.post {
            mediaSession?.run {
                player.release()
                release()
                mediaSession = null
            }
            notificationManager?.setPlayer(null)
            notificationManager = null
        }
        stopForeground(STOP_FOREGROUND_REMOVE)
        super.onDestroy()
    }

    private val mediaDescription = object : PlayerNotificationManager.MediaDescriptionAdapter {
        override fun createCurrentContentIntent(player: Player): PendingIntent? {
            val intent = Intent(this@PlaybackService, MainActivity::class.java)
            return PendingIntent.getActivity(
                this@PlaybackService,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )
        }

        override fun getCurrentContentTitle(player: Player): CharSequence {
            return player.currentMediaItem?.mediaMetadata?.title
                ?: getString(R.string.unknown_label)
        }

        override fun getCurrentContentText(player: Player): CharSequence? {
            return player.currentMediaItem?.mediaMetadata?.title
                ?: getString(R.string.unknown_label)
        }

        override fun getCurrentLargeIcon(
            player: Player,
            callback: PlayerNotificationManager.BitmapCallback,
        ): Bitmap? {
            return null
        }
    }

    private val notificationListener = object : PlayerNotificationManager.NotificationListener {
        override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
            notificationManager?.setPlayer(null)
            notificationManager = null
        }

        override fun onNotificationPosted(
            notificationId: Int,
            notification: Notification,
            ongoing: Boolean,
        ) {
            if (ongoing) {
                startForeground(notificationId, notification)
            } else if (ActivityCompat.checkSelfPermission(
                    this@PlaybackService,
                    Manifest.permission.POST_NOTIFICATIONS,
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                stopForeground(STOP_FOREGROUND_DETACH)
                NotificationManagerCompat.from(this@PlaybackService)
                    .notify(notificationId, notification)
            }
        }
    }

    companion object {
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "sleeptight_playback_channel"
        private const val CHANNEL_NAME = "SleepTight Playback Service"
    }
}
