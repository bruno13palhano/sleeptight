package com.bruno13palhano.sleeptight.ui.screens.babystatus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.data.CommonDataContract
import com.bruno13palhano.core.data.di.DefaultBabyStatusRep
import com.bruno13palhano.model.BabyStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BabyStatusListViewModel @Inject constructor(
    @DefaultBabyStatusRep private val babyStatusRepository: CommonDataContract<BabyStatus>
) : ViewModel() {

    val babyStatusList = babyStatusRepository.getAll()
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun deleteBabyStatus(babyStatusId: Long, onBabyStatusDeleted: () -> Unit) {
        viewModelScope.launch {
            babyStatusRepository.deleteById(babyStatusId)
        }
        onBabyStatusDeleted()
    }
}