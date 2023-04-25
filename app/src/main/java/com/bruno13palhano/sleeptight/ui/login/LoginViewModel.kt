package com.bruno13palhano.sleeptight.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.authentication.DefaultUserFirebase
import com.bruno13palhano.authentication.UserAuthentication
import com.bruno13palhano.core.data.di.DefaultUserRep
import com.bruno13palhano.core.data.repository.UserRepository
import com.bruno13palhano.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    @DefaultUserFirebase private val authentication: UserAuthentication,
    @DefaultUserRep private val userRepository: UserRepository
) : ViewModel() {
    private val _loginStatus = MutableStateFlow<LoginStatus>(LoginStatus.Default)
    val loginStatus = _loginStatus.asStateFlow()

    fun login(
        email: String,
        password: String
    ) {
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
        email != "" && password != ""

    private fun insertUserInDatabase(user: User) {
        viewModelScope.launch {
            userRepository.insertUser(user)
        }
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