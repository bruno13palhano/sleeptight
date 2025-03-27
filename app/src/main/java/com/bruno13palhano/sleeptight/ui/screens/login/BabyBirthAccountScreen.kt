package com.bruno13palhano.sleeptight.ui.screens.login

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.ui.screens.CircularProgress
import com.bruno13palhano.sleeptight.ui.screens.TimePickerDialog
import com.bruno13palhano.sleeptight.ui.screens.clearFocusOnKeyboardDismiss
import com.bruno13palhano.sleeptight.ui.theme.SleepTightTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BabyBirthAccountScreen(
    onCreateAccountSuccess: () -> Unit,
    onNavigationIconClick: () -> Unit,
    createAccountViewModel: CreateAccountViewModel,
) {
    val configuration = LocalConfiguration.current
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val loginStatus by createAccountViewModel.loginStatus.collectAsStateWithLifecycle()

    var showDatePickerDialog by remember { mutableStateOf(false) }
    var datePickerState = rememberDatePickerState()

    var showBirthtimePickerDialog by remember { mutableStateOf(false) }
    var birthtimePickerState = rememberTimePickerState()

    if (showDatePickerDialog) {
        DatePickerDialog(
            onDismissRequest = {
                showDatePickerDialog = false
                focusManager.clearFocus(force = true)
            },
            confirmButton = {
                Button(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            createAccountViewModel.updateDate(it)
                        }
                        showDatePickerDialog = false
                        focusManager.clearFocus(force = true)
                    },
                ) {
                    Text(text = stringResource(id = R.string.done_label))
                }
            },
        ) {
            datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = createAccountViewModel.birthdateInMillis,
                initialDisplayMode =
                if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    DisplayMode.Picker
                } else {
                    DisplayMode.Input
                },
            )
            DatePicker(
                state = datePickerState,
                showModeToggle = configuration.orientation == Configuration.ORIENTATION_PORTRAIT,
            )
        }
    }

    if (showBirthtimePickerDialog) {
        TimePickerDialog(
            title = stringResource(id = R.string.birth_time_label),
            onCancelButton = {
                showBirthtimePickerDialog = false
                focusManager.clearFocus(force = true)
            },
            onConfirmButton = {
                birthtimePickerState.let {
                    createAccountViewModel.updateTime(it.hour, it.minute)
                }
                showBirthtimePickerDialog = false
                focusManager.clearFocus(force = true)
            },
        ) {
            birthtimePickerState = rememberTimePickerState(
                initialHour = createAccountViewModel.birthtimeHour,
                initialMinute = createAccountViewModel.birthtimeMinute,
                is24Hour = true,
            )
            if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                TimePicker(state = birthtimePickerState)
            } else {
                TimeInput(state = birthtimePickerState)
            }
        }
    }

    when (loginStatus) {
        CreateAccountViewModel.LoginStatus.Default -> {
            BabyBirthAccountContent(
                birthdate = createAccountViewModel.birthdate,
                birthtime = createAccountViewModel.birthtime,
                height = createAccountViewModel.height,
                weight = createAccountViewModel.weight,
                showButton = createAccountViewModel.isHeightAndWeightNotEmpty(),
                configuration = configuration,
                onHeightChange = createAccountViewModel::updateHeight,
                onWeightChange = createAccountViewModel::updateWeight,
                onBirthdateDone = { showDatePickerDialog = true },
                onBirthtimeDone = { showBirthtimePickerDialog = true },
                onHeightDone = { focusManager.clearFocus(force = true) },
                onWeightDone = { focusManager.clearFocus(force = true) },
                onOutsideClick = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                },
                onNavigationIconClick = onNavigationIconClick,
                createUser = createAccountViewModel::insertUser,
            )
        }
        CreateAccountViewModel.LoginStatus.Loading -> {
            CircularProgress()
        }
        CreateAccountViewModel.LoginStatus.Success -> {
            LaunchedEffect(key1 = Unit) {
                onCreateAccountSuccess()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BabyBirthAccountContent(
    birthdate: String,
    birthtime: String,
    height: String,
    weight: String,
    showButton: Boolean,
    configuration: Configuration,
    onHeightChange: (height: String) -> Unit,
    onWeightChange: (weight: String) -> Unit,
    onBirthdateDone: () -> Unit,
    onBirthtimeDone: () -> Unit,
    onHeightDone: () -> Unit,
    onWeightDone: () -> Unit,
    onOutsideClick: () -> Unit,
    onNavigationIconClick: () -> Unit,
    createUser: () -> Unit,
) {
    Scaffold(
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) { onOutsideClick() },
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.birth_information_label)) },
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
            if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                if (showButton) {
                    FloatingActionButton(onClick = createUser) {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = stringResource(id = R.string.done_label),
                        )
                    }
                }
            }
        },
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .verticalScroll(rememberScrollState()),
        ) {
            DateField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp)
                    .onFocusChanged { focusState ->
                        if (focusState.hasFocus) {
                            onBirthdateDone()
                        }
                    },
                date = birthdate,
            )

            TimeField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp)
                    .onFocusChanged { focusState ->
                        if (focusState.hasFocus) {
                            onBirthtimeDone()
                        }
                    },
                time = birthtime,
            )

            HeightField(
                height = height,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp)
                    .clearFocusOnKeyboardDismiss(),
                onHeightChange = { heightValue -> onHeightChange(heightValue) },
                onDone = onHeightDone,
            )

            WeightField(
                weight = weight,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp)
                    .clearFocusOnKeyboardDismiss(),
                onWeightChange = { weightValue -> onWeightChange(weightValue) },
                onDone = onWeightDone,
            )

            if (configuration.orientation != Configuration.ORIENTATION_PORTRAIT) {
                FloatingActionButton(
                    onClick = {
                        createUser()
                    },
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.End),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = stringResource(id = R.string.done_label),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BabyBirthAccountScreenPreview() {
    SleepTightTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            BabyBirthAccountContent(
                birthdate = "",
                birthtime = "",
                height = "",
                weight = "",
                showButton = false,
                configuration = LocalConfiguration.current,
                onHeightChange = {},
                onWeightChange = {},
                onBirthdateDone = {},
                onBirthtimeDone = {},
                onHeightDone = {},
                onWeightDone = {},
                onOutsideClick = {},
                onNavigationIconClick = {},
                createUser = {},
            )
        }
    }
}
