package com.bruno13palhano.sleeptight.ui.mediaplayer

import android.app.PendingIntent
import android.app.PendingIntent.getActivity
import android.app.TaskStackBuilder
import android.content.Intent
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import com.bruno13palhano.sleeptight.MainActivity

@OptIn(UnstableApi::class)
class SleepTightPlaybackService : PlaybackService() {
    companion object {
        private const val FLAGS = PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    }

    override fun getSingleTopActivity(): PendingIntent? {
        return getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            FLAGS,
        )
    }

    override fun getBackStackedActivity(): PendingIntent? {
        return TaskStackBuilder.create(this).run {
            addNextIntent(Intent(this@SleepTightPlaybackService, MainActivity::class.java))
            getPendingIntent(0, FLAGS)
        }
    }
}
