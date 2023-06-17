package com.bruno13palhano.sleeptight.ui.screens.naps

import android.content.res.Configuration
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.TimerOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bruno13palhano.sleeptight.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NapScreen(
    napId: Long,
    onDoneClick: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val focusManager = LocalFocusManager.current
    var title by remember { mutableStateOf(TextFieldValue("")) }
    var date by remember { mutableStateOf(TextFieldValue("")) }
    var startTime by remember { mutableStateOf(TextFieldValue("")) }
    var endTime by remember { mutableStateOf(TextFieldValue("")) }
    var observations by remember { mutableStateOf(TextFieldValue("")) }

    when (configuration.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            Scaffold(
                floatingActionButton = {
                    FloatingActionButton(onClick = onDoneClick) {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = stringResource(id = R.string.done_label)
                        )
                    }
                }
            ) {
                Column(modifier = Modifier.padding(it)) {
                    CommonFields(
                        title = title,
                        date = date,
                        startTime = startTime,
                        endTime = endTime,
                        onTitleChange = { titleValue -> title = titleValue },
                        onTitleDone = { focusManager.moveFocus(FocusDirection.Next) },
                        onDateDone = { focusManager.moveFocus(FocusDirection.Next) },
                        onStartTimeDone = { focusManager.moveFocus(FocusDirection.Next) },
                        onEndTimeDone = { focusManager.moveFocus(FocusDirection.Next) },
                    )

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 88.dp),
                        value = observations,
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
                            focusManager.clearFocus()
                        }),
                        onValueChange = { observationsValue -> observations = observationsValue },
                        label = { Text(text = stringResource(id = R.string.observation_label)) },
                        placeholder = { Text(text = stringResource(id = R.string.insert_observations_label)) }
                    )
                }
            }
        }
        else -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                CommonFields(
                    title = title,
                    date = date,
                    startTime = startTime,
                    endTime = endTime,
                    onTitleChange = { titleValue -> title = titleValue },
                    onTitleDone = { focusManager.moveFocus(FocusDirection.Next) },
                    onDateDone = { focusManager.moveFocus(FocusDirection.Next) },
                    onStartTimeDone = { focusManager.moveFocus(FocusDirection.Next) },
                    onEndTimeDone = { focusManager.moveFocus(FocusDirection.Next) },
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .sizeIn(minHeight = 200.dp)
                        .padding(top = 8.dp, start = 16.dp, end = 16.dp),
                    value = observations,
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
                        focusManager.clearFocus()
                    }),
                    onValueChange = { observationsValue -> observations = observationsValue },
                    label = { Text(text = stringResource(id = R.string.observation_label)) },
                    placeholder = { Text(text = stringResource(id = R.string.insert_observations_label)) }
                )

                FloatingActionButton(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(16.dp),
                    onClick = onDoneClick
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CommonFields(
    title: TextFieldValue,
    date: TextFieldValue,
    startTime: TextFieldValue,
    endTime: TextFieldValue,
    onTitleChange: (title: TextFieldValue) -> Unit,
    onTitleDone: () -> Unit,
    onDateDone: () -> Unit,
    onStartTimeDone: () -> Unit,
    onEndTimeDone: () -> Unit,
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp, start = 16.dp, end = 16.dp),
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
        onValueChange = { titleValue -> onTitleChange(titleValue) },
        singleLine = true,
        label = { Text(text = stringResource(id = R.string.title_label)) },
        placeholder = { Text(text = stringResource(id = R.string.insert_title_label)) }
    )

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 16.dp, end = 16.dp)
            .clickable { onDateDone() },
        value = date,
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.CalendarMonth,
                contentDescription = stringResource(id = R.string.date_label)
            )
        },
        onValueChange = {},
        singleLine = true,
        label = { Text(text = stringResource(id = R.string.date_label)) },
        placeholder = { Text(text = stringResource(id = R.string.insert_date_label)) }
    )

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 16.dp, end = 16.dp)
            .clickable { onStartTimeDone() },
        value = startTime,
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Timer,
                contentDescription = stringResource(id = R.string.start_time_label)
            )
        },
        onValueChange = {},
        singleLine = true,
        label = { Text(text = stringResource(id = R.string.start_time_label)) },
        placeholder = { Text(text = stringResource(id = R.string.insert_start_time_label)) }
    )

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 16.dp, end = 16.dp)
            .clickable { onEndTimeDone() },
        value = endTime,
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.TimerOff,
                contentDescription = stringResource(id = R.string.end_time_label)
            )
        },
        onValueChange = {},
        singleLine = true,
        label = { Text(text = stringResource(id = R.string.end_time_label)) },
        placeholder = { Text(text = stringResource(id = R.string.insert_end_time_label)) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NapScreenPreview() {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = stringResource(id = R.string.done_label)
                )
            }
        }
    ) {
        Column(Modifier.padding(it)) {
            CommonFields(
                title = TextFieldValue(""),
                date = TextFieldValue(""),
                startTime = TextFieldValue(""),
                endTime = TextFieldValue(""),
                onTitleChange = {},
                onTitleDone = { /*TODO*/ },
                onDateDone = { /*TODO*/ },
                onStartTimeDone = { /*TODO*/ },
                onEndTimeDone = { /*TODO*/ },
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 88.dp),
                value = TextFieldValue(""),
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

                }),
                onValueChange = {},
                label = { Text(text = stringResource(id = R.string.observation_label)) },
                placeholder = { Text(text = stringResource(id = R.string.insert_observations_label)) }
            )
        }
    }
}