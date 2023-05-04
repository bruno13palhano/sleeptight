package com.bruno13palhano.sleeptight.ui.babystatus.newbabystatus

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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewBabyStatusViewModel @Inject constructor(
    @DefaultBabyStatusRep private val babyStatusRepository: BabyStatusRepository
) : ViewModel() {

    private val _date = MutableStateFlow(MaterialDatePicker.todayInUtcMilliseconds())
    val date = _date.asStateFlow()
        .stateIn(
            scope = viewModelScope,
            initialValue = 0L,
            started = WhileSubscribed(5_000)
        )
    val dateUi = _date.map {
        DateFormatUtil.format(it)
    }
        .stateIn(
            scope = viewModelScope,
            initialValue = "",
            started = WhileSubscribed(5_000)
        )

    fun setDate(dateInMillis: Long) {
        _date.value = dateInMillis
    }

    val title = MutableStateFlow("")
    val height = MutableStateFlow("")
    val weight = MutableStateFlow("")

    fun insertBabyStatus() {
        val babyStatus = BabyStatus(
            id = 0L,
            title = title.value,
            date = date.value,
            height = height.value.toFloat(),
            weight = weight.value.toFloat()
        )
        viewModelScope.launch {
            babyStatusRepository.insertBabyStatus(babyStatus)
        }
    }
}