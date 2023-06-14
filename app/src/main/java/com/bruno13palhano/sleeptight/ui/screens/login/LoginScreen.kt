package com.bruno13palhano.sleeptight.ui.screens.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bruno13palhano.sleeptight.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onCreateAccountButtonClick: () -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onLoginSuccess) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_done_24),
                    contentDescription = stringResource(id = R.string.confirm_login_label)
                )
            }
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            var email by remember { mutableStateOf(TextFieldValue("")) }
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp, start = 16.dp, end = 16.dp),
                value = email,
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_email_24),
                        contentDescription = stringResource(id = R.string.email_label)
                    )
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    this.defaultKeyboardAction(ImeAction.Done)
                }),
                onValueChange = { emailValue ->
                    email = emailValue
                },
                label = { Text(text = stringResource(id = R.string.email_label)) },
                placeholder = { Text(text = stringResource(id = R.string.insert_email_label)) }
            )

            var password by remember { mutableStateOf(TextFieldValue("")) }
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp),
                value = password,
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_key_24),
                        contentDescription = stringResource(id = R.string.password_label)
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                keyboardActions = KeyboardActions(onDone = {
                    this.defaultKeyboardAction(ImeAction.Done)
                }),
                onValueChange = { passwordValue ->
                    password = passwordValue
                },
                label = { Text(text = stringResource(id = R.string.password_label)) },
                placeholder = { Text(text = stringResource(id = R.string.insert_password_label)) }
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
        floatingActionButton = {
            FloatingActionButton(onClick = { println("Login button was clicked!") }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_done_24),
                    contentDescription = stringResource(id = R.string.confirm_login_label)
                )
            }
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            var email by remember { mutableStateOf(TextFieldValue("")) }
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp, start = 16.dp, end = 16.dp),
                value = email,
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_email_24),
                        contentDescription = stringResource(id = R.string.email_label)
                    )
                },
                onValueChange = { emailValue ->
                    email = emailValue
                },
                label = { Text(text = stringResource(id = R.string.email_label)) },
                placeholder = { Text(text = stringResource(id = R.string.insert_email_label)) }
            )

            var password by remember { mutableStateOf(TextFieldValue("")) }
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp),
                value = password,
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_key_24),
                        contentDescription = stringResource(id = R.string.password_label)
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                onValueChange = { passwordValue ->
                    password = passwordValue
                },
                label = { Text(text = stringResource(id = R.string.password_label)) },
                placeholder = { Text(text = stringResource(id = R.string.insert_password_label)) }
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