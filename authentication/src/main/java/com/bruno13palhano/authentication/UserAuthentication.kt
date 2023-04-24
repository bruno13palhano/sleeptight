package com.bruno13palhano.authentication

import android.graphics.Bitmap
import com.bruno13palhano.model.User

interface UserAuthentication {
    fun createUser(user: User, onSuccess: (userUid: String) -> Unit, onFail: () -> Unit)
    fun login(email: String, password: String, onSuccess: () -> Unit, onFail: () -> Unit)
    fun logout()
    fun isUserAuthenticated(): Boolean
    fun getCurrentUser(): User
    fun updateUserUrlPhoto(photo: Bitmap, onSuccess: (newPhotoUrl: String, userUid: String) -> Unit,
        onFail: () -> Unit)
    fun updateUsername(username: String, onSuccess: (newUsername: String, userUid: String) -> Unit,
        onFail: () -> Unit)
    fun updateUserAttributesInFirebaseFirestore(user: User, userUid: String, onSuccess: () -> Unit,
        onFail: () -> Unit)
}