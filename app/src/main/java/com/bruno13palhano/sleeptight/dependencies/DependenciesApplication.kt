package com.bruno13palhano.sleeptight.dependencies

import android.app.Application
import androidx.annotation.OptIn
import androidx.hilt.work.HiltWorkerFactory
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.work.Configuration
import com.bruno13palhano.sleeptight.ui.mediaplayer.MediaItemTree
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@HiltAndroidApp
class DependenciesApplication : Application(), Configuration.Provider {
    @Inject lateinit var workerFactory: HiltWorkerFactory
    private val mediaItems = mutableListOf<MediaItem>()

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    @OptIn(UnstableApi::class)
    override fun onCreate() {
        super.onCreate()

        CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
            MediaItemTree.initialize()
            val items = MediaItemTree.getChildren("[allItems]") ?: emptyList()
            synchronized(mediaItems) {
                mediaItems.addAll(items)
            }
        }
    }

    fun getPreloadedMediaItems(): List<MediaItem> = synchronized(mediaItems) { mediaItems }
}
