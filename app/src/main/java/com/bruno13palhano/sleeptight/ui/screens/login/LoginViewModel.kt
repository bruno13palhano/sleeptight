package com.bruno13palhano.sleeptight.ui.screens.login

import androidx.compose.runtime.getValue
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
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    @DefaultUserFirebase private val authentication: UserAuthentication,
    @UserRep private val userRepository: UserDataContract<User>,
) : ViewModel() {
    private val _loginStatus = MutableStateFlow<LoginStatus>(LoginStatus.Default)
    val loginStatus = _loginStatus.asStateFlow()

    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set

    val isEmailAndPasswordNotEmpty: StateFlow<Boolean> = snapshotFlow {
        isValuesValid(email, password)
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

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
                },
            )
        }
    }

    private fun isValuesValid(email: String, password: String): Boolean =
        email.trim() != "" && password.trim() != ""

    private fun insertUserInDatabase(user: User) {
        viewModelScope.launch {
            userRepository.insert(user)
        }
    }

    private fun loading() {
        _loginStatus.value = LoginStatus.Loading
    }

    sealed class LoginStatus {
        object Loading : LoginStatus()
        object Error : LoginStatus()
        object Success : LoginStatus()
        object Default : LoginStatus()
    }
}
