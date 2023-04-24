package com.bruno13palhano.authentication

import android.graphics.Bitmap
import com.bruno13palhano.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class UserFirebase @Inject constructor(
    private val auth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : UserAuthentication {

    override fun createUser(
        user: User,
        onSuccess: (userUid: String) -> Unit,
        onFail: () -> Unit
    ) {
        auth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    auth.currentUser?.uid?.let {
                        onSuccessfulCreateUser(user, it)
                        onSuccess(it)
                    }
                } else {
                    onFail()
                }
            }
    }

    override fun login(email: String, password: String, onSuccess: () -> Unit, onFail: () -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onFail()
                }
            }
    }

    override fun logout() {
        auth.signOut()
    }

    override fun isUserAuthenticated(): Boolean {
        return auth.currentUser != null
    }

    override fun getCurrentUser(): User {
        return User(
            id = auth.currentUser?.uid ?: "",
            username = auth.currentUser?.displayName ?: "",
            email = auth.currentUser?.email ?: "",
            babyUrlPhoto = auth.currentUser?.photoUrl.toString()
        )
    }

    override fun updateUserUrlPhoto(
        photo: Bitmap,
        onSuccess: (newPhotoUrl: String, userUid: String) -> Unit,
        onFail: () -> Unit
    ) {
        val storageRef = storage.reference

        auth.currentUser?.let {
            val profilePhotoRef = storageRef.child("${it.email}/profile_image.jpg")
            val baos = ByteArrayOutputStream()
            photo.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            val uploadTask = profilePhotoRef.putBytes(data)
            uploadTask
                .addOnSuccessListener { taskSnapshot ->
                    profilePhotoRef.downloadUrl.addOnSuccessListener { uri ->
                        val profileUpdates = userProfileChangeRequest {
                            photoUri = uri
                        }
                        it.updateProfile(profileUpdates).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                onSuccess(uri.toString(), it.uid)
                            } else {
                                onFail()
                            }
                        }
                    }
                }
                .addOnFailureListener {
                    onFail()
                }
        }
    }

    override fun updateUsername(
        username: String,
        onSuccess: (newUsername: String, userUid: String) -> Unit,
        onFail: () -> Unit
    ) {
        val profileUpdates = userProfileChangeRequest {
            displayName = username
        }
        auth.currentUser?.let {
            it.updateProfile(profileUpdates).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    updateUsernameInFirebaseFirestore(
                        newUsername = username,
                        userUid = it.uid,
                        callback = object : FirebaseFirestoreCallback {
                            override fun onSuccess() {
                                onSuccess(username, it.uid)
                            }

                            override fun onFail() {
                                onFail()
                            }
                        }
                    )
                } else {
                    onFail()
                }
            }
        }
    }

    private fun onSuccessfulCreateUser(user: User, userUid: String) {
        addUserInFirebaseFirestore(user, userUid)
        updateProfile(user.username)
    }

    private fun updateProfile(username: String) {
        val profileUpdates = userProfileChangeRequest {
            displayName = username
        }
        val user = auth.currentUser
        user!!.updateProfile(profileUpdates)
    }

    private fun addUserInFirebaseFirestore(user: User, userUid: String) {
        val firebaseUser = hashMapOf(
            "username" to user.username,
            "babyName" to user.babyName,
            "birthplace" to user.birthplace,
            "birthdate" to user.birthdate,
            "birthtime" to user.birthtime,
            "height" to user.height,
            "weight" to user.weight
        )
        firebaseFirestore.collection("users")
            .document(userUid)
            .set(firebaseUser)
    }

    private fun updateUsernameInFirebaseFirestore(
        newUsername: String,
        userUid: String,
        callback: FirebaseFirestoreCallback
    ) {
        val usernameRef = firebaseFirestore.collection("users").document(userUid)
        usernameRef.update("username", newUsername)
            .addOnSuccessListener {
                callback.onSuccess()
            }
            .addOnFailureListener {
                callback.onFail()
            }
    }

    override fun updateUserAttributesInFirebaseFirestore(
        user: User,
        userUid: String,
        onSuccess: () -> Unit,
        onFail: () -> Unit
    ) {
        val userRef = firebaseFirestore.collection("users").document(userUid)
        userRef.update(
            "birthplace", user.birthplace,
            "birthdate", user.birthdate,
            "birthTime", user.birthtime,
            "height", user.height,
            "weight", user.weight
        )
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFail()
            }
    }

    interface FirebaseFirestoreCallback {
        fun onSuccess()
        fun onFail()
    }
}