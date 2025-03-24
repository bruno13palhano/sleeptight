package com.bruno13palhano.sleeptight.ui.mediaplayer

import android.net.Uri

data class SleepMusic(
    val id: String,
    val title: String,
    val album: String,
    val artist: String,
    val genre: String,
    val source: Uri?,
    val image: Uri?,
)
