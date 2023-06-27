package com.bruno13palhano.sleeptight.ui.screens.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.bruno13palhano.authentication.DefaultUserFirebase
import com.bruno13palhano.authentication.UserAuthentication
import com.bruno13palhano.core.data.di.DefaultUserRep
import com.bruno13palhano.core.data.repository.UserRepository
import com.bruno13palhano.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    @DefaultUserFirebase private val authentication: UserAuthentication,
    @DefaultUserRep private val userRepository: UserRepository
) : ViewModel() {
    private val _loginStatus = MutableStateFlow<LoginStatus>(LoginStatus.Default)
    val loginStatus = _loginStatus.asStateFlow()

    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set

    val isEmailAndPasswordNotEmpty = isValuesValid(email, password)

    fun updateEmail(email: String) {
        this.email = email
    }

    fun updatePassword(password: String) {
        this.password = password
    }

    fun login() {
        loading()
        if (isValuesValid(email, password)) {
            authentication.login(
                email = email,
                password = password,
                onSuccess = { user ->
                    insertUserInDatabase(user)
                    _loginStatus.value = LoginStatus.Success
                },
                onFail = {
                    _loginStatus.value = LoginStatus.Error
                }
            )
        }
    }

    private fun isValuesValid(email: String, password: String): Boolean =
        email.trim() != "" && password.trim() != ""

    private fun insertUserInDatabase(user: User) {
        userRepository.insertUser(user)
    }

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