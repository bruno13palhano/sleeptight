package com.bruno13palhano.sleeptight.ui.screens.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.di.BabyStatusRep
import com.bruno13palhano.core.repository.BabyStatusRepository
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.FloatEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class AnalyticsBabyStatusChartViewModel @Inject constructor(
    @BabyStatusRep babyStatusRepository: BabyStatusRepository,
) : ViewModel() {
    val babyStatusChartUi = babyStatusRepository.getAll()
        .map {
            val heightList = mutableListOf<Float>()
            val weightList = mutableListOf<Float>()

            it.forEach { babyStatus ->
                heightList.add(babyStatus.height)
                weightList.add(babyStatus.weight)
            }

            ChartEntryModelProducer(
                heightList.mapIndexed { index, height ->
                    FloatEntry(index.toFloat(), height)
                },
                weightList.mapIndexed { index, weight ->
                    FloatEntry(index.toFloat(), weight)
                },
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = ChartEntryModelProducer(),
        )
}
