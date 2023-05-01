package com.bruno13palhano.sleeptight.ui.mediaplayer

import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.RawResourceDataSource
import com.bruno13palhano.sleeptight.R

@UnstableApi object ListOfMusics {
    val sleepMusics = listOf(
        SleepMusic(
            id = "gusttavo_lima_01",
            title = "Bloqueado",
            album = "Ao vivo",
            artist = "Gusttavo Lima",
            genre = "Sertanejo",
            source = RawResourceDataSource.buildRawResourceUri(R.raw.bloqueado),
            image = null
        ),
        SleepMusic(
            id = "gusttavo_lima_02",
            title = "Ficha limpa",
            album = "Ao vivo",
            artist = "Gusttavo Lima",
            genre = "Sertanejo",
            source = RawResourceDataSource.buildRawResourceUri(R.raw.ficha_limpa),
            image = null
        ),
        SleepMusic(
            id = "gusttavo_lima_03",
            title = "Não me arranha",
            album = "Ao vivo",
            artist = "Gusttavo Lima",
            genre = "Sertanejo",
            source = RawResourceDataSource.buildRawResourceUri(R.raw.nao_me_arranha),
            image = null
        ),
        SleepMusic(
            id = "gusttavo_lima_04",
            title = "Quebrando protocolo",
            album = "Ao vivo",
            artist = "Gusttavo Lima",
            genre = "Sertanejo",
            source = RawResourceDataSource.buildRawResourceUri(R.raw.quebrando_protocolo),
            image = null
        ),
        SleepMusic(
            id = "ze_neto_e_cristiano_01",
            title = "Decida",
            album = "Ao vivo",
            artist = "Zé Neto e Cristiano",
            genre = "Sertanejo",
            source = RawResourceDataSource.buildRawResourceUri(R.raw.decida),
            image = null
        ),
        SleepMusic(
            id = "ze_neto_e_cristiano_02",
            title = "Saudade",
            album = "Ao vivo",
            artist = "Zé Neto e Cristiano",
            genre = "Sertanejo",
            source = RawResourceDataSource.buildRawResourceUri(R.raw.saudade),
            image = null
        ),
        SleepMusic(
            id = "ze_neto_e_cristiano_03",
            title = "Uma vez por mês",
            album = "Ao vivo",
            artist = "Zé Neto e Cristiano",
            genre = "Sertanejo",
            source = RawResourceDataSource.buildRawResourceUri(R.raw.uma_vez_por_mes),
            image = null
        )
    )
}