package com.bruno13palhano.sleeptight.ui.screens.home

import android.icu.text.DateFormat
import android.icu.util.Calendar
import android.icu.util.TimeZone
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.authentication.DefaultUserFirebase
import com.bruno13palhano.authentication.UserAuthentication
import com.bruno13palhano.core.di.BabyStatusRep
import com.bruno13palhano.core.di.NapRep
import com.bruno13palhano.core.di.NotificationRep
import com.bruno13palhano.core.di.UserRep
import com.bruno13palhano.core.repository.BabyStatusRepository
import com.bruno13palhano.core.repository.NapRepository
import com.bruno13palhano.core.repository.NotificationRepository
import com.bruno13palhano.core.repository.UserRepository
import com.bruno13palhano.sleeptight.ui.util.DateFormatUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class HomeViewModel @Inject constructor(
    @DefaultUserFirebase private val authentication: UserAuthentication,
    @UserRep private val userRepository: UserRepository,
    @BabyStatusRep private val babyStatusRepository: BabyStatusRepository,
    @NapRep private val napRepository: NapRepository,
    @NotificationRep private val notificationRepository: NotificationRepository,
) : ViewModel() {
    val babyInfoState = userRepository.getById(authentication.getCurrentUser().id)
        .map {
            BabyInfoState(
                babyName = it.babyName,
                momName = it.username,
                profileImage = it.babyUrlPhoto,
            )
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = BabyInfoState(),
            started = SharingStarted.Companion.WhileSubscribed(5_000),
        )

    val babyStatusState = combine(
        userRepository.getById(authentication.getCurrentUser().id),
        babyStatusRepository.getLast(),
    ) { user, babyStatus ->
        BabyStatusState(
            id = babyStatus.id,
            date = if (babyStatus.date == 0L) {
                DateFormatUtil.format(user.birthdate)
            } else {
                DateFormatUtil.format(babyStatus.date)
            },
            height = if (babyStatus.height == 0F) "${user.height}cm" else "${babyStatus.height}cm",
            weight = if (babyStatus.weight == 0F) "${user.weight}kg" else "${babyStatus.weight}kg",
        )
    }
        .stateIn(
            scope = viewModelScope,
            initialValue = BabyStatusState(),
            started = SharingStarted.Companion.WhileSubscribed(5_000),
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
            started = SharingStarted.Companion.WhileSubscribed(5_000),
        )

    val napState = napRepository.getLast()
        .map {
            NapState(
                id = it.id,
                title = it.title,
                date = DateFormatUtil.format(it.date),
                sleepingTime = convertSleepingTime(it.sleepingTime),
            )
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = NapState(),
            started = SharingStarted.Companion.WhileSubscribed(5_000),
        )

    private val isLogged = isUserAuthenticated()
    val homeState = flow {
        emit(
            if (isLogged) {
                HomeState.LoggedIn
            } else {
                HomeState.NotLoggedIn
            },
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5_000),
            initialValue = HomeState.Loading,
        )

    private fun convertSleepingTime(sleepingTime: Long): String {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.timeInMillis = sleepingTime

        return DateFormat.getPatternInstance(DateFormat.HOUR24_MINUTE).format(calendar)
    }

    private fun isUserAuthenticated() = authentication.isUserAuthenticated()

    sealed class HomeState {
        object Loading : HomeState()
        object LoggedIn : HomeState()
        object NotLoggedIn : HomeState()
    }

    data class BabyInfoState(
        val babyName: String = "",
        val momName: String = "",
        val profileImage: String = "",
    )

    data class BabyStatusState(
        val id: Long = 0L,
        val date: String = "",
        val height: String = "",
        val weight: String = "",
    )

    data class NotificationState(
        val id: Long = 0L,
        val title: String = "",
        val date: String = "",
    )

    data class NapState(
        val id: Long = 0L,
        val title: String = "",
        val date: String = "",
        val sleepingTime: String = "",
    )
}
