package com.bruno13palhano.sleeptight.ui.screens.login

import android.icu.text.DecimalFormat
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Balance
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.SquareFoot
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import com.bruno13palhano.sleeptight.R
import java.util.Locale

@Composable
fun EmailField(
    email: String,
    modifier: Modifier,
    onEmailChange: (email: String) -> Unit,
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

@Composable
fun PasswordField(
    password: String,
    showPassword: Boolean,
    modifier: Modifier,
    onPasswordChange: (password: String) -> Unit,
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

@Composable
fun UsernameField(
    username: String,
    modifier: Modifier,
    onUsernameChange: (username: String) -> Unit,
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

@Composable
fun BabyNameField(
    babyName: String,
    isEnabled: Boolean = true,
    modifier: Modifier,
    onBabyNameChange: (babyName: String) -> Unit,
    onDone: () -> Unit
) {
    OutlinedTextField(
        modifier = modifier,
        value = babyName,
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Label,
                contentDescription = stringResource(id = R.string.baby_name_label)
            )
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {
            this.defaultKeyboardAction(ImeAction.Done)
            onDone()
        }),
        onValueChange = { emailValue ->
            onBabyNameChange(emailValue)
        },
        singleLine = true,
        enabled = isEnabled,
        label = { Text(text = stringResource(id = R.string.baby_name_label)) },
        placeholder = { Text(text = stringResource(id = R.string.insert_baby_name_label)) }
    )
}

@Composable
fun BirthplaceField(
    birthplace: String,
    isEnabled: Boolean = true,
    modifier: Modifier,
    onBirthplaceChange: (birthplace: String) -> Unit,
    onDone: () -> Unit
) {
    OutlinedTextField(
        modifier = modifier,
        value = birthplace,
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.LocationOn,
                contentDescription = stringResource(id = R.string.birthplace_label)
            )
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {
            this.defaultKeyboardAction(ImeAction.Done)
            onDone()
        }),
        onValueChange = { emailValue ->
            onBirthplaceChange(emailValue)
        },
        singleLine = true,
        enabled = isEnabled,
        label = { Text(text = stringResource(id = R.string.birthplace_label)) },
        placeholder = { Text(text = stringResource(id = R.string.insert_birthplace_label)) }
    )
}

@Composable
fun DateField(
    date: String,
    modifier: Modifier,
    isEnabled: Boolean = true
) {
    OutlinedTextField(
        modifier = modifier,
        value = date,
        readOnly = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.CalendarMonth,
                contentDescription = stringResource(id = R.string.birth_date_label)
            )
        },
        onValueChange = {},
        singleLine = true,
        enabled = isEnabled,
        label = {
            Text(
                text = stringResource(id = R.string.birth_date_label),
                overflow = TextOverflow.Ellipsis
            )
        },
    )
}

@Composable
fun TimeField(
    time: String,
    modifier: Modifier,
    isEnabled: Boolean = true
) {
    OutlinedTextField(
        modifier = modifier,
        value = time,
        readOnly = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Timer,
                contentDescription = stringResource(id = R.string.birth_time_label)
            )
        },
        onValueChange = {},
        singleLine = true,
        enabled = isEnabled,
        label = { Text(text = stringResource(id = R.string.birth_time_label)) },
    )
}

@Composable
fun HeightField(
    height: String,
    isEnabled: Boolean = true,
    modifier: Modifier,
    onHeightChange: (height: String) -> Unit,
    onDone: () -> Unit
) {
    val decimalFormat = DecimalFormat.getInstance(Locale.getDefault()) as DecimalFormat
    val decimalSeparator = decimalFormat.decimalFormatSymbols.decimalSeparator
    val pattern = remember { Regex("^\\d*\\$decimalSeparator?\\d*\$") }

    OutlinedTextField(
        modifier = modifier,
        value = height,
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.SquareFoot,
                contentDescription = stringResource(id = R.string.birth_height_label)
            )
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Decimal
        ),
        keyboardActions = KeyboardActions(onDone = {
            this.defaultKeyboardAction(ImeAction.Done)
            onDone()
        }),
        onValueChange = { heightValue ->
            if (heightValue.isEmpty() || heightValue.matches(pattern)) {
                onHeightChange(heightValue)
            }
        },
        singleLine = true,
        enabled = isEnabled,
        label = { Text(text = stringResource(id = R.string.birth_height_label)) },
        placeholder = { Text(text = stringResource(id = R.string.insert_height_label)) }
    )
}

@Composable
fun WeightField(
    weight: String,
    isEnabled: Boolean = true,
    modifier: Modifier,
    onWeightChange: (weight: String) -> Unit,
    onDone: () -> Unit
) {
    val decimalFormat = DecimalFormat.getInstance(Locale.getDefault()) as DecimalFormat
    val decimalSeparator = decimalFormat.decimalFormatSymbols.decimalSeparator
    val pattern = remember { Regex("^\\d*\\$decimalSeparator?\\d*\$") }

    OutlinedTextField(
        modifier = modifier,
        value = weight,
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Balance,
                contentDescription = stringResource(id = R.string.birth_weight_label)
            )
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Decimal
        ),
        keyboardActions = KeyboardActions(onDone = {
            this.defaultKeyboardAction(ImeAction.Done)
            onDone()
        }),
        onValueChange = { weightValue ->
            if (weightValue.isEmpty() || weightValue.matches(pattern)) {
                onWeightChange(weightValue)
            }
        },
        singleLine = true,
        enabled = isEnabled,
        label = { Text(text = stringResource(id = R.string.birth_weight_label)) },
        placeholder = { Text(text = stringResource(id = R.string.insert_weight_label)) }
    )
}
