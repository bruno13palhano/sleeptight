package com.bruno13palhano.sleeptight.ui.screens.naps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.data.CommonDataContract
import com.bruno13palhano.core.data.di.NapRep
import com.bruno13palhano.model.Nap
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class NapsViewModel @Inject constructor(
    @NapRep private val napRepository: CommonDataContract<Nap>,
) : ViewModel() {

    val uiState = napRepository.getAll()
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = emptyList(),
        )

    fun deleteNap(napId: Long, onNapDeleted: () -> Unit) {
        viewModelScope.launch {
            napRepository.deleteById(napId)
        }
        onNapDeleted()
    }
}
