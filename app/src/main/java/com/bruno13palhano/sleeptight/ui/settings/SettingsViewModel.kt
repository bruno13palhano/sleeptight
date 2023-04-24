package com.bruno13palhano.sleeptight.ui.settings

import android.icu.text.DateFormat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.authentication.DefaultUserFirebase
import com.bruno13palhano.authentication.UserAuthentication
import com.bruno13palhano.core.data.di.DefaultUserRep
import com.bruno13palhano.core.data.repository.UserRepository
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
class SettingsViewModel @Inject constructor(
    @DefaultUserFirebase private val authentication: UserAuthentication,
    @DefaultUserRep private val userRepository: UserRepository
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

    init {
        viewModelScope.launch {
            userRepository.getUserByIdStream(authentication.getCurrentUser().id).collect {
                localUi.value = it.birthplace
                date.value = it.birthdate
                time.value = it.birthTime
                height.value = it.height
                weight.value = it.weight
            }
        }
    }

    fun logout() {
        authentication.logout()
    }
}