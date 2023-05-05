package com.bruno13palhano.sleeptight.ui.babystatus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.di.DefaultBabyStatusRep
import com.bruno13palhano.core.data.repository.BabyStatusRepository
import com.bruno13palhano.model.BabyStatus
import com.bruno13palhano.sleeptight.ui.util.DateFormatUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BabyStatusViewModel @Inject constructor(
    @DefaultBabyStatusRep private val babyStatusRepository: BabyStatusRepository
) : ViewModel() {
    val title = MutableStateFlow("")
    val height = MutableStateFlow("")
    val weight = MutableStateFlow("")

    val date = MutableStateFlow(0L)
    val dateUi = date.asStateFlow()
        .map {
            DateFormatUtil.format(it)
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = "",
            started = WhileSubscribed(5_000)
        )

    fun setDate(date: Long) {
        this.date.value = date
    }

    fun getBabyStatus(id: Long) {
        viewModelScope.launch {
            babyStatusRepository.getBabyStatusByIdStream(id).collect {
                title.value = it.title
                height.value = it.height.toString()
                weight.value = it.weight.toString()
                date.value = it.date
            }
        }
    }

    fun deleteBabyStatus(id: Long) {
        viewModelScope.launch {
            babyStatusRepository.deleteBabyStatusById(id)
        }
    }

    fun updateBabyStatus(id: Long) {
        viewModelScope.launch {
            val babyStatus = BabyStatus(
                id = id,
                title = title.value,
                date = date.value,
                height = stringToFloat(height.value),
                weight = stringToFloat(weight.value)
            )
            babyStatusRepository.updateBabyStatus(babyStatus)
        }
    }

    private fun stringToFloat(value: String): Float {
        return try {
            value.toFloat()
        } catch (ignored: Exception) {
            0F
        }
    }
}