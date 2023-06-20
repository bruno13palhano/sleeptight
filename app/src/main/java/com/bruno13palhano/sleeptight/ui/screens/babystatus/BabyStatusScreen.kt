package com.bruno13palhano.sleeptight.ui.screens.babystatus

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.ui.screens.CommonMenu
import com.bruno13palhano.sleeptight.ui.screens.login.HeightField
import com.bruno13palhano.sleeptight.ui.screens.login.WeightField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BabyStatusScreen(
    babyStatusId: Long,
    onDoneButtonClick: () -> Unit,
    onNavigationIconClick: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val focusManager = LocalFocusManager.current
    var title by remember { mutableStateOf(TextFieldValue("")) }
    var date by remember { mutableStateOf(TextFieldValue("")) }
    var height by remember { mutableStateOf(TextFieldValue("")) }
    var weight by remember { mutableStateOf(TextFieldValue("")) }

    var expanded by remember { mutableStateOf(false) }

    when (configuration.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(text = stringResource(id = R.string.baby_status_label)) },
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
                    FloatingActionButton(onClick = onDoneButtonClick) {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = stringResource(id = R.string.add_button)
                        )
                    }
                }
            ) {
                Column(modifier = Modifier.padding(it)) {
                    CommonFields(
                        title = title,
                        date = date,
                        height = height,
                        weight = weight,
                        onDateClick = { focusManager.moveFocus(FocusDirection.Next) },
                        onTitleChange = { titleValue -> title = titleValue },
                        onHeightChange = { heightValue -> height = heightValue },
                        onWeightChange = { weightValue -> weight = weightValue },
                        onTitleDone = { focusManager.moveFocus(FocusDirection.Next) },
                        onHeightDone = { focusManager.moveFocus(FocusDirection.Next) },
                        onWeightDone = { focusManager.clearFocus() }
                    )
                }
            }
        }
        else -> {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(text = stringResource(id = R.string.baby_status_label)) },
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
                                onClick = {}
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
                }
            ) {
                Column(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    CommonFields(
                        title = title,
                        date = date,
                        height = height,
                        weight = weight,
                        onDateClick = { focusManager.moveFocus(FocusDirection.Next) },
                        onTitleChange = { titleValue -> title = titleValue },
                        onHeightChange = { heightValue -> height = heightValue },
                        onWeightChange = { weightValue -> weight = weightValue },
                        onTitleDone = { focusManager.moveFocus(FocusDirection.Next) },
                        onHeightDone = { focusManager.moveFocus(FocusDirection.Next) },
                        onWeightDone = { focusManager.clearFocus() }
                    )

                    FloatingActionButton(
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(16.dp),
                        onClick = onDoneButtonClick
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = stringResource(id = R.string.add_button)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonFields(
    title: TextFieldValue,
    date: TextFieldValue,
    height: TextFieldValue,
    weight: TextFieldValue,
    onDateClick: () -> Unit,
    onTitleChange: (title: TextFieldValue) -> Unit,
    onHeightChange: (height: TextFieldValue) -> Unit,
    onWeightChange: (weight: TextFieldValue) -> Unit,
    onTitleDone: () -> Unit,
    onHeightDone: () -> Unit,
    onWeightDone: () -> Unit
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
        onValueChange = { titleValue -> onTitleChange(titleValue) },
        singleLine = true,
        label = { Text(text = stringResource(id = R.string.title_label)) },
        placeholder = { Text(text = stringResource(id = R.string.insert_title_label)) }
    )

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 16.dp, end = 16.dp)
            .clickable { onDateClick() },
        value = date,
        readOnly = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.CalendarMonth,
                contentDescription = stringResource(id = R.string.date_label)
            )
        },
        onValueChange = {},
        singleLine = true,
        label = { Text(text = stringResource(id = R.string.date_label)) }
    )

    HeightField(
        height = height,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 16.dp, end = 16.dp),
        onHeightChange = { heightValue -> onHeightChange(heightValue) },
        onDone = onHeightDone
    )

    WeightField(
        weight = weight,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 16.dp, end = 16.dp),
        onWeightChange = { weightValue -> onWeightChange(weightValue) },
        onDone = onWeightDone
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun BabyStatusScreenPreview() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.baby_status_label)) },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.up_button_label)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = stringResource(id = R.string.more_options_label)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = stringResource(id = R.string.add_button)
                )
            }
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            CommonFields(
                title = TextFieldValue(""),
                date = TextFieldValue(""),
                height = TextFieldValue(""),
                weight = TextFieldValue(""),
                onDateClick = {},
                onTitleChange = {},
                onHeightChange = {},
                onWeightChange = {},
                onTitleDone = {},
                onHeightDone = {},
                onWeightDone = {}
            )
        }
    }
}