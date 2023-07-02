package com.bruno13palhano.sleeptight.ui.screens.babystatus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.di.DefaultBabyStatusRep
import com.bruno13palhano.core.data.repository.BabyStatusRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class BabyStatusListViewModel @Inject constructor(
    @DefaultBabyStatusRep private val babyStatusRepository: BabyStatusRepository
) : ViewModel() {

    val babyStatusList = babyStatusRepository.all
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun deleteBabyStatus(babyStatusId: Long, onBabyStatusDeleted: () -> Unit) {
        babyStatusRepository.deleteById(babyStatusId)
        onBabyStatusDeleted()
    }
}