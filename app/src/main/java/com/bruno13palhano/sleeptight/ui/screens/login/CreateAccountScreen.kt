package com.bruno13palhano.sleeptight.ui.screens.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.ui.screens.clearFocusOnKeyboardDismiss
import com.bruno13palhano.sleeptight.ui.theme.SleepTightTheme

@Composable
fun CreateAccountScreen(
    onNextButtonClick: () -> Unit,
    onNavigationIconClick: () -> Unit,
    createAccountViewModel: CreateAccountViewModel,
) {
    val focusManager = LocalFocusManager.current
    var showPassword by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val showButton by createAccountViewModel.isUserBasicDataNotEmpty.collectAsStateWithLifecycle()

    CreateAccountContent(
        showButton = showButton,
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
        onOutsideClick = {
            keyboardController?.hide()
            focusManager.clearFocus()
        },
        onNavigationIconClick = onNavigationIconClick,
        onNextButtonClick = onNextButtonClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAccountContent(
    showButton: Boolean,
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
    onOutsideClick: () -> Unit,
    onNavigationIconClick: () -> Unit,
    onNextButtonClick: () -> Unit,
) {
    Scaffold(
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) { onOutsideClick() },
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.create_account_label)) },
                navigationIcon = {
                    IconButton(onClick = onNavigationIconClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.up_button_label),
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            if (showButton) {
                FloatingActionButton(onClick = onNextButtonClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.NavigateNext,
                        contentDescription = stringResource(id = R.string.next_label),
                    )
                }
            }
        },
    ) {
        Column(modifier = Modifier.padding(it)) {
            UsernameField(
                username = username,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp)
                    .clearFocusOnKeyboardDismiss(),
                onUsernameChange = onUsernameChange,
                onDone = onUsernameDone,
            )

            EmailField(
                email = email,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp)
                    .clearFocusOnKeyboardDismiss(),
                onEmailChange = onEmailChange,
                onDone = onEmailDone,
            )

            PasswordField(
                password = password,
                showPassword = showPassword,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp)
                    .clearFocusOnKeyboardDismiss(),
                onPasswordChange = onPasswordChange,
                showPasswordCallback = { showPasswordValue ->
                    onShowPasswordChange(
                        showPasswordValue,
                    )
                },
                onDone = onPasswordDone,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateAccountScreenPreview() {
    SleepTightTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            CreateAccountContent(
                showButton = false,
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
                onOutsideClick = {},
                onNavigationIconClick = {},
                onNextButtonClick = {},
            )
        }
    }
}
