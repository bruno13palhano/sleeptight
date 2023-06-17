package com.bruno13palhano.sleeptight.ui.screens.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bruno13palhano.sleeptight.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onCreateAccountButtonClick: () -> Unit,
    onNavigationIconClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.login_label)) },
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
            FloatingActionButton(onClick = onLoginSuccess) {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = stringResource(id = R.string.confirm_login_label)
                )
            }
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            val focusManager = LocalFocusManager.current
            var email by remember { mutableStateOf(TextFieldValue("")) }
            EmailField(
                email = email,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp),
                onEmailChange = { emailValue -> email = emailValue },
                onDone = { focusManager.moveFocus(FocusDirection.Next) }
            )

            var password by remember { mutableStateOf(TextFieldValue("")) }
            var showPassword by remember { mutableStateOf(false) }
            PasswordField(
                password = password,
                showPassword = showPassword,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp),
                onPasswordChange = { passwordValue -> password = passwordValue },
                showPasswordCallback = { showPasswordValue -> showPassword = showPasswordValue },
                onDone = { focusManager.clearFocus() }
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.login_label)) },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.up_button_label)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = stringResource(id = R.string.confirm_login_label)
                )
            }
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            val email by remember { mutableStateOf(TextFieldValue("")) }
            EmailField(
                email = email,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp),
                onEmailChange = {},
                onDone = {}
            )

            val password by remember { mutableStateOf(TextFieldValue("")) }
            val showPassword by remember { mutableStateOf(false) }
            PasswordField(
                password = password,
                showPassword = showPassword,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp),
                onPasswordChange = {},
                showPasswordCallback = {},
                onDone = {}
            )

            TextButton(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 8.dp),
                onClick = {}
            ) {
                Text(text = stringResource(id = R.string.create_account_label))
            }
        }
    }
}