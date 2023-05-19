package com.bruno13palhano.sleeptight.ui.lists.nap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.di.DefaultNapRep
import com.bruno13palhano.core.data.repository.NapRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class NapsViewModel @Inject constructor(
    @DefaultNapRep private val napRepository: NapRepository
) : ViewModel() {

    val uiState = napRepository.getAllStream()
        .stateIn(
            scope = viewModelScope,
            initialValue = emptyList(),
            started = WhileSubscribed(5_000)
        )
}