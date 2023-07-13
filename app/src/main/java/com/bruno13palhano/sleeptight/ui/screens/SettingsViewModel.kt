package com.bruno13palhano.sleeptight.ui.screens

import android.icu.text.DateFormat
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.authentication.DefaultUserFirebase
import com.bruno13palhano.authentication.UserAuthentication
import com.bruno13palhano.core.data.data.UserDataContract
import com.bruno13palhano.core.data.di.DefaultUserRep
import com.bruno13palhano.model.User
import com.bruno13palhano.sleeptight.ui.util.CalendarUtil
import com.bruno13palhano.sleeptight.ui.util.DateFormatUtil
import com.bruno13palhano.sleeptight.ui.util.getHour
import com.bruno13palhano.sleeptight.ui.util.getMinute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @DefaultUserFirebase private val authentication: UserAuthentication,
    @DefaultUserRep private val userRepository: UserDataContract<User>
) : ViewModel() {

    private lateinit var userInDB: User
    private val _isEditable = MutableStateFlow(false)
    val isEditable = _isEditable.asStateFlow()
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = false
        )

    fun activeEditable() {
        _isEditable.value = true
    }

    var username by mutableStateOf("")
        private set
    var babyName by mutableStateOf("")
        private set
    var birthplace by mutableStateOf("")
        private set
    var birthdateInMillis by mutableLongStateOf(0L)
        private set
    var birthdate by mutableStateOf("")
        private set
    var birthtimeInMillis by mutableLongStateOf(0L)
        private set
    var birthtimeHour by mutableIntStateOf(0)
        private set
    var birthtimeMinute by mutableIntStateOf(0)
        private set
    var birthtime by mutableStateOf("")
        private set
    var height by mutableStateOf("")
        private set
    var weight by mutableStateOf("")
        private set
    var photoUri by mutableStateOf<Uri>(Uri.EMPTY)
        private set
    var photoByteArray by mutableStateOf(ByteArray(1024))
        private set

    fun updateBabyName(babyName: String) {
        this.babyName = babyName
    }

    fun updateBirthplace(birthplace: String) {
        this.birthplace = birthplace
    }

    fun updateBirthdate(birthdate: Long) {
        birthdateInMillis = birthdate
        this.birthdate = DateFormatUtil.format(birthdateInMillis)
    }

    fun updateBirthtime(hour: Int, minute: Int) {
        birthtimeInMillis = CalendarUtil.timeToMilliseconds(hour, minute)
        birthtime = DateFormat.getPatternInstance(DateFormat.HOUR24_MINUTE).format(birthtimeInMillis)
    }

    fun updateHeight(height: String) {
        this.height = height
    }

    fun updateWeight(weight: String) {
        this.weight = weight
    }

    fun updatePhotoUri(photoUri: Uri) {
        this.photoUri = photoUri
    }

    fun updatePhotoByteArray(photoByteArray: ByteArray) {
        this.photoByteArray = photoByteArray
    }

    data class ActionDoneStateUi(
        val isUserDataNotEmpty: Boolean = false,
        val isEditable: Boolean = false
    )

    init {
        viewModelScope.launch {
            userRepository.getById(authentication.getCurrentUser().id).collect {
                userInDB = it
                username = it.username
                babyName = it.babyName
                photoUri = Uri.parse(it.babyUrlPhoto)
                birthplace = it.birthplace
                birthdateInMillis = it.birthdate
                birthdate = DateFormatUtil.format(it.birthdate)
                birthtimeInMillis = it.birthtime
                birthtimeHour = getHour(it.birthtime)
                birthtimeMinute = getMinute(it.birthtime)
                birthtime = DateFormat.getPatternInstance(DateFormat.HOUR24_MINUTE).format(it.birthtime)
                height = it.height.toString()
                weight = it.weight.toString()
            }
        }
    }

    fun updateUserValues() {
        _isEditable.value = false
        val currentUser = authentication.getCurrentUser()

        val user = User(
            id = currentUser.id,
            username = currentUser.username,
            email = currentUser.email,
            babyName = babyName,
            babyUrlPhoto = photoUri.toString(),
            birthplace = birthplace,
            birthdate = birthdateInMillis,
            birthtime = birthtimeInMillis,
            height = stringToFloat(height),
            weight = stringToFloat(weight)
        )

        if (userInDB != user) {
            if (userInDB.babyUrlPhoto != user.babyUrlPhoto) {
                authentication.updateUserUrlPhoto(
                    photo = photoByteArray,
                    onSuccess = { newPhotoUrl, userUid ->
                        updateUserPhotoInDatabase(newPhotoUrl, userUid)
                    },
                    onFail = {}
                )
            }
            if (userInDB.babyName != user.babyName) {
                authentication.updateUserBabyNameInFirebaseFirestore(
                    babyName = user.babyName,
                    userUid = user.id,
                    onSuccess = { updateUserBabyNameInDataBase(user.babyName, user.id) },
                    onFail = {}
                )
            }
            if (userInDB.birthplace != user.birthplace) {
                authentication.updateUserBirthplaceInFirebaseFirestore(
                    birthplace = user.birthplace,
                    userUid = user.id,
                    onSuccess = { updateUserBirthplaceInDatabase(user.birthplace, user.id) },
                    onFail = {}
                )
            }
            if (userInDB.birthdate != user.birthdate) {
                authentication.updateUserBirthdateInFirebaseFirestore(
                    birthdate = user.birthdate,
                    userUid = user.id,
                    onSuccess = { updateUserBirthdateInDatabase(user.birthdate, user.id) },
                    onFail = {}
                )
            }
            if (userInDB.birthtime != user.birthtime) {
                authentication.updateUserBirthtimeInFirebaseFirestore(
                    birthtime = user.birthtime,
                    userUid = user.id,
                    onSuccess = { updateUserBirthtimeInDatabase(user.birthtime, user.id) },
                    onFail = {}
                )
            }
            if (userInDB.height != user.height) {
                authentication.updateUserHeightInFirebaseFirestore(
                    height = user.height,
                    userUid = user.id,
                    onSuccess = { updateUserHeight(user.height, user.id) },
                    onFail = {}
                )
            }
            if (userInDB.weight != user.weight) {
                authentication.updateUserWeightInFirebaseFirestore(
                    weight = user.weight,
                    userUid = user.id,
                    onSuccess = { updateUserWeight(user.weight, user.id) },
                    onFail = {}
                )
            }
        }
    }

    private fun stringToFloat(value: String): Float {
        return try {
            value.toFloat()
        } catch (ignored: Exception) { 0F }
    }

    private fun updateUserPhotoInDatabase(urlPhoto: String, id: String) {
        viewModelScope.launch {
            userRepository.updateUrlPhoto(urlPhoto, id)
        }
    }

    private fun updateUserBabyNameInDataBase(babyName: String, id: String) {
        viewModelScope.launch {
            userRepository.updateBabyName(babyName, id)
        }
    }

    private fun updateUserBirthplaceInDatabase(birthplace: String, id: String) {
        viewModelScope.launch {
            userRepository.updateBirthplace(birthplace, id)
        }
    }

    private fun updateUserBirthdateInDatabase(birthdate: Long, id: String) {
        viewModelScope.launch {
            userRepository.updateBirthdate(birthdate, id)
        }
    }

    private fun updateUserBirthtimeInDatabase(birthtime: Long, id: String) {
        viewModelScope.launch {
            userRepository.updateBirthtime(birthtime, id)
        }
    }

    private fun updateUserHeight(height: Float, id: String) {
        viewModelScope.launch {
            userRepository.updateHeight(height, id)
        }
    }

    private fun updateUserWeight(weight: Float, id: String) {
        viewModelScope.launch {
            userRepository.updateWeight(weight, id)
        }
    }

    fun logout() {
        authentication.logout()
    }
}