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
import com.bruno13palhano.core.data.di.BabyStatusRep
import com.bruno13palhano.core.data.di.NapRep
import com.bruno13palhano.core.data.di.NotificationRep
import com.bruno13palhano.core.data.di.UserRep
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
    @UserRep private val userRepository: UserDataContract<User>,
    @BabyStatusRep private val babyStatusRepository: CommonDataContract<BabyStatus>,
    @NapRep private val napRepository: CommonDataContract<Nap>,
    @NotificationRep private val notificationRepository: CommonDataContract<Notification>
) : ViewModel() {
    private val _babyName = MutableStateFlow("")
    private val _username = MutableStateFlow("")
    private val _profileImage = MutableStateFlow("")

    private val _babyTitle = MutableStateFlow("")
    private val _babyDateAtBirth = MutableStateFlow(0L)
    private val _babyDate = MutableStateFlow(0L)
    private val _heightAtBirth = MutableStateFlow(0F)
    private val _weightAtBirth = MutableStateFlow(0F)
    private val _height = MutableStateFlow(0F)
    private val _weight = MutableStateFlow(0F)

    private val _notificationTitle = MutableStateFlow("")
    private val _notificationDate = MutableStateFlow(0L)

    private val _napTitle = MutableStateFlow("")
    private val _napDate = MutableStateFlow(0L)
    private val _napSleepingTime = MutableStateFlow(0L)

    val babyInfoState = combine(_babyName, _username, _profileImage) { babyName, momName, profileImage ->
        BabyInfoState(
            babyName = babyName,
            momName = momName,
            profileImage = profileImage
        )
    }
        .stateIn(
            scope = viewModelScope,
            initialValue = BabyInfoState(),
            started = WhileSubscribed(5_000)
        )

    val babyStatusState = combine(
        _babyTitle,
        combine(_babyDate, _babyDateAtBirth) {date, dateAtBirth ->
            if (date == 0L) dateAtBirth else date
        },
        combine(_height, _heightAtBirth) { height, heightAtBirth ->
            if (height == 0F) heightAtBirth else height
        },
        combine(_weight, _weightAtBirth) { weight, weightAtBirth ->
            if (weight == 0F) weightAtBirth else weight
        }
    ) { title, date, height, weight ->
        BabyStatusState(
            title = title,
            date = DateFormatUtil.format(date),
            height = "${height}cm",
            weight = "${weight}kg"
        )
    }
        .stateIn(
            scope = viewModelScope,
            initialValue = BabyStatusState(),
            started = WhileSubscribed(5_000)
        )

    val notificationState = combine(_notificationTitle, _notificationDate) { title, date ->
        NotificationState(
            title = title,
            date = DateFormatUtil.format(date),
        )
    }
        .stateIn(
            scope = viewModelScope,
            initialValue = NotificationState(),
            started = WhileSubscribed(5_000)
        )

    val napState = combine(_napTitle, _napDate, _napSleepingTime) { title, date, sleepingTime ->
        NapState(
            title = title,
            date = DateFormatUtil.format(date),
            sleepingTime = convertSleepingTime(sleepingTime)
        )
    }
        .stateIn(
            scope = viewModelScope,
            initialValue = NapState(),
            started = WhileSubscribed(5_000)
        )

    init {
        viewModelScope.launch {
            userRepository.getById(authentication.getCurrentUser().id).collect {
                _username.value = it.username
                _babyName.value = it.babyName
                _profileImage.value = it.babyUrlPhoto
                _babyDateAtBirth.value = it.birthdate
                _heightAtBirth.value = it.height
                _weightAtBirth.value = it.weight
            }
        }

        viewModelScope.launch {
            babyStatusRepository.getLast().collect {
                _babyTitle.value = it.title
                _babyDate.value = it.date
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

    private fun convertSleepingTime(sleepingTime: Long): String {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.timeInMillis = sleepingTime

        return DateFormat.getPatternInstance(DateFormat.HOUR24_MINUTE).format(calendar)
    }

    private fun isUserAuthenticated() = authentication.isUserAuthenticated()

    sealed class HomeState {
        object Loading: HomeState()
        object LoggedIn: HomeState()
        object NotLoggedIn: HomeState()
    }

    data class BabyInfoState(
        val babyName: String = "",
        val momName: String = "",
        val profileImage: String = ""
    )

    data class BabyStatusState(
        val title: String = "",
        val date: String = "",
        val height: String = "",
        val weight: String = ""
    )

    data class NotificationState(
        val title: String = "",
        val date: String = ""
    )

    data class NapState(
        val title: String = "",
        val date: String = "",
        val sleepingTime: String = ""
    )
}