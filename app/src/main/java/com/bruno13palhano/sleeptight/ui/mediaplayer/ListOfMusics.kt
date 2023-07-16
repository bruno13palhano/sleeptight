package com.bruno13palhano.sleeptight.ui.mediaplayer

import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.RawResourceDataSource
import com.bruno13palhano.sleeptight.R

@UnstableApi object ListOfMusics {
    val sleepMusics = listOf(
        SleepMusic(
            id = "fan_01",
            title = "Fan",
            album = "SleepTight",
            artist = "SleepTight",
            genre = "White Noise",
            source = RawResourceDataSource.buildRawResourceUri(R.raw.fan),
            image = null
        ),
        SleepMusic(
            id = "heater_02",
            title = "Heater",
            album = "SleepTight",
            artist = "SleepTight",
            genre = "White Noise",
            source = RawResourceDataSource.buildRawResourceUri(R.raw.heater),
            image = null
        ),
        SleepMusic(
            id = "ocean_03",
            title = "Ocean",
            album = "SleepTight",
            artist = "SleepTight",
            genre = "White Noise",
            source = RawResourceDataSource.buildRawResourceUri(R.raw.white_noise),
            image = null
        ),
        SleepMusic(
            id = "rain_04",
            title = "Rain",
            album = "SleepTight",
            artist = "SleepTight",
            genre = "White Noise",
            source = RawResourceDataSource.buildRawResourceUri(R.raw.rain),
            image = null
        ),
        SleepMusic(
            id = "water_05",
            title = "Water",
            album = "SleepTight",
            artist = "SleepTight",
            genre = "White Noise",
            source = RawResourceDataSource.buildRawResourceUri(R.raw.water),
            image = null
        ),
        SleepMusic(
            id = "white_noise_06",
            title = "White Noise",
            album = "SleepTight",
            artist = "SleepTight",
            genre = "White Noise",
            source = RawResourceDataSource.buildRawResourceUri(R.raw.white_noise),
            image = null
        ),
        SleepMusic(
            id = "lullaby_07",
            title = "Lullaby",
            album = "SleepTight",
            artist = "SleepTight",
            genre = "White Noise",
            source = RawResourceDataSource.buildRawResourceUri(R.raw.lullaby),
            image = null
        )
    )
}