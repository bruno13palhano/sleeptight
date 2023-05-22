package com.bruno13palhano.sleeptight.ui.lists.babystatus.newbabystatus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.di.DefaultBabyStatusRep
import com.bruno13palhano.core.data.repository.BabyStatusRepository
import com.bruno13palhano.model.BabyStatus
import com.bruno13palhano.sleeptight.ui.util.DateFormatUtil
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewBabyStatusViewModel @Inject constructor(
    @DefaultBabyStatusRep private val babyStatusRepository: BabyStatusRepository
) : ViewModel() {

    val date = MutableStateFlow(MaterialDatePicker.todayInUtcMilliseconds())
    val dateUi = date.map {
        DateFormatUtil.format(it)
    }
        .stateIn(
            scope = viewModelScope,
            initialValue = "",
            started = WhileSubscribed(5_000)
        )

    fun setDate(dateInMillis: Long) {
        date.value = dateInMillis
    }

    val title = MutableStateFlow("")
    val height = MutableStateFlow("")
    val weight = MutableStateFlow("")

    val heightAndWeightValue = combine(height, weight) { height, weight ->
        height != "" && weight != ""
    }
        .stateIn(
            scope = viewModelScope,
            initialValue = false,
            started = WhileSubscribed(5_000)
        )

    fun insertBabyStatus() {
        viewModelScope.launch {
            val babyStatus = BabyStatus(
                id = 0L,
                title = title.value,
                date = date.value,
                height = stringToFloat(height.value),
                weight = stringToFloat(weight.value)
            )
            babyStatusRepository.insert(babyStatus)
            restoresValues()
        }
    }

    private fun stringToFloat(value: String): Float {
        return try {
            value.toFloat()
        } catch (ignored: Exception) { 0F }
    }

    private fun restoresValues() {
        date.value = MaterialDatePicker.todayInUtcMilliseconds()
        title.value = ""
        height.value = ""
        weight.value = ""
    }
}