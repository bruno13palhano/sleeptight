package com.bruno13palhano.sleeptight.ui.screens.login

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bruno13palhano.sleeptight.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BabyBirthAccountScreen(
    onCreateAccountSuccess: () -> Unit,
    onNavigationIconClick: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val focusManager = LocalFocusManager.current
    var date by remember { mutableStateOf(TextFieldValue("")) }
    var time by remember { mutableStateOf(TextFieldValue("")) }
    var height by remember { mutableStateOf(TextFieldValue("")) }
    var weight by remember { mutableStateOf(TextFieldValue("")) }

    when (configuration.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(text = stringResource(id = R.string.birth_information_label)) },
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
                    FloatingActionButton(onClick = onCreateAccountSuccess) {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = stringResource(id = R.string.done_label)
                        )
                    }
                }
            ) {
                Column(modifier = Modifier.padding(it)) {
                    CommonFields(
                        date = date,
                        time = time,
                        height = height,
                        weight = weight,
                        focusManager = focusManager,
                        onDateClick = {},
                        onTimeClick = {},
                        onHeightChange = { heightValue -> height = heightValue },
                        onWeightChange = { weightValue -> weight = weightValue }
                    )
                }
            }
        }
        else -> {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(text = stringResource(id = R.string.birth_information_label)) },
                        navigationIcon = {
                            IconButton(onClick = onNavigationIconClick) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowBack,
                                    contentDescription = stringResource(id = R.string.up_button_label)
                                )
                            }
                        }
                    )
                }
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                ) {
                    CommonFields(
                        date = date,
                        time = time,
                        height = height,
                        weight = weight,
                        focusManager = focusManager,
                        onDateClick = {},
                        onTimeClick = {},
                        onHeightChange = { heightValue -> height = heightValue },
                        onWeightChange = { weightValue -> weight = weightValue }
                    )

                    FloatingActionButton(
                        onClick = onCreateAccountSuccess,
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.End)
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
}

@Composable
private fun CommonFields(
    date: TextFieldValue,
    time: TextFieldValue,
    height: TextFieldValue,
    weight: TextFieldValue,
    focusManager: FocusManager,
    onDateClick: () -> Unit,
    onTimeClick: () -> Unit,
    onHeightChange: (height: TextFieldValue) -> Unit,
    onWeightChange: (weight: TextFieldValue) -> Unit
) {
    DateField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 16.dp, end = 16.dp)
            .clickable { onDateClick() },
        date = date
    )

    TimeField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 16.dp, end = 16.dp)
            .clickable { onTimeClick() },
        time = time
    )

    HeightField(
        height = height,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 16.dp, end = 16.dp),
        onHeightChange = { heightValue -> onHeightChange(heightValue) },
        onDone = { focusManager.moveFocus(FocusDirection.Next) }
    )

    WeightField(
        weight = weight,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 16.dp, end = 16.dp),
        onWeightChange = { weightValue -> onWeightChange(weightValue) },
        onDone = { focusManager.clearFocus() },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun BabyBirthAccountScreenPreview() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.birth_information_label)) },
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
                    contentDescription = stringResource(id = R.string.done_label)
                )
            }
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            CommonFields(
                date = TextFieldValue(""),
                time = TextFieldValue(""),
                height = TextFieldValue(""),
                weight = TextFieldValue(""),
                focusManager = LocalFocusManager.current,
                onDateClick = {},
                onTimeClick = {},
                onHeightChange = {},
                onWeightChange = {}
            )
        }
    }
}