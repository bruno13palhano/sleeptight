package com.bruno13palhano.sleeptight.ui.screens

import android.icu.text.DateFormat
import android.icu.util.Calendar
import android.icu.util.TimeZone
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.authentication.DefaultUserFirebase
import com.bruno13palhano.authentication.UserAuthentication
import com.bruno13palhano.core.data.data.CommonDataContract
import com.bruno13palhano.core.data.data.UserDataContract
import com.bruno13palhano.core.data.di.DefaultBabyStatusRep
import com.bruno13palhano.core.data.di.DefaultNapRep
import com.bruno13palhano.core.data.di.DefaultNotificationRep
import com.bruno13palhano.core.data.di.DefaultUserRep
import com.bruno13palhano.model.BabyStatus
import com.bruno13palhano.model.Nap
import com.bruno13palhano.model.Notification
import com.bruno13palhano.model.User
import com.bruno13palhano.sleeptight.ui.util.DateFormatUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @DefaultUserFirebase private val authentication: UserAuthentication,
    @DefaultUserRep private val userRepository: UserDataContract<User>,
    @DefaultBabyStatusRep private val babyStatusRepository: CommonDataContract<BabyStatus>,
    @DefaultNapRep private val napRepository: CommonDataContract<Nap>,
    @DefaultNotificationRep private val notificationRepository: CommonDataContract<Notification>
) : ViewModel() {
    private val _babyName = MutableStateFlow("")
    val babyName = _babyName.asStateFlow()
        .stateIn(
            scope = viewModelScope,
            initialValue = "",
            started = WhileSubscribed(5_000)
        )

    private val _profileImage = MutableStateFlow("")
    val profileImage = _profileImage.asStateFlow()
        .stateIn(
            scope = viewModelScope,
            initialValue = "",
            started = WhileSubscribed(5_000)
        )

    private val _height = MutableStateFlow(0F)
    val height = _height.asStateFlow()
        .map { "$it cm" }
        .stateIn(
            scope = viewModelScope,
            initialValue = "",
            started = WhileSubscribed(5_000)
        )

    private val _weight = MutableStateFlow(0F)
    val weight = _weight.asStateFlow()
        .map { "$it kg" }
        .stateIn(
            scope = viewModelScope,
            initialValue = "",
            started = WhileSubscribed(5_000)
        )

    private val _notificationTitle = MutableStateFlow("")
    val notificationTitle = _notificationTitle.asStateFlow()
        .stateIn(
            scope = viewModelScope,
            initialValue = "",
            started = WhileSubscribed(5_000)
        )

    private val _notificationDate = MutableStateFlow(0L)
    val notificationDate = _notificationDate.asStateFlow()
        .map {
            DateFormatUtil.format(it)
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = "",
            started = WhileSubscribed(5_000)
        )

    private val _napTitle = MutableStateFlow("")
    val napTitle = _napTitle.asStateFlow()
        .stateIn(
            scope = viewModelScope,
            initialValue = "",
            started = WhileSubscribed(5_000)
        )

    private val _napDate = MutableStateFlow(0L)
    val napDate = _napDate.asStateFlow()
        .map {
            DateFormatUtil.format(it)
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = "",
            started = WhileSubscribed(5_000)
        )

    private val _napSleepingTime = MutableStateFlow(0L)
    val napSleepingTime = _napSleepingTime.asStateFlow()
        .map {
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            calendar.timeInMillis = it

            DateFormat.getPatternInstance(DateFormat.HOUR24_MINUTE).format(calendar)
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = "",
            started = WhileSubscribed(5_000)
        )



    private fun isUserAuthenticated() = authentication.isUserAuthenticated()

    init {
        viewModelScope.launch {
            userRepository.getById(authentication.getCurrentUser().id).collect {
                _babyName.value = it.babyName
                _profileImage.value = it.babyUrlPhoto
            }
        }

        viewModelScope.launch {
            babyStatusRepository.getLast().collect {
                _height.value = it.height
                _weight.value = it.weight
            }
        }

        viewModelScope.launch {
            notificationRepository.getLast().collect {
                _notificationTitle.value = it.title
                _notificationDate.value = it.date
            }
        }

        viewModelScope.launch {
            napRepository.getLast().collect {
                _napTitle.value = it.title
                _napDate.value = it.date
                _napSleepingTime.value = it.sleepingTime
            }
        }
    }

    private val isLogged = isUserAuthenticated()
    val homeState = flow {
        emit(
            if (isLogged) {
                HomeState.LoggedIn
            } else {
                HomeState.NotLoggedIn
            }
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = HomeState.Loading
        )

    sealed class HomeState {
        object Loading: HomeState()
        object LoggedIn: HomeState()
        object NotLoggedIn: HomeState()
    }
}