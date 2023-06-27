package com.bruno13palhano.sleeptight.ui.screens.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bruno13palhano.sleeptight.R

@Composable
fun CreateAccountScreen(
    onNextButtonClick: () -> Unit,
    onNavigationIconClick: () -> Unit,
    createAccountViewModel: CreateAccountViewModel
) {
    val focusManager = LocalFocusManager.current
    var showPassword by remember { mutableStateOf(false) }

    CreateAccountContent(
        username = createAccountViewModel.username,
        email = createAccountViewModel.email,
        password = createAccountViewModel.password,
        showPassword = showPassword,
        onUsernameChange = createAccountViewModel::updateUsername,
        onEmailChange = createAccountViewModel::updateEmail,
        onPasswordChange = createAccountViewModel::updatePassword,
        onShowPasswordChange = { showPasswordValue -> showPassword = showPasswordValue },
        onUsernameDone = { focusManager.moveFocus(FocusDirection.Next) },
        onEmailDone = { focusManager.moveFocus(FocusDirection.Next) },
        onPasswordDone = { focusManager.clearFocus(force = true) },
        onNavigationIconClick = onNavigationIconClick,
        onNextButtonClick = onNextButtonClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAccountContent(
    username: String,
    email: String,
    password: String,
    showPassword: Boolean,
    onUsernameChange: (username: String) -> Unit,
    onEmailChange: (email: String) -> Unit,
    onPasswordChange: (password: String) -> Unit,
    onShowPasswordChange: (showPassword: Boolean) -> Unit,
    onUsernameDone: () -> Unit,
    onEmailDone: () -> Unit,
    onPasswordDone: () -> Unit,
    onNavigationIconClick: () -> Unit,
    onNextButtonClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.create_account_label)) },
                navigationIcon = {
                    IconButton(onClick = onNavigationIconClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.up_button_label)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNextButtonClick) {
                Icon(
                    imageVector = Icons.Filled.NavigateNext,
                    contentDescription = stringResource(id = R.string.next_label)
                )
            }
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            UsernameField(
                username = username,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp),
                onUsernameChange = onUsernameChange,
                onDone = onUsernameDone
            )

            EmailField(
                email = email,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp),
                onEmailChange = onEmailChange,
                onDone = onEmailDone
            )

            PasswordField(
                password = password,
                showPassword = showPassword,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp),
                onPasswordChange = onPasswordChange,
                showPasswordCallback = { showPasswordValue -> onShowPasswordChange(showPasswordValue) },
                onDone = onPasswordDone
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateAccountScreenPreview() {
    CreateAccountContent(
        username = "",
        email = "",
        password = "",
        showPassword = false,
        onUsernameChange = {},
        onEmailChange = {},
        onPasswordChange = {},
        onShowPasswordChange = {},
        onUsernameDone = {},
        onEmailDone = {},
        onPasswordDone = {},
        onNavigationIconClick = {},
        onNextButtonClick = {}
    )
}