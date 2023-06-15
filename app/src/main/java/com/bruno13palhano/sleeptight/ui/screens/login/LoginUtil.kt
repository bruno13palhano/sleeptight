package com.bruno13palhano.sleeptight.ui.screens.login

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import com.bruno13palhano.sleeptight.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailField(
    email: TextFieldValue,
    modifier: Modifier,
    onEmailChange: (email: TextFieldValue) -> Unit,
    onDone: () -> Unit
) {
    OutlinedTextField(
        modifier = modifier,
        value = email,
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Email,
                contentDescription = stringResource(id = R.string.email_label)
            )
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {
            this.defaultKeyboardAction(ImeAction.Done)
            onDone()
        }),
        onValueChange = { emailValue ->
            onEmailChange(emailValue)
        },
        singleLine = true,
        label = { Text(text = stringResource(id = R.string.email_label)) },
        placeholder = { Text(text = stringResource(id = R.string.insert_email_label)) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordField(
    password: TextFieldValue,
    showPassword: Boolean,
    modifier: Modifier,
    onPasswordChange: (password: TextFieldValue) -> Unit,
    showPasswordCallback: (showPassword: Boolean) -> Unit,
    onDone: () -> Unit
) {
    OutlinedTextField(
        modifier = modifier,
        value = password,
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Key,
                contentDescription = stringResource(id = R.string.password_label)
            )
        },
        trailingIcon = {
            if (showPassword) {
                IconButton(onClick = { showPasswordCallback(false) }) {
                    Icon(
                        imageVector = Icons.Filled.Visibility,
                        contentDescription = stringResource(id = R.string.hide_password_label)
                    )
                }
            } else {
                IconButton(onClick = { showPasswordCallback(true) }) {
                    Icon(
                        imageVector = Icons.Filled.VisibilityOff,
                        contentDescription = stringResource(id = R.string.show_password_label)
                    )
                }
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = {
            this.defaultKeyboardAction(ImeAction.Done)
            onDone()
        }),
        onValueChange = { passwordValue ->
            onPasswordChange(passwordValue)
        },
        visualTransformation = if (showPassword) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        singleLine = true,
        label = { Text(text = stringResource(id = R.string.password_label)) },
        placeholder = { Text(text = stringResource(id = R.string.insert_password_label)) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsernameField(
    username: TextFieldValue,
    modifier: Modifier,
    onUsernameChange: (username: TextFieldValue) -> Unit,
    onDone: () -> Unit
) {
    OutlinedTextField(
        modifier = modifier,
        value = username,
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Label,
                contentDescription = stringResource(id = R.string.username_label)
            )
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {
            this.defaultKeyboardAction(ImeAction.Done)
            onDone()
        }),
        onValueChange = { usernameValue ->
            onUsernameChange(usernameValue)
        },
        singleLine = true,
        label = { Text(text = stringResource(id = R.string.username_label)) },
        placeholder = { Text(text = stringResource(id = R.string.insert_username_label)) }
    )
}