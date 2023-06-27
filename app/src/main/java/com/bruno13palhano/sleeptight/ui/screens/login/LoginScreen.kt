package com.bruno13palhano.sleeptight.ui.screens.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.ui.screens.CircularProgress

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onCreateAccountButtonClick: () -> Unit,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val focusManager = LocalFocusManager.current
    var showPassword by remember { mutableStateOf(false) }

    val loginStatus by loginViewModel.loginStatus.collectAsStateWithLifecycle()

    when(loginStatus) {
        LoginViewModel.LoginStatus.Default -> {
            LoginContent(
                onCreateAccountButtonClick = onCreateAccountButtonClick,
                email =  loginViewModel.email,
                password = loginViewModel.password,
                showPassword = showPassword,
                onEmailChange = { emailValue -> loginViewModel.updateEmail(emailValue) },
                onPasswordChange = { passwordValue -> loginViewModel.updatePassword(passwordValue) },
                onShowPasswordChange = { showPasswordValue -> showPassword = showPasswordValue },
                onEmailDone = { focusManager.moveFocus(FocusDirection.Next) },
                onPasswordDone = { focusManager.clearFocus(force = true) },
                login = { loginViewModel.login() }
            )
        }
        LoginViewModel.LoginStatus.Success -> {
            onLoginSuccess()
        }
        LoginViewModel.LoginStatus.Loading -> {
            CircularProgress()
        }
        LoginViewModel.LoginStatus.Error -> {
            LoginContent(
                onCreateAccountButtonClick = onCreateAccountButtonClick,
                email =  loginViewModel.email,
                password = loginViewModel.password,
                showPassword = showPassword,
                onEmailChange = { emailValue -> loginViewModel.updateEmail(emailValue) },
                onPasswordChange = { passwordValue -> loginViewModel.updatePassword(passwordValue) },
                onShowPasswordChange = { showPasswordValue -> showPassword = showPasswordValue },
                onEmailDone = { focusManager.moveFocus(FocusDirection.Next) },
                onPasswordDone = { focusManager.clearFocus(force = true) },
                login = { loginViewModel.login() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginContent(
    onCreateAccountButtonClick: () -> Unit,
    email: String,
    password: String,
    showPassword: Boolean,
    onEmailChange: (email: String) -> Unit,
    onPasswordChange: (password: String) -> Unit,
    onShowPasswordChange: (showPassword: Boolean) -> Unit,
    onEmailDone: () -> Unit,
    onPasswordDone: () -> Unit,
    login: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.login_label)) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = login) {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = stringResource(id = R.string.confirm_login_label)
                )
            }
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            EmailField(
                email = email,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp),
                onEmailChange = { emailValue -> onEmailChange(emailValue) },
                onDone = onEmailDone
            )

            PasswordField(
                password = password,
                showPassword = showPassword,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp),
                onPasswordChange = { passwordValue -> onPasswordChange(passwordValue) },
                showPasswordCallback = { showPasswordValue ->
                    onShowPasswordChange(showPasswordValue)
                },
                onDone = onPasswordDone
            )

            TextButton(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 8.dp),
                onClick = onCreateAccountButtonClick
            ) {
                Text(text = stringResource(id = R.string.create_account_label))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginContent(
        onCreateAccountButtonClick = {},
        email = "",
        password = "",
        showPassword = false,
        onEmailChange = {},
        onPasswordChange = {},
        onShowPasswordChange = {},
        onEmailDone = {},
        onPasswordDone = {},
        login = {}
    )
}