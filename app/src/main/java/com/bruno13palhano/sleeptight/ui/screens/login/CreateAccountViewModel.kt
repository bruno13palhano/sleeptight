package com.bruno13palhano.sleeptight.ui.screens.login

import android.icu.text.DateFormat
import android.icu.util.Calendar
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.authentication.DefaultUserFirebase
import com.bruno13palhano.authentication.UserAuthentication
import com.bruno13palhano.core.data.data.UserDataContract
import com.bruno13palhano.core.data.di.UserRep
import com.bruno13palhano.model.User
import com.bruno13palhano.sleeptight.ui.util.CalendarUtil
import com.bruno13palhano.sleeptight.ui.util.DateFormatUtil
import com.bruno13palhano.sleeptight.ui.util.getHour
import com.bruno13palhano.sleeptight.ui.util.getMinute
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateAccountViewModel @Inject constructor(
    @DefaultUserFirebase private val authentication: UserAuthentication,
    @UserRep private val userRepository: UserDataContract<User>
) : ViewModel() {
    private val _loginStatus = MutableStateFlow<LoginStatus>(LoginStatus.Default)
    val loginStatus = _loginStatus.asStateFlow()
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = LoginStatus.Default
        )

    var username by mutableStateOf("")
        private set
    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var babyName by mutableStateOf("")
        private set
    var birthplace by mutableStateOf("")
        private set
    var birthdateInMillis by mutableLongStateOf(MaterialDatePicker.todayInUtcMilliseconds())
        private set
    var birthdate by mutableStateOf(DateFormatUtil.format(birthdateInMillis))
        private set
    var birthtimeInMillis by mutableLongStateOf(Calendar.getInstance().timeInMillis)
        private set
    var birthtimeHour by mutableIntStateOf(getHour(birthtimeInMillis))
        private set
    var birthtimeMinute by mutableIntStateOf(getMinute(birthtimeInMillis))
        private set
    var birthtime: String by mutableStateOf(DateFormat.getPatternInstance(DateFormat.HOUR24_MINUTE)
        .format(birthtimeInMillis))
        private set
    var height by mutableStateOf("")
        private set
    var weight by mutableStateOf("")
        private set
    var photoUri by mutableStateOf<Uri>(Uri.EMPTY)
        private set
    var photoByteArray by mutableStateOf(ByteArray(1024))
        private set

    fun updateUsername(username: String) {
        this.username = username
    }

    fun updateEmail(email: String) {
        this.email = email
    }

    fun updatePassword(password: String) {
        this.password = password
    }

    fun updateBabyName(babyName: String) {
        this.babyName = babyName
    }

    fun updateBirthplace(birthplace: String) {
        this.birthplace = birthplace
    }

    fun updatePhotoUri(photoUri: Uri) {
        this.photoUri = photoUri
    }

    fun updatePhotoByteArray(photoByteArray: ByteArray) {
        this.photoByteArray = photoByteArray
    }

    fun updateDate(birthdate: Long) {
        birthdateInMillis = birthdate
        this.birthdate = DateFormatUtil.format(birthdate)
    }

    fun updateTime(hour: Int, minute: Int) {
        birthtimeInMillis = CalendarUtil.timeToMilliseconds(hour, minute)
        this.birthtime = DateFormat.getPatternInstance(DateFormat.HOUR24_MINUTE).format(birthtimeInMillis)
    }

    fun updateHeight(height: String) {
        this.height = height
    }

    fun updateWeight(weight: String) {
        this.weight = weight
    }

    val isUserBasicDataNotEmpty: StateFlow<Boolean> = snapshotFlow {
        isUserDataNotEmpty(username, email, password)
    }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = false
        )

    fun isBabyNameNotEmpty() = babyName.trim() != ""
    fun isBirthplaceNotEmpty() = birthplace.trim() != ""
    fun isHeightAndWeightNotEmpty() = height.trim() != "" && weight.trim() != ""

    fun insertUser() {
        val user = User(
            username = username,
            email = email,
            password = password,
            babyName = babyName,
            birthplace = birthplace,
            birthdate = birthdateInMillis,
            birthtime = birthtimeInMillis,
            height = stringToFloat(height),
            weight = stringToFloat(weight)
        )

        loading()
        if (isUserDataNotEmpty(user.username, user.email, user.password)) {
            authentication.createUser(
                user = user,
                onSuccess = {
                    updateUserUrlPhoto(
                        photo = photoByteArray,
                        onSuccess = { newPhotoUrl, userUid ->
                            viewModelScope.launch {
                                userRepository.insert(
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
                            }
                            _loginStatus.value = LoginStatus.Success
                            clearUserValues()
                        },
                        onFail = {
                            _loginStatus.value = LoginStatus.Default
                        }
                    )
                },
                onFail = {
                    _loginStatus.value = LoginStatus.Default
                }
            )
        }
    }

    private fun clearUserValues() {
        username = ""
        email = ""
        password = ""
        photoUri = Uri.EMPTY
        photoByteArray = ByteArray(1024)
        birthplace = ""
        birthtimeInMillis = MaterialDatePicker.todayInUtcMilliseconds()
        birthtimeInMillis = Calendar.getInstance().timeInMillis
        height = ""
        weight = ""
    }

    private fun updateUserUrlPhoto(
        photo: ByteArray,
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
        object Success: LoginStatus()
        object Default: LoginStatus()
    }
}