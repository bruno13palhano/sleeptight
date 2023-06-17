package com.bruno13palhano.sleeptight.ui.screens.naps

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.NavigateNext
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bruno13palhano.sleeptight.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewNapTitleAndObservationScreen(
    onNextButtonClick: () -> Unit,
    onNavigationIconClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    var title by remember { mutableStateOf(TextFieldValue("")) }
    var observations by remember { mutableStateOf(TextFieldValue("")) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.title_and_observations_label)) },
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
            FloatingActionButton(onClick = onNextButtonClick) {
                Icon(
                    imageVector = Icons.Filled.NavigateNext,
                    contentDescription = stringResource(id = R.string.next_label)
                )
            }
        }
    ) {
        CommonFields(
            modifier = Modifier.padding(it),
            title = title,
            observations = observations,
            onTitleChange = { titleValue -> title = titleValue },
            onObservationsChange = { observationsValue -> observations = observationsValue },
            onTitleDone = { focusManager.moveFocus(FocusDirection.Next) },
            onObservationsDone = { focusManager.clearFocus() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CommonFields(
    modifier: Modifier,
    title: TextFieldValue,
    observations: TextFieldValue,
    onTitleChange: (title: TextFieldValue) -> Unit,
    onObservationsChange: (observations: TextFieldValue) -> Unit,
    onTitleDone: () -> Unit,
    onObservationsDone: () -> Unit
) {
    Column(modifier = modifier) {
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
                .weight(1F, true)
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
                onObservationsDone()
            }),
            onValueChange = { observationsValue -> onObservationsChange(observationsValue) },
            label = { Text(text = stringResource(id = R.string.observation_label)) },
            placeholder = { Text(text = stringResource(id = R.string.insert_observations_label)) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun NewNapTitleAndObservationScreenPreview() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.title_and_observations_label)) },
                navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
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
                    imageVector = Icons.Filled.NavigateNext,
                    contentDescription = stringResource(id = R.string.next_label)
                )
            }
        }
    ) {
        CommonFields(
            modifier = Modifier.padding(it),
            title = TextFieldValue(""),
            observations = TextFieldValue(""),
            onTitleChange = {},
            onObservationsChange = {},
            onTitleDone = {},
            onObservationsDone = {}
        )
    }
}