package com.bruno13palhano.authentication

import com.bruno13palhano.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class UserFirebase @Inject constructor(
    private val auth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
) : UserAuthentication {

    override fun createUser(user: User, onSuccess: (userUid: String) -> Unit, onFail: () -> Unit) {
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

    override fun login(
        email: String,
        password: String,
        onSuccess: (user: User) -> Unit,
        onFail: () -> Unit,
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    getCurrentUser(
                        onSuccess = { user ->
                            onSuccess(user)
                        },
                        onFail = {
                        },
                    )
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
            babyUrlPhoto = auth.currentUser?.photoUrl.toString(),
        )
    }

    private fun getCurrentUser(onSuccess: (user: User) -> Unit, onFail: () -> Unit) {
        val id = auth.currentUser?.uid ?: ""
        val username = auth.currentUser?.displayName ?: ""
        val email = auth.currentUser?.email ?: ""
        val babyUrlPhoto = auth.currentUser?.photoUrl.toString()

        val userRef = firebaseFirestore.collection("users").document(id)
        userRef.get()
            .addOnSuccessListener {
                val currentUser = User(
                    id = id,
                    username = username,
                    email = email,
                    babyName = it["babyName"].toString(),
                    babyUrlPhoto = babyUrlPhoto,
                    birthplace = it["birthplace"].toString(),
                    birthdate = it["birthdate"] as Long,
                    birthtime = it["birthtime"] as Long,
                    height = (it["height"] as Double).toFloat(),
                    weight = (it["weight"] as Double).toFloat(),
                )
                onSuccess(currentUser)
            }
            .addOnFailureListener {
                onFail()
            }
    }

    override fun updateUserUrlPhoto(
        photo: ByteArray,
        onSuccess: (newPhotoUrl: String, userUid: String) -> Unit,
        onFail: () -> Unit,
    ) {
        val storageRef = storage.reference

        auth.currentUser?.let {
            val profilePhotoRef = storageRef.child("${it.email}/profile_image.jpg")
            val uploadTask = profilePhotoRef.putBytes(photo)
            uploadTask
                .addOnSuccessListener { _ ->
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
        onFail: () -> Unit,
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
                        },
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
            "weight" to user.weight,
        )
        firebaseFirestore.collection("users")
            .document(userUid)
            .set(firebaseUser)
    }

    private fun updateUsernameInFirebaseFirestore(
        newUsername: String,
        userUid: String,
        callback: FirebaseFirestoreCallback,
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

    override fun updateUserBabyNameInFirebaseFirestore(
        babyName: String,
        userUid: String,
        onSuccess: () -> Unit,
        onFail: () -> Unit,
    ) {
        val userRef = firebaseFirestore.collection("users").document(userUid)
        userRef.update("babyName", babyName)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFail()
            }
    }

    override fun updateUserBirthplaceInFirebaseFirestore(
        birthplace: String,
        userUid: String,
        onSuccess: () -> Unit,
        onFail: () -> Unit,
    ) {
        val userRef = firebaseFirestore.collection("users").document(userUid)
        userRef.update("birthplace", birthplace)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFail()
            }
    }

    override fun updateUserBirthdateInFirebaseFirestore(
        birthdate: Long,
        userUid: String,
        onSuccess: () -> Unit,
        onFail: () -> Unit,
    ) {
        val userRef = firebaseFirestore.collection("users").document(userUid)
        userRef.update("birthdate", birthdate)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFail()
            }
    }

    override fun updateUserBirthtimeInFirebaseFirestore(
        birthtime: Long,
        userUid: String,
        onSuccess: () -> Unit,
        onFail: () -> Unit,
    ) {
        val userRef = firebaseFirestore.collection("users").document(userUid)
        userRef.update("birthtime", birthtime)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFail()
            }
    }

    override fun updateUserHeightInFirebaseFirestore(
        height: Float,
        userUid: String,
        onSuccess: () -> Unit,
        onFail: () -> Unit,
    ) {
        val userRef = firebaseFirestore.collection("users").document(userUid)
        userRef.update("height", height)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFail()
            }
    }

    override fun updateUserWeightInFirebaseFirestore(
        weight: Float,
        userUid: String,
        onSuccess: () -> Unit,
        onFail: () -> Unit,
    ) {
        val userRef = firebaseFirestore.collection("users").document(userUid)
        userRef.update("weight", weight)
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
