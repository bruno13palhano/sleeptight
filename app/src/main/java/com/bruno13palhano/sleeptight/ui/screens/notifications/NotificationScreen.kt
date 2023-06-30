package com.bruno13palhano.sleeptight.ui.screens.notifications

import android.content.res.Configuration
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.ui.screens.CommonMenu

private const val NOTIFICATION_ACTION_PREFIX = "com.bruno13palhano.sleeptight"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    notificationId: Long,
    onDoneButtonClick: () -> Unit,
    onNavigationIconClick: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val focusManager = LocalFocusManager.current
    var title by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var repeat by remember { mutableStateOf(false) }
    var description by remember { mutableStateOf("") }

    var expanded by remember { mutableStateOf(false) }

    NotificationScreenContent(
        orientation = configuration.orientation,
        title = title,
        time = time,
        date = date,
        repeat = repeat,
        description = description,
        onTitleChange = {},
        onRepeatChange = {},
        onDescriptionChange = {},
        onTitleDone = {},
        onDescriptionDone = {},
        onTimeClick = {},
        onDateClick = {},
        onNavigationIconClick = {},
        onDoneButtonClick = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreenContent(
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
    onNavigationIconClick: () -> Unit,
    onDoneButtonClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.notification_label)) },
                navigationIcon = {
                    IconButton(onClick = onNavigationIconClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.up_button_label)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { expanded = true }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = stringResource(id = R.string.more_options_label)
                            )

                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CommonMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expandedValue ->
                                        expanded = expandedValue
                                    },
                                    onClick = { println("index: $it") }
                                )
                            }
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                FloatingActionButton(onClick = onDoneButtonClick) {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = stringResource(id = R.string.done_label)
                    )
                }
            }
        }
    ) {
        Column(modifier = Modifier
            .padding(it)
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp),
                value = title,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Label,
                        contentDescription = stringResource(id = R.string.title_label)
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
                placeholder = { Text(text = stringResource(id = R.string.insert_title_label)) }
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp)
                    .clickable { onTimeClick() },
                value = time,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Timer,
                        contentDescription = stringResource(id = R.string.hour_label)
                    )
                },
                singleLine = true,
                onValueChange = {},
                label = { Text(text = stringResource(id = R.string.hour_label)) },
                placeholder = { Text(text = stringResource(id = R.string.insert_hours_label)) }
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp)
                    .clickable { onDateClick() },
                value = date,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.CalendarMonth,
                        contentDescription = stringResource(id = R.string.date_label)
                    )
                },
                singleLine = true,
                onValueChange = {},
                label = { Text(text = stringResource(id = R.string.date_label)) },
                placeholder = { Text(text = stringResource(id = R.string.insert_date_label)) }
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                text = stringResource(id = R.string.repeat_label)
            )

            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = repeat,
                    onClick = { onRepeatChange(!repeat) }
                )
                Text(
                    text = stringResource(id = R.string.off_label)
                )

                RadioButton(
                    selected = !repeat,
                    onClick = { onRepeatChange(!repeat) }
                )
                Text(
                    text = stringResource(id = R.string.on_label)
                )
            }

            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1F, true)
                        .padding(start = 16.dp, end = 16.dp, bottom = 88.dp),
                    value = description,
                    onValueChange = { descriptionValue -> onDescriptionChange(descriptionValue) },
                    leadingIcon = {
                        Row(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(top = 16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Description,
                                contentDescription = stringResource(id = R.string.description_label)
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        this.defaultKeyboardAction(ImeAction.Done)
                        onDescriptionDone()
                    }),
                    label = { Text(text = stringResource(id = R.string.description_label)) },
                    placeholder = { Text(text = stringResource(id = R.string.insert_description_label)) }
                )
            } else {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .sizeIn(minHeight = 200.dp)
                        .padding(start = 16.dp, end = 16.dp),
                    value = description,
                    onValueChange = { descriptionValue -> onDescriptionChange(descriptionValue) },
                    leadingIcon = {
                        Row(
                            modifier = Modifier
                                .fillMaxHeight()
                                .sizeIn(minHeight = 200.dp)
                                .padding(top = 16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Description,
                                contentDescription = stringResource(id = R.string.description_label)
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        this.defaultKeyboardAction(ImeAction.Done)
                        onDescriptionDone()
                    }),
                    label = { Text(text = stringResource(id = R.string.description_label)) },
                    placeholder = { Text(text = stringResource(id = R.string.insert_description_label)) }
                )

                FloatingActionButton(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(16.dp),
                    onClick = onDoneButtonClick,
                ) {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = stringResource(id = R.string.done_label)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationScreenPreview() {
    NotificationScreenContent(
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
        onNavigationIconClick = {},
        onDoneButtonClick = {}
    )
}