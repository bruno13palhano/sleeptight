package com.bruno13palhano.sleeptight.ui.screens.analytics

import androidx.lifecycle.ViewModel
import com.bruno13palhano.core.data.di.DefaultBabyStatusRep
import com.bruno13palhano.core.data.repository.BabyStatusRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class AnalyticsBabyStatusChartViewModel @Inject constructor(
    @DefaultBabyStatusRep babyStatusRepository: BabyStatusRepository
) : ViewModel() {
    val babyStatusChartUi = babyStatusRepository.all
        .map {
            val heightList = mutableListOf<Float>()
            val weightList = mutableListOf<Float>()

            it.forEach { babyStatus ->
                heightList.add(babyStatus.height)
                weightList.add(babyStatus.weight)
            }

            BabyStatusChartUi(
                allHeight = heightList,
                allWeight = weightList
            )
        }

    data class BabyStatusChartUi(
        val allHeight: List<Float> = emptyList(),
        val allWeight: List<Float> = emptyList()
    )
}