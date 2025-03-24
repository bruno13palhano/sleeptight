package com.bruno13palhano.authentication

import com.bruno13palhano.model.User

interface UserAuthentication {
    fun createUser(user: User, onSuccess: (userUid: String) -> Unit, onFail: () -> Unit)
    fun login(email: String, password: String, onSuccess: (user: User) -> Unit, onFail: () -> Unit)
    fun logout()
    fun isUserAuthenticated(): Boolean
    fun getCurrentUser(): User
    fun updateUserUrlPhoto(
        photo: ByteArray,
        onSuccess: (newPhotoUrl: String, userUid: String) -> Unit,
        onFail: () -> Unit,
    )
    fun updateUsername(
        username: String,
        onSuccess: (newUsername: String, userUid: String) -> Unit,
        onFail: () -> Unit,
    )
    fun updateUserBabyNameInFirebaseFirestore(
        babyName: String,
        userUid: String,
        onSuccess: () -> Unit,
        onFail: () -> Unit,
    )
    fun updateUserBirthplaceInFirebaseFirestore(
        birthplace: String,
        userUid: String,
        onSuccess: () -> Unit,
        onFail: () -> Unit,
    )
    fun updateUserBirthdateInFirebaseFirestore(
        birthdate: Long,
        userUid: String,
        onSuccess: () -> Unit,
        onFail: () -> Unit,
    )
    fun updateUserBirthtimeInFirebaseFirestore(
        birthtime: Long,
        userUid: String,
        onSuccess: () -> Unit,
        onFail: () -> Unit,
    )
    fun updateUserHeightInFirebaseFirestore(
        height: Float,
        userUid: String,
        onSuccess: () -> Unit,
        onFail: () -> Unit,
    )
    fun updateUserWeightInFirebaseFirestore(
        weight: Float,
        userUid: String,
        onSuccess: () -> Unit,
        onFail: () -> Unit,
    )
}
