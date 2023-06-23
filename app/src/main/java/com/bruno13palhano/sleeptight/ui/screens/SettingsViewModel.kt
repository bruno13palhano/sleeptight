package com.bruno13palhano.sleeptight.ui.screens

import android.graphics.Bitmap
import android.icu.text.DateFormat
import androidx.core.graphics.createBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.authentication.DefaultUserFirebase
import com.bruno13palhano.authentication.UserAuthentication
import com.bruno13palhano.core.data.di.DefaultUserRep
import com.bruno13palhano.core.data.repository.UserRepository
import com.bruno13palhano.model.User
import com.bruno13palhano.sleeptight.ui.util.CalendarUtil
import com.bruno13palhano.sleeptight.ui.util.DateFormatUtil
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
class SettingsViewModel @Inject constructor(
    @DefaultUserFirebase private val authentication: UserAuthentication,
    @DefaultUserRep private val userRepository: UserRepository
) : ViewModel() {

    private val isEditable = MutableStateFlow(false)

    fun activeEditable() {
        isEditable.value = true
    }

    private val _username = MutableStateFlow("")
    val username = _username.asStateFlow()
        .stateIn(
            scope = viewModelScope,
            initialValue = "",
            started = WhileSubscribed(5_000)
        )

    val date = MutableStateFlow(0L)
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

    val time = MutableStateFlow(0L)
    val timeUi = time.asStateFlow()
        .map {
            DateFormat.getPatternInstance(DateFormat.HOUR24_MINUTE).format(it)
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = "",
            started = WhileSubscribed(5_000)
        )

    fun setBirthTime(hour: Int, minute: Int) {
        this.time.value = CalendarUtil.timeToMilliseconds(hour, minute)
    }

    val birthplace = MutableStateFlow("")
    val height = MutableStateFlow("")
    val weight = MutableStateFlow("")
    val babyName = MutableStateFlow("")

    val isUserdataNotEmpty = combine(
        babyName, birthplace, height, weight, isEditable
    ) { babyName, birthplace, height, weight, isEditable ->
        ActionDoneStateUi(
            isUserDataNotEmpty = babyName.trim() != "" && birthplace.trim() != ""
                    && height.trim() != "" && weight.trim() != "",
            isEditable = isEditable
        )
    }
        .stateIn(
            scope = viewModelScope,
            initialValue = ActionDoneStateUi(),
            started = WhileSubscribed(5_000)
        )

    data class ActionDoneStateUi(
        val isUserDataNotEmpty: Boolean = false,
        val isEditable: Boolean = false
    )

    private val photo = MutableStateFlow("")
    val photoUi = photo.asStateFlow()
    private val bitmapPhoto = MutableStateFlow(createBitmap(1, 1))

    fun setBabyPhoto(bitmap: Bitmap, uri: String) {
        bitmapPhoto.value = bitmap
        photo.value = uri
    }

    private lateinit var userInDB: User

    init {
        viewModelScope.launch {
            userRepository.getUserByIdStream(authentication.getCurrentUser().id).collect {
                userInDB = it
                _username.value = it.username
                babyName.value = it.babyName
                photo.value = it.babyUrlPhoto
                birthplace.value = it.birthplace
                date.value = it.birthdate
                time.value = it.birthtime
                height.value = it.height.toString()
                weight.value = it.weight.toString()
            }
        }
    }

    fun updateUserValues() {
        isEditable.value = false
        val currentUser = authentication.getCurrentUser()

        val user = User(
            id = currentUser.id,
            username = currentUser.username,
            email = currentUser.email,
            babyName = babyName.value,
            babyUrlPhoto = photo.value,
            birthplace = birthplace.value,
            birthdate = date.value,
            birthtime = time.value,
            height = stringToFloat(height.value),
            weight = stringToFloat(weight.value)
        )

        if (userInDB != user) {
            if (userInDB.babyUrlPhoto != user.babyUrlPhoto) {
                authentication.updateUserUrlPhoto(
                    photo = bitmapPhoto.value,
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
        userRepository.updateUserUrlPhoto(urlPhoto, id)
    }

    private fun updateUserBabyNameInDataBase(babyName: String, id: String) {
        userRepository.updateUserBabyName(babyName, id)
    }

    private fun updateUserBirthplaceInDatabase(birthplace: String, id: String) {
        userRepository.updateUserBirthplace(birthplace, id)
    }

    private fun updateUserBirthdateInDatabase(birthdate: Long, id: String) {
        userRepository.updateUserBirthdate(birthdate, id)
    }

    private fun updateUserBirthtimeInDatabase(birthtime: Long, id: String) {
        userRepository.updateUserBirthtime(birthtime, id)
    }

    private fun updateUserHeight(height: Float, id: String) {
        userRepository.updateUserHeight(height, id)
    }

    private fun updateUserWeight(weight: Float, id: String) {
        userRepository.updateUserWeight(weight, id)
    }

    fun logout() {
        authentication.logout()
    }
}