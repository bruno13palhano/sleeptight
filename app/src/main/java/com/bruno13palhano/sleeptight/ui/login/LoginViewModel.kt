package com.bruno13palhano.sleeptight.ui.login

import androidx.lifecycle.ViewModel
import com.bruno13palhano.authentication.DefaultUserFirebase
import com.bruno13palhano.authentication.UserAuthentication
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    @DefaultUserFirebase private val authentication: UserAuthentication
) : ViewModel() {

    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFail: () -> Unit
    ) {
        if (isValuesValid(email, password)) {
            authentication.login(
                email = email,
                password = password,
                onSuccess = {
                    onSuccess()
                },
                onFail = {
                    onFail()
                }
            )
        }
    }

    private fun isValuesValid(email: String, password: String): Boolean =
        email != "" && password != ""
}