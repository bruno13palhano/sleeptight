package com.bruno13palhano.sleeptight.ui.screens.notifications

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Timer
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
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.ui.screens.shared.TimePickerDialog
import com.bruno13palhano.sleeptight.ui.screens.shared.clearFocusOnKeyboardDismiss
import com.bruno13palhano.sleeptight.ui.screens.notifications.receivers.NotificationReceiver
import com.bruno13palhano.sleeptight.ui.theme.SleepTightTheme

private const val NOTIFICATION_ACTION_PREFIX = "com.bruno13palhano.sleeptight"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewNotificationScreen(
    onDoneButtonClick: () -> Unit,
    onNavigationIconClick: () -> Unit,
    newNotificationViewModel: NewNotificationViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var showDatePickerDialog by remember { mutableStateOf(false) }
    var datePickerState = rememberDatePickerState()
    var showTimePickerDialog by remember { mutableStateOf(false) }
    var timePickerState = rememberTimePickerState()

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
                            newNotificationViewModel.updateDate(it)
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
                initialSelectedDateMillis = newNotificationViewModel.dateInMillis,
                initialDisplayMode = if (isPortraitMode(configuration.orientation)) {
                    DisplayMode.Picker
                } else {
                    DisplayMode.Input
                },
            )
            DatePicker(
                state = datePickerState,
                showModeToggle = isPortraitMode(configuration.orientation),
            )
        }
    }

    if (showTimePickerDialog) {
        TimePickerDialog(
            title = stringResource(id = R.string.hour_label),
            onCancelButton = {
                showTimePickerDialog = false
                focusManager.clearFocus(force = true)
            },
            onConfirmButton = {
                timePickerState.let {
                    newNotificationViewModel.updateTime(it.hour, it.minute)
                }
                showTimePickerDialog = false
                focusManager.clearFocus(force = true)
            },
        ) {
            timePickerState = rememberTimePickerState(
                initialHour = newNotificationViewModel.timeHour,
                initialMinute = newNotificationViewModel.timeMinute,
                is24Hour = true,
            )

            if (isPortraitMode(configuration.orientation)) {
                TimePicker(state = timePickerState)
            } else {
                TimeInput(state = timePickerState)
            }
        }
    }

    NewNotificationContent(
        orientation = configuration.orientation,
        title = newNotificationViewModel.title,
        time = newNotificationViewModel.time,
        date = newNotificationViewModel.date,
        repeat = newNotificationViewModel.repeat,
        description = newNotificationViewModel.description,
        onTitleChange = newNotificationViewModel::updateTitle,
        onRepeatChange = newNotificationViewModel::updateRepeat,
        onDescriptionChange = newNotificationViewModel::updateDescription,
        onTitleDone = { focusManager.clearFocus(force = true) },
        onDescriptionDone = { focusManager.clearFocus(force = true) },
        onTimeClick = { showTimePickerDialog = true },
        onDateClick = { showDatePickerDialog = true },
        onOutsideClick = {
            keyboardController?.hide()
            focusManager.clearFocus()
        },
        onNavigationIconClick = onNavigationIconClick,
        onDoneButtonClick = {
            newNotificationViewModel.insertNotification {
                setAlarm(
                    id = it,
                    title = newNotificationViewModel.title,
                    time = newNotificationViewModel.timeInMillis,
                    date = newNotificationViewModel.dateInMillis,
                    repeat = newNotificationViewModel.repeat,
                    description = newNotificationViewModel.description,
                    context = context,
                )
            }
            onDoneButtonClick()
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewNotificationContent(
    orientation: Int,
    title: String,
    time: String,
    date: String,
    repeat: Boolean,
    description: String,
    onTitleChange: (title: String) -> Unit,
    onRepeatChange: (repeat: Boolean) -> Unit,
    onDescriptionChange: (description: String) -> Unit,
    onTitleDone: () -> Unit,
    onDescriptionDone: () -> Unit,
    onTimeClick: () -> Unit,
    onDateClick: () -> Unit,
    onOutsideClick: () -> Unit,
    onNavigationIconClick: () -> Unit,
    onDoneButtonClick: () -> Unit,
) {
    Scaffold(
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) { onOutsideClick() },
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.new_notification_label)) },
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
            if (isPortraitMode(orientation)) {
                FloatingActionButton(onClick = onDoneButtonClick) {
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
                .fillMaxHeight()
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
                        imageVector = Icons.AutoMirrored.Filled.Label,
                        contentDescription = stringResource(id = R.string.title_label),
                    )
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    this.defaultKeyboardAction(ImeAction.Done)
                    onTitleDone()
                }),
                singleLine = true,
                onValueChange = { titleValue -> onTitleChange(titleValue) },
                label = { Text(text = stringResource(id = R.string.title_label)) },
                placeholder = { Text(text = stringResource(id = R.string.insert_title_label)) },
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp)
                    .onFocusChanged { focusState ->
                        if (focusState.hasFocus) {
                            onTimeClick()
                        }
                    },
                value = time,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Timer,
                        contentDescription = stringResource(id = R.string.hour_label),
                    )
                },
                singleLine = true,
                onValueChange = {},
                label = { Text(text = stringResource(id = R.string.hour_label)) },
                placeholder = { Text(text = stringResource(id = R.string.insert_hours_label)) },
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
                singleLine = true,
                onValueChange = {},
                label = { Text(text = stringResource(id = R.string.date_label)) },
                placeholder = { Text(text = stringResource(id = R.string.insert_date_label)) },
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                text = stringResource(id = R.string.repeat_label),
            )

            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(
                    selected = !repeat,
                    onClick = {
                        if (repeat) {
                            onRepeatChange(false)
                        }
                    },
                )
                Text(
                    text = stringResource(id = R.string.off_label),
                )

                RadioButton(
                    selected = repeat,
                    onClick = {
                        if (!repeat) {
                            onRepeatChange(true)
                        }
                    },
                )
                Text(
                    text = stringResource(id = R.string.on_label),
                )
            }

            if (isPortraitMode(orientation)) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1F, true)
                        .padding(start = 16.dp, end = 16.dp, bottom = 88.dp)
                        .clearFocusOnKeyboardDismiss(),
                    value = description,
                    onValueChange = { descriptionValue -> onDescriptionChange(descriptionValue) },
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
                        onDescriptionDone()
                    }),
                    label = { Text(text = stringResource(id = R.string.description_label)) },
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.insert_description_label),
                        )
                    },
                )
            } else {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .sizeIn(minHeight = 200.dp)
                        .padding(start = 16.dp, end = 16.dp)
                        .clearFocusOnKeyboardDismiss(),
                    value = description,
                    onValueChange = { descriptionValue -> onDescriptionChange(descriptionValue) },
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
                        onDescriptionDone()
                    }),
                    label = { Text(text = stringResource(id = R.string.description_label)) },
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.insert_description_label),
                        )
                    },
                )

                FloatingActionButton(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(16.dp),
                    onClick = onDoneButtonClick,
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
fun NewNotificationScreenPreview() {
    SleepTightTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            NewNotificationContent(
                orientation = 1,
                title = "",
                time = "",
                date = "",
                repeat = false,
                description = "",
                onTitleChange = {},
                onRepeatChange = {},
                onDescriptionChange = {},
                onTitleDone = {},
                onDescriptionDone = {},
                onTimeClick = {},
                onDateClick = {},
                onOutsideClick = {},
                onNavigationIconClick = {},
                onDoneButtonClick = {},
            )
        }
    }
}

fun isPortraitMode(orientation: Int): Boolean = orientation == Configuration.ORIENTATION_PORTRAIT

private fun setAlarm(
    id: Long,
    title: String,
    time: Long,
    date: Long,
    repeat: Boolean,
    description: String,
    context: Context,
) {
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    val notifyIntent = Intent(context, NotificationReceiver::class.java)
    notifyIntent.apply {
        action = "$NOTIFICATION_ACTION_PREFIX.$id"
        putExtra("id", id.toInt())
        putExtra("title", title)
        putExtra("description", description)
    }

    val notifyPendingIntent = PendingIntent.getBroadcast(
        context,
        id.toInt(),
        notifyIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
    )

    val alarmNotification = AlarmNotification(
        notificationManager = notificationManager,
        alarmManager = alarmManager,
    )

    alarmNotification.setAlarmManager(
        notifyPendingIntent = notifyPendingIntent,
        time = time,
        date = date,
        repeat = repeat,
    )
}
