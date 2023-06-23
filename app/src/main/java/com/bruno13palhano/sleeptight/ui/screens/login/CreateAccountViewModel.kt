package com.bruno13palhano.sleeptight.ui.createaccount

import android.graphics.Bitmap
import android.icu.text.DateFormat
import android.icu.util.Calendar
import androidx.core.graphics.createBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.authentication.DefaultUserFirebase
import com.bruno13palhano.authentication.UserAuthentication
import com.bruno13palhano.core.data.di.DefaultUserRep
import com.bruno13palhano.core.data.repository.UserRepository
import com.bruno13palhano.model.User
import com.bruno13palhano.sleeptight.ui.util.DateFormatUtil
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateAccountViewModel @Inject constructor(
    @DefaultUserFirebase private val authentication: UserAuthentication,
    @DefaultUserRep private val userRepository: UserRepository
) : ViewModel() {
    private val calendar = Calendar.getInstance()

    private val _loginStatus = MutableStateFlow<LoginStatus>(LoginStatus.Default)
    val loginStatus = _loginStatus.asStateFlow()

    val username = MutableStateFlow("")
    val email = MutableStateFlow("")
    val password = MutableStateFlow("")
    val babyName = MutableStateFlow("")
    val birthplace = MutableStateFlow("")
    val height = MutableStateFlow("")
    val weight = MutableStateFlow("")

    val isUserDataNotEmpty = combine(username, email, password) { username, email, password ->
        isUserDataNotEmpty(username, email, password)
    }
        .stateIn(
            scope = viewModelScope,
            initialValue = false,
            started = WhileSubscribed(5_000)
        )

    val isBabyNameNotEmpty = babyName.asStateFlow()
        .map {
            it != ""
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = false,
            started = WhileSubscribed(5_000)
        )

    val isBirthplaceNotEmpty = birthplace.asStateFlow()
        .map {
            it != ""
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = false,
            started = WhileSubscribed(5_000)
        )

    val isHeightAndWeightNotEmpty = combine(height, weight) { height, weight ->
        height.trim() != "" && weight.trim() != ""
    }
        .stateIn(
            scope = viewModelScope,
            initialValue = false,
            started = WhileSubscribed(5_000)
        )

    private val _photo = MutableStateFlow(createBitmap(1,1))
    private val _photoUi = MutableStateFlow("")
    val photoUi = _photoUi.asStateFlow()

    fun setPhoto(photo: Bitmap, uri: String) {
        _photo.value = photo
        _photoUi.value = uri
    }

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

    fun insertUser() {
        val user = User(
            username = username.value,
            email = email.value,
            password = password.value,
            babyName = babyName.value,
            birthplace = birthplace.value,
            birthdate = _date.value,
            birthtime = _time.value,
            height = stringToFloat(height.value),
            weight = stringToFloat(weight.value)
        )

        loading()
        if (isUserDataNotEmpty(user.username, user.email, user.password)) {
            authentication.createUser(
                user = user,
                onSuccess = {
                    updateUserUrlPhoto(
                        photo = _photo.value,
                        onSuccess = { newPhotoUrl, userUid ->
                            userRepository.insertUser(
                                User(
                                    id = userUid,
                                    username = user.username,
                                    email = user.email,
                                    babyName = user.babyName,
                                    babyUrlPhoto = newPhotoUrl,
                                    birthplace = user.birthplace,
                                    birthdate = user.birthdate,
                                    birthtime = user.birthtime,
                                    height = user.height,
                                    weight = user.weight
                                )
                            )
                            _loginStatus.value = LoginStatus.Success
                            clearUserValues()
                        },
                        onFail = {
                            _loginStatus.value = LoginStatus.Error
                        }
                    )
                },
                onFail = {
                    _loginStatus.value = LoginStatus.Error
                }
            )
        }
    }

    private fun clearUserValues() {
        username.value = ""
        email.value = ""
        password.value = ""
        _photo.value = createBitmap(1,1)
        _photoUi.value = ""
        birthplace.value = ""
        _date.value = MaterialDatePicker.todayInUtcMilliseconds()
        _time.value = calendar.timeInMillis
        height.value = ""
        weight.value = ""
    }

    private fun updateUserUrlPhoto(
        photo: Bitmap,
        onSuccess: (newPhotoUrl: String, userUid: String) -> Unit,
        onFail: () -> Unit
    ) {
        authentication.updateUserUrlPhoto(
            photo = photo,
            onSuccess = { newPhotoUrl, userUid ->
                onSuccess(newPhotoUrl, userUid)
            },
            onFail = {
                onFail()
            }
        )
    }

    private fun stringToFloat(value: String): Float {
        return try {
            value.toFloat()
        } catch (ignored: Exception) { 0F }
    }

    private fun isUserDataNotEmpty(username: String, email: String, password: String): Boolean =
        username.trim() != "" && email.trim() != "" && password.trim() != ""

    private fun loading() {
        _loginStatus.value = LoginStatus.Loading
    }

    sealed class LoginStatus {
        object Loading: LoginStatus()
        object Error: LoginStatus()
        object Success: LoginStatus()
        object Default: LoginStatus()
    }
}