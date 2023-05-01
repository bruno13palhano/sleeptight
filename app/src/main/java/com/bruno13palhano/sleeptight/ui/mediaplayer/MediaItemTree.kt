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
    private const val ALBUM_ID = "[albumID]"
    private const val GENRE_ID = "[genreID]"
    private const val ARTIST_ID = "[artistID]"
    private const val ALBUM_PREFIX = "[album]"
    private const val GENRE_PREFIX = "[genre]"
    private const val ARTIST_PREFIX = "[artist]"
    private const val ITEM_PREFIX = "[item]"

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
        imageUri: Uri? = null
    ) : MediaItem {
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
                mediaId =  ROOT_ID,
                isPlayable = false,
                isBrowsable = true,
                mediaType = MediaMetadata.MEDIA_TYPE_FOLDER_MIXED
            )
        )
        treeNodes[ALBUM_ID] = MediaItemNode(
            buildMediaItem(
                title = "Album Folder",
                mediaId = ALBUM_ID,
                isPlayable = false,
                isBrowsable = true,
                mediaType = MediaMetadata.MEDIA_TYPE_FOLDER_ALBUMS
            )
        )
        treeNodes[ARTIST_ID] = MediaItemNode(
            buildMediaItem(
                title = "Artist Folder",
                mediaId = ARTIST_ID,
                isPlayable = false,
                isBrowsable = true,
                mediaType = MediaMetadata.MEDIA_TYPE_FOLDER_ARTISTS
            )
        )
        treeNodes[GENRE_ID] = MediaItemNode(
            buildMediaItem(
                title = "Genre Folder",
                mediaId = GENRE_ID,
                isPlayable = false,
                isBrowsable = true,
                mediaType = MediaMetadata.MEDIA_TYPE_FOLDER_GENRES
            )
        )
        treeNodes[ROOT_ID]!!.addChild(ALBUM_ID)
        treeNodes[ROOT_ID]!!.addChild(ARTIST_ID)
        treeNodes[ROOT_ID]!!.addChild(GENRE_ID)

        val sleepMusics = ListOfMusics.sleepMusics

        sleepMusics.forEach { sleepMusic ->
            addNodeToTree(sleepMusic)
        }
    }

    private fun addNodeToTree(sleepMusic: SleepMusic) {
        val idInTree = ITEM_PREFIX + sleepMusic.id
        val albumFolderIdInTree = ALBUM_PREFIX + sleepMusic.album
        val artistFolderIdInTree = ARTIST_PREFIX + sleepMusic.artist
        val genreFolderIdInTree = GENRE_PREFIX + sleepMusic.genre

        treeNodes[idInTree] = MediaItemNode(
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
                imageUri = sleepMusic.image
            )
        )
        titleMap[sleepMusic.title.lowercase()] = treeNodes[idInTree]!!

        if (!treeNodes.containsKey(albumFolderIdInTree)) {
            treeNodes[albumFolderIdInTree] = MediaItemNode(
                buildMediaItem(
                    title = sleepMusic.album,
                    mediaId = albumFolderIdInTree,
                    isPlayable = true,
                    isBrowsable = true,
                    mediaType = MediaMetadata.MEDIA_TYPE_ALBUM
                )
            )
            treeNodes[ALBUM_ID]!!.addChild(albumFolderIdInTree)
        }
        treeNodes[albumFolderIdInTree]!!.addChild(idInTree)

        if (!treeNodes.containsKey(artistFolderIdInTree)) {
            treeNodes[artistFolderIdInTree] = MediaItemNode(
                buildMediaItem(
                    title = sleepMusic.artist,
                    mediaId = artistFolderIdInTree,
                    isPlayable = true,
                    isBrowsable = true,
                    mediaType = MediaMetadata.MEDIA_TYPE_ARTIST
                )
            )
            treeNodes[ARTIST_ID]!!.addChild(artistFolderIdInTree)
        }
        treeNodes[artistFolderIdInTree]!!.addChild(idInTree)

        if (!treeNodes.containsKey(genreFolderIdInTree)) {
            treeNodes[genreFolderIdInTree] = MediaItemNode(
                buildMediaItem(
                    title = sleepMusic.title,
                    mediaId = genreFolderIdInTree,
                    isPlayable = true,
                    isBrowsable = true,
                    mediaType = MediaMetadata.MEDIA_TYPE_GENRE
                )
            )
            treeNodes[GENRE_ID]!!.addChild(genreFolderIdInTree)
        }
        treeNodes[genreFolderIdInTree]!!.addChild(idInTree)
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