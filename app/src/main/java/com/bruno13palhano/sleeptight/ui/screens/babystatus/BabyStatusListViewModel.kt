package com.bruno13palhano.sleeptight.ui.screens.babystatus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.CommonDataContract
import com.bruno13palhano.core.di.BabyStatusRep
import com.bruno13palhano.model.BabyStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class BabyStatusListViewModel @Inject constructor(
    @BabyStatusRep private val babyStatusRepository: CommonDataContract<BabyStatus>,
) : ViewModel() {

    val babyStatusList = babyStatusRepository.getAll()
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = emptyList(),
        )

    fun deleteBabyStatus(babyStatusId: Long, onBabyStatusDeleted: () -> Unit) {
        viewModelScope.launch {
            babyStatusRepository.deleteById(babyStatusId)
        }
        onBabyStatusDeleted()
    }
}
