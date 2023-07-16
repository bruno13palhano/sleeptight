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
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @DefaultUserFirebase private val authentication: UserAuthentication,
    @UserRep private val userRepository: UserDataContract<User>,
    @BabyStatusRep private val babyStatusRepository: CommonDataContract<BabyStatus>,
    @NapRep private val napRepository: CommonDataContract<Nap>,
    @NotificationRep private val notificationRepository: CommonDataContract<Notification>
) : ViewModel() {
    val babyInfoState = userRepository.getById(authentication.getCurrentUser().id)
        .map {
            BabyInfoState(
                babyName = it.babyName,
                momName = it.username,
                profileImage = it.babyUrlPhoto
            )
        }
            .stateIn(
                scope = viewModelScope,
                initialValue = BabyInfoState(),
                started = WhileSubscribed(5_000)
            )

    val babyStatusState = combine(
        userRepository.getById(authentication.getCurrentUser().id),
        babyStatusRepository.getLast()
    ) { user, babyStatus ->
        BabyStatusState(
            id = babyStatus.id,
            date = if (babyStatus.date == 0L) {
                DateFormatUtil.format(user.birthdate)
            } else {
                   DateFormatUtil.format(babyStatus.date)
            },
            height = if (babyStatus.height == 0F) "${user.height}cm" else "${babyStatus.height}cm",
            weight = if (babyStatus.weight == 0F) "${user.weight}kg" else "${babyStatus.weight}kg"
        )
    }
        .stateIn(
            scope = viewModelScope,
            initialValue = BabyStatusState(),
            started = WhileSubscribed(5_000)
        )

    val notificationState = notificationRepository.getLast()
        .map {
            NotificationState(
                id = it.id,
                title = it.title,
                date = DateFormatUtil.format(it.date),
            )

        }
        .stateIn(
            scope = viewModelScope,
            initialValue = NotificationState(),
            started = WhileSubscribed(5_000)
        )

    val napState = napRepository.getLast()
        .map {
            NapState(
                id = it.id,
                title = it.title,
                date = DateFormatUtil.format(it.date),
                sleepingTime = convertSleepingTime(it.sleepingTime)
            )
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = NapState(),
            started = WhileSubscribed(5_000)
        )

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
        val id: Long = 0L,
        val date: String = "",
        val height: String = "",
        val weight: String = ""
    )

    data class NotificationState(
        val id: Long = 0L,
        val title: String = "",
        val date: String = ""
    )

    data class NapState(
        val id: Long = 0L,
        val title: String = "",
        val date: String = "",
        val sleepingTime: String = ""
    )
}