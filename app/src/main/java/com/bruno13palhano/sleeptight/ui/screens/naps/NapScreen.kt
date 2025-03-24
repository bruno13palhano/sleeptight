package com.bruno13palhano.sleeptight.ui.screens.naps

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.TimerOff
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.ui.screens.CommonMenu
import com.bruno13palhano.sleeptight.ui.screens.CommonMenuItemIndex
import com.bruno13palhano.sleeptight.ui.screens.TimePickerDialog
import com.bruno13palhano.sleeptight.ui.screens.clearFocusOnKeyboardDismiss
import com.bruno13palhano.sleeptight.ui.theme.SleepTightTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun NapScreen(napId: Long, navigateUp: () -> Unit, napViewModel: NapViewModel = hiltViewModel()) {
    LaunchedEffect(key1 = Unit) {
        napViewModel.getNap(napId)
    }

    val configuration = LocalConfiguration.current
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var showDatePickerDialog by remember { mutableStateOf(false) }
    var datePickerState = rememberDatePickerState()
    var showStartTimePicker by remember { mutableStateOf(false) }
    var startTimePickerState = rememberTimePickerState()
    var showEndTimePicker by remember { mutableStateOf(false) }
    var endTimePickerState = rememberTimePickerState()

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
                            napViewModel.updateNapDate(it)
                        }
                        showDatePickerDialog = false
                        focusManager.clearFocus(force = true)
                    },
                ) {
                    Text(text = stringResource(id = R.string.date_label))
                }
            },
        ) {
            datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = napViewModel.dateInMillis,
                initialDisplayMode = if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
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

    if (showStartTimePicker) {
        TimePickerDialog(
            title = stringResource(id = R.string.start_time_label),
            onCancelButton = {
                showStartTimePicker = false
                focusManager.clearFocus(force = true)
            },
            onConfirmButton = {
                startTimePickerState.let {
                    napViewModel.updateStartTime(it.hour, it.minute)
                }
                showStartTimePicker = false
                focusManager.clearFocus(force = true)
            },
        ) {
            startTimePickerState = rememberTimePickerState(
                initialHour = napViewModel.startTimeHour,
                initialMinute = napViewModel.startTimeMinute,
                is24Hour = true,
            )

            if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                TimePicker(state = startTimePickerState)
            } else {
                TimeInput(state = startTimePickerState)
            }
        }
    }

    if (showEndTimePicker) {
        TimePickerDialog(
            title = stringResource(id = R.string.end_time_label),
            onCancelButton = {
                showEndTimePicker = false
                focusManager.clearFocus(force = true)
            },
            onConfirmButton = {
                endTimePickerState.let {
                    napViewModel.updateEndTime(it.hour, it.minute)
                }
                showEndTimePicker = false
                focusManager.clearFocus(force = true)
            },
        ) {
            endTimePickerState = rememberTimePickerState(
                initialHour = napViewModel.endTimeHour,
                initialMinute = napViewModel.endTimeMinute,
                is24Hour = true,
            )

            if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                TimePicker(state = endTimePickerState)
            } else {
                TimeInput(state = endTimePickerState)
            }
        }
    }

    NapContent(
        orientation = configuration.orientation,
        title = napViewModel.title,
        observation = napViewModel.observation,
        date = napViewModel.date,
        startTime = napViewModel.startTime,
        endTime = napViewModel.endTime,
        onTitleChange = napViewModel::updateNapTitle,
        onObservationChange = napViewModel::updateNapObservation,
        onTitleDone = { focusManager.clearFocus(force = true) },
        onObservationDone = { focusManager.clearFocus(force = true) },
        onDateClick = { showDatePickerDialog = true },
        onStartTimeClick = { showStartTimePicker = true },
        onEndTimeClick = { showEndTimePicker = true },
        onOutsideClick = {
            keyboardController?.hide()
            focusManager.clearFocus()
        },
        onDeleteItemClick = {
            napViewModel.deleteNapById(napId)
            navigateUp()
        },
        onShareItemClick = {},
        onActionDone = {
            napViewModel.updateNap(napId)
            navigateUp()
        },
        navigateUp = navigateUp,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NapContent(
    orientation: Int,
    title: String,
    observation: String,
    date: String,
    startTime: String,
    endTime: String,
    onTitleChange: (title: String) -> Unit,
    onObservationChange: (observation: String) -> Unit,
    onTitleDone: () -> Unit,
    onObservationDone: () -> Unit,
    onDateClick: () -> Unit,
    onStartTimeClick: () -> Unit,
    onEndTimeClick: () -> Unit,
    onOutsideClick: () -> Unit,
    onDeleteItemClick: () -> Unit,
    onShareItemClick: () -> Unit,
    onActionDone: () -> Unit,
    navigateUp: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) { onOutsideClick() },
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.nap_label)) },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.up_button_label),
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { expanded = true },
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = stringResource(
                                    id = R.string.more_options_label,
                                ),
                            )

                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                CommonMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expandedValue ->
                                        expanded = expandedValue
                                    },
                                    onClick = { index ->
                                        when (index) {
                                            CommonMenuItemIndex.DELETE_ITEM_INDEX -> {
                                                onDeleteItemClick()
                                            }
                                            CommonMenuItemIndex.SHARE_ITEM_INDEX -> {
                                                onShareItemClick()
                                            }
                                        }
                                    },
                                )
                            }
                        }
                    }
                },
            )
        },
        floatingActionButton = {
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                FloatingActionButton(
                    onClick = onActionDone,
                ) {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = stringResource(id = R.string.done_label),
                    )
                }
            }
        },
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp)
                    .clearFocusOnKeyboardDismiss(),
                value = title,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Label,
                        contentDescription = stringResource(id = R.string.title_label),
                    )
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    this.defaultKeyboardAction(ImeAction.Done)
                    onTitleDone()
                }),
                onValueChange = { titleValue -> onTitleChange(titleValue) },
                singleLine = true,
                label = { Text(text = stringResource(id = R.string.title_label)) },
                placeholder = { Text(text = stringResource(id = R.string.insert_title_label)) },
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp)
                    .onFocusChanged { focusState ->
                        if (focusState.hasFocus) {
                            onDateClick()
                        }
                    },
                value = date,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.CalendarMonth,
                        contentDescription = stringResource(id = R.string.date_label),
                    )
                },
                onValueChange = {},
                singleLine = true,
                readOnly = true,
                label = { Text(text = stringResource(id = R.string.date_label)) },
                placeholder = { Text(text = stringResource(id = R.string.insert_date_label)) },
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp)
                    .onFocusChanged { focusState ->
                        if (focusState.hasFocus) {
                            onStartTimeClick()
                        }
                    },
                value = startTime,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Timer,
                        contentDescription = stringResource(id = R.string.start_time_label),
                    )
                },
                onValueChange = {},
                singleLine = true,
                readOnly = true,
                label = { Text(text = stringResource(id = R.string.start_time_label)) },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.insert_start_time_label),
                    )
                },
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp)
                    .onFocusChanged { focusState ->
                        if (focusState.hasFocus) {
                            onEndTimeClick()
                        }
                    },
                value = endTime,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.TimerOff,
                        contentDescription = stringResource(id = R.string.end_time_label),
                    )
                },
                onValueChange = {},
                singleLine = true,
                readOnly = true,
                label = { Text(text = stringResource(id = R.string.end_time_label)) },
                placeholder = { Text(text = stringResource(id = R.string.insert_end_time_label)) },
            )

            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1F, true)
                        .padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 88.dp)
                        .clearFocusOnKeyboardDismiss(),
                    value = observation,
                    leadingIcon = {
                        Row(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(top = 16.dp),
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Description,
                                contentDescription = stringResource(
                                    id = R.string.description_label,
                                ),
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        this.defaultKeyboardAction(ImeAction.Done)
                        onObservationDone()
                    }),
                    onValueChange = { observationValue -> onObservationChange(observationValue) },
                    label = { Text(text = stringResource(id = R.string.observation_label)) },
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.insert_observations_label),
                        )
                    },
                )
            } else {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .sizeIn(minHeight = 200.dp)
                        .padding(top = 8.dp, start = 16.dp, end = 16.dp)
                        .clearFocusOnKeyboardDismiss(),
                    value = observation,
                    leadingIcon = {
                        Row(
                            modifier = Modifier
                                .fillMaxHeight()
                                .sizeIn(minHeight = 200.dp)
                                .padding(top = 16.dp),
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Description,
                                contentDescription = stringResource(
                                    id = R.string.description_label,
                                ),
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        this.defaultKeyboardAction(ImeAction.Done)
                        onObservationDone()
                    }),
                    onValueChange = { observationValue -> onObservationChange(observationValue) },
                    label = { Text(text = stringResource(id = R.string.observation_label)) },
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.insert_observations_label),
                        )
                    },
                )

                FloatingActionButton(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(16.dp),
                    onClick = onActionDone,
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NapScreenPreview() {
    SleepTightTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            NapContent(
                orientation = 1,
                title = "",
                observation = "",
                date = "",
                startTime = "",
                endTime = "",
                onTitleChange = {},
                onObservationChange = {},
                onTitleDone = {},
                onObservationDone = {},
                onDateClick = {},
                onStartTimeClick = {},
                onEndTimeClick = {},
                onOutsideClick = {},
                onDeleteItemClick = {},
                onShareItemClick = {},
                onActionDone = {},
                navigateUp = {},
            )
        }
    }
}
