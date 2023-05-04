package com.bruno13palhano.sleeptight.ui.babystatus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.di.DefaultBabyStatusRep
import com.bruno13palhano.core.data.repository.BabyStatusRepository
import com.bruno13palhano.model.BabyStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class BabyStatusViewModel @Inject constructor(
    @DefaultBabyStatusRep private val babyStatusRepository: BabyStatusRepository
) : ViewModel() {

    private val _allBabyStatus = MutableStateFlow(emptyList<BabyStatus>())
    val allBabyStatus = _allBabyStatus.asStateFlow()
        .stateIn(
            scope = viewModelScope,
            initialValue = emptyList(),
            started = WhileSubscribed(5_000)
        )
}