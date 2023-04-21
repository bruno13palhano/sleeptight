package com.bruno13palhano.sleeptight.ui.createaccount

import android.icu.text.DateFormat
import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.model.User
import com.bruno13palhano.sleeptight.ui.util.DateFormatUtil
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CreateAccountViewModel @Inject constructor(

) : ViewModel() {
    private val calendar = Calendar.getInstance()

    val username = MutableStateFlow("")
    val email = MutableStateFlow("")
    val password = MutableStateFlow("")
    val babyName = MutableStateFlow("")
    val birthplace = MutableStateFlow("")

    private val _date = MutableStateFlow(MaterialDatePicker.todayInUtcMilliseconds())
    val date = _date.asStateFlow()
    val dateUi = _date.asStateFlow()
        .map {
            DateFormatUtil.format(it)
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = "",
            started = WhileSubscribed(5_000)
        )

    fun setDate(date: Long) {
        _date.value = date
    }

    private val _time = MutableStateFlow(calendar.timeInMillis)
    val time = _time.asStateFlow()
    val timeUi = _time.asStateFlow()
        .map {
            DateFormat.getPatternInstance(DateFormat.HOUR24_MINUTE).format(it)
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = "",
            started = WhileSubscribed(5_000)
        )

    fun setTime(time: Long) {
        _time.value = time
    }

    private val _height = MutableStateFlow(0F)
    val height = _height.asStateFlow()
    val heightUi = _height.asStateFlow()
        .map {
            String.format("%.1fcm", it)
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = "",
            started = WhileSubscribed(5_000)
        )

    fun setHeight(height: Float) {
        _height.value = height
    }

    private val _weight = MutableStateFlow(0F)
    val weight = _weight.asStateFlow()
    val weightUi = _weight.asStateFlow()
        .map {
            String.format("%.1fkg", it)
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = "",
            started = WhileSubscribed(5_000)
        )

    fun setWeight(weight: Float) {
        _weight.value = weight
    }

    fun insertUser() {
        clearUserValues()
    }

    private fun clearUserValues() {
        username.value = ""
        email.value = ""
        password.value = ""
        birthplace.value = ""
        _date.value = MaterialDatePicker.todayInUtcMilliseconds()
        _time.value = calendar.timeInMillis
        _height.value = 0F
        _weight.value = 0F
    }
}