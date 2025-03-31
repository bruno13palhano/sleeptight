package com.bruno13palhano.sleeptight.ui.mediaplayer

import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaItem.SubtitleConfiguration
import androidx.media3.common.MediaMetadata
import androidx.media3.common.util.UnstableApi
import com.google.common.collect.ImmutableList

@UnstableApi object MediaItemTree {
    private var treeNodes: MutableMap<String, MediaItemNode> = mutableMapOf()
    private var titleMap: MutableMap<String, MediaItemNode> = mutableMapOf()
    private var isInitialized = false
    private const val ROOT_ID = "[rootId]"
    private const val ALL_ITEMS = "[allItems]"

    init {
        initialize()
    }

    private class MediaItemNode(val item: MediaItem) {
        private val children: MutableList<MediaItem> = ArrayList()

        fun addChild(childID: String) {
            this.children.add(treeNodes[childID]!!.item)
        }

        fun getChildren(): List<MediaItem> {
            return ImmutableList.copyOf(children)
        }
    }

    private fun buildMediaItem(
        title: String,
        mediaId: String,
        isPlayable: Boolean,
        isBrowsable: Boolean,
        mediaType: @MediaMetadata.MediaType Int,
        subtitleConfigurations: List<SubtitleConfiguration> = mutableListOf(),
        album: String? = null,
        artist: String? = null,
        genre: String? = null,
        sourceUri: Uri? = null,
        imageUri: Uri? = null,
    ): MediaItem {
        val metadata = MediaMetadata.Builder()
            .setAlbumTitle(album)
            .setTitle(title)
            .setArtist(artist)
            .setGenre(genre)
            .setIsBrowsable(isBrowsable)
            .setIsPlayable(isPlayable)
            .setArtworkUri(imageUri)
            .setMediaType(mediaType)
            .build()

        return MediaItem.Builder()
            .setMediaId(mediaId)
            .setSubtitleConfigurations(subtitleConfigurations)
            .setMediaMetadata(metadata)
            .setUri(sourceUri)
            .build()
    }

    fun initialize() {
        if (isInitialized) return
        isInitialized = true

        treeNodes[ROOT_ID] = MediaItemNode(
            buildMediaItem(
                title = "Root Folder",
                mediaId = ROOT_ID,
                isPlayable = false,
                isBrowsable = true,
                mediaType = MediaMetadata.MEDIA_TYPE_FOLDER_MIXED,
            ),
        )

        treeNodes[ALL_ITEMS] = MediaItemNode(
            buildMediaItem(
                title = "All musics",
                mediaId = ALL_ITEMS,
                isPlayable = false,
                isBrowsable = true,
                mediaType = MediaMetadata.MEDIA_TYPE_MUSIC,
            ),
        )

        treeNodes[ROOT_ID]!!.addChild(ALL_ITEMS)

        val sleepMusics = ListOfMusics.sleepMusics
        sleepMusics.forEach { sleepMusic ->
            addNodeToTree(sleepMusic)
        }
    }

    private fun addNodeToTree(sleepMusic: SleepMusic) {
        val allMusicsInTree = ALL_ITEMS + sleepMusic.id

        if (!treeNodes.containsKey(allMusicsInTree)) {
            treeNodes[allMusicsInTree] = MediaItemNode(
                buildMediaItem(
                    title = sleepMusic.title,
                    mediaId = sleepMusic.id,
                    isPlayable = true,
                    isBrowsable = false,
                    mediaType = MediaMetadata.MEDIA_TYPE_MUSIC,
                    album = sleepMusic.album,
                    artist = sleepMusic.artist,
                    genre = sleepMusic.genre,
                    sourceUri = sleepMusic.source,
                    imageUri = sleepMusic.image,
                ),
            )
            treeNodes[ALL_ITEMS]!!.addChild(allMusicsInTree)
        }
    }

    fun getItem(id: String): MediaItem? {
        return treeNodes[id]?.item
    }

    fun getRootItem(): MediaItem {
        return treeNodes[ROOT_ID]!!.item
    }

    fun getChildren(id: String): List<MediaItem>? {
        return treeNodes[id]?.getChildren()
    }

    fun getRandomItem(): MediaItem {
        var curRoot = getRootItem()
        while (curRoot.mediaMetadata.isBrowsable == true) {
            val children = getChildren(curRoot.mediaId)!!
            curRoot = children.random()
        }
        return curRoot
    }

    fun getItemFromTitle(title: String): MediaItem? {
        return titleMap[title]?.item
    }
}
