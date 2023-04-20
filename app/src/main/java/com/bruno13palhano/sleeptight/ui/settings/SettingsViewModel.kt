package com.bruno13palhano.sleeptight.ui.settings

import android.icu.text.DateFormat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.sleeptight.ui.util.DateFormatUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(

) : ViewModel() {

    private val _isEditable = MutableStateFlow(false)
    val isEditable = _isEditable.asStateFlow()

    fun toggleEditable() {
        _isEditable.value = !_isEditable.value
    }

    fun setEditable(isEditable: Boolean) {
        _isEditable.value = isEditable
    }

    private val date = MutableStateFlow(0L)
    val dateUi = date.asStateFlow()
        .map {
            DateFormatUtil.format(it)
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = "",
            started = WhileSubscribed(5_000)
        )

    fun sateBirthDate(date: Long) {
        this.date.value = date
    }

    private val time = MutableStateFlow(0L)
    val timeUi = time.asStateFlow()
        .map {
            DateFormat.getPatternInstance(DateFormat.HOUR24_MINUTE).format(it)
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = "",
            started = WhileSubscribed(5_000)
        )

    fun setBirthTime(time: Long) {
        this.time.value = time
    }

    val localUi = MutableStateFlow("")

    private val height = MutableStateFlow(0F)
    val heightUi = height.asStateFlow()
        .map {
            String.format("%.1fcm", it)
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = "",
            started = WhileSubscribed(5_000)
        )

    fun setHeight(height: Float) {
        this.height.value = height
    }

    private val weight = MutableStateFlow(0F)
    val weightUi = weight.asStateFlow()
        .map {
            String.format("%.1fkg", it)
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = "",
            started = WhileSubscribed(5_000)
        )

    fun setWeight(weight: Float) {
        this.weight.value = weight
    }
}