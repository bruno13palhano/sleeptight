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
import android.os.IBinder
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.media3.common.AudioAttributes
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.ui.PlayerNotificationManager
import com.bruno13palhano.sleeptight.MainActivity

@UnstableApi
class PlaybackService : Service() {
    private var mediaSession: MediaSession? = null
    private lateinit var player: ExoPlayer
    private var notificationManager: PlayerNotificationManager? = null
    private val binder = PlaybackBinder()

    inner class PlaybackBinder : Binder() {
        fun getMediaSession(): MediaSession? = mediaSession
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        player = ExoPlayer.Builder(this)
            .setAudioAttributes(AudioAttributes.DEFAULT, true)
            .build()
        mediaSession = MediaSession.Builder(this, player).build()

        MediaItemTree.initialize()
        val allMusics = MediaItemTree.getChildren("[allItems]")

        allMusics?.let {
            player.addMediaItems(it)
        }
        player.prepare()

        player.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                if (isPlaying && notificationManager == null) {
                    setupNotificationManager()
                }
            }
        })

        setupNotificationManager()
    }

    private fun setupNotificationManager() {
        if (notificationManager == null) {
            notificationManager = PlayerNotificationManager.Builder(
                this,
                NOTIFICATION_ID,
                CHANNEL_ID,
            )
                .setMediaDescriptionAdapter(object :
                    PlayerNotificationManager.MediaDescriptionAdapter {
                    override fun getCurrentContentTitle(player: Player): CharSequence {
                        return player.currentMediaItem?.mediaMetadata?.title.toString()
                    }

                    override fun createCurrentContentIntent(player: Player): PendingIntent? {
                        val intent = Intent(this@PlaybackService, MainActivity::class.java)
                        return PendingIntent.getActivity(
                            this@PlaybackService,
                            0,
                            intent,
                            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
                        )
                    }

                    override fun getCurrentContentText(player: Player): CharSequence? {
                        return player.currentMediaItem?.mediaMetadata?.title ?: "Unknown"
                    }

                    override fun getCurrentLargeIcon(
                        player: Player,
                        callback: PlayerNotificationManager.BitmapCallback,
                    ): Bitmap? {
                        return null
                    }
                })
                .setNotificationListener(object : PlayerNotificationManager.NotificationListener {
                    override fun onNotificationCancelled(
                        notificationId: Int,
                        dismissedByUser: Boolean,
                    ) {
                        stopForeground(STOP_FOREGROUND_DETACH)
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
                })
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
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        notificationManager?.setPlayer(null)
        notificationManager = null
        super.onDestroy()
    }

    companion object {
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "sleeptight_playback_channel"
        private const val CHANNEL_NAME = "SleepTight Playback Service"
    }
}
