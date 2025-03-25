package com.bruno13palhano.sleeptight.ui.mediaplayer

import android.content.ContentResolver
import android.net.Uri
import androidx.media3.common.util.UnstableApi
import com.bruno13palhano.sleeptight.R

@UnstableApi object ListOfMusics {
    private val builder = Uri.Builder()
        .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)

    val sleepMusics = listOf(
        SleepMusic(
            id = "fan_01",
            title = "Fan",
            album = "Sounds to Sleep",
            artist = "SleepTight",
            genre = "White Noise",
            source = builder.path(R.raw.fan.toString()).build(),
            image = null,
        ),
        SleepMusic(
            id = "heater_02",
            title = "Heater",
            album = "Sounds to Sleep",
            artist = "SleepTight",
            genre = "White Noise",
            source = builder.path(R.raw.heater.toString()).build(),
            image = null,
        ),
        SleepMusic(
            id = "ocean_03",
            title = "Ocean",
            album = "Sounds to Sleep",
            artist = "SleepTight",
            genre = "White Noise",
            source = builder.path(R.raw.white_noise.toString()).build(),
            image = null,
        ),
        SleepMusic(
            id = "rain_04",
            title = "Rain",
            album = "Sounds to Sleep",
            artist = "SleepTight",
            genre = "White Noise",
            source = builder.path(R.raw.rain.toString()).build(),
            image = null,
        ),
        SleepMusic(
            id = "water_05",
            title = "Water",
            album = "Sounds to Sleep",
            artist = "SleepTight",
            genre = "White Noise",
            source = builder.path(R.raw.water.toString()).build(),
            image = null,
        ),
        SleepMusic(
            id = "white_noise_06",
            title = "White Noise",
            album = "Sounds to Sleep",
            artist = "SleepTight",
            genre = "White Noise",
            source = builder.path(R.raw.white_noise.toString()).build(),
            image = null,
        ),
        SleepMusic(
            id = "lullaby_07",
            title = "Lullaby",
            album = "Sounds to Sleep",
            artist = "SleepTight",
            genre = "White Noise",
            source = builder.path(R.raw.lullaby.toString()).build(),
            image = null,
        ),
    )
}
