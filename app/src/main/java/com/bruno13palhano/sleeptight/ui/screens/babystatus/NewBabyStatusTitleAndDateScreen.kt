package com.bruno13palhano.sleeptight.ui.screens.babystatus

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.NavigateNext
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.ui.screens.clearFocusOnKeyboardDismiss
import com.bruno13palhano.sleeptight.ui.theme.SleepTightTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun NewBabyStatusTitleAndDateScreen(
    onNextButtonClick: () -> Unit,
    onNavigationIconClick: () -> Unit,
    newBabyStatusViewModel: NewBabyStatusViewModel,
) {
    val configuration = LocalConfiguration.current
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var showDatePickerDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = newBabyStatusViewModel.dateInMillis,
        initialDisplayMode = if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            DisplayMode.Picker
        } else {
            DisplayMode.Input
        },
    )

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
                            newBabyStatusViewModel.updateDate(it)
                        }
                        showDatePickerDialog = false
                        focusManager.clearFocus(force = true)
                    },
                ) {
                    Text(text = stringResource(id = R.string.date_label))
                }
            },
        ) {
            DatePicker(
                state = datePickerState,
                showModeToggle = configuration.orientation == Configuration.ORIENTATION_PORTRAIT,
            )
        }
    }

    NewBabyStatusTitleAndDateContent(
        title = newBabyStatusViewModel.title,
        date = newBabyStatusViewModel.date,
        onTitleChange = newBabyStatusViewModel::updateTitle,
        onTitleDone = { focusManager.moveFocus(FocusDirection.Next) },
        onDateClick = { show -> showDatePickerDialog = show },
        onOutsideClick = {
            keyboardController?.hide()
            focusManager.clearFocus()
        },
        onNextButtonClick = onNextButtonClick,
        onNavigationIconClick = onNavigationIconClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewBabyStatusTitleAndDateContent(
    title: String,
    date: String,
    onTitleChange: (title: String) -> Unit,
    onTitleDone: () -> Unit,
    onDateClick: (show: Boolean) -> Unit,
    onOutsideClick: () -> Unit,
    onNextButtonClick: () -> Unit,
    onNavigationIconClick: () -> Unit,
) {
    Scaffold(
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) { onOutsideClick() },
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.baby_status_title_and_date)) },
                navigationIcon = {
                    IconButton(onClick = onNavigationIconClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.up_button_label),
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNextButtonClick) {
                Icon(
                    imageVector = Icons.Filled.NavigateNext,
                    contentDescription = stringResource(id = R.string.add_button),
                )
            }
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
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
                            onDateClick(true)
                        }
                    },
                value = date,
                readOnly = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.CalendarMonth,
                        contentDescription = stringResource(id = R.string.date_label),
                    )
                },
                onValueChange = {},
                singleLine = true,
                label = { Text(text = stringResource(id = R.string.date_label)) },
                placeholder = { Text(text = stringResource(id = R.string.insert_date_label)) },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NewBabyStatusTitleAndDateScreenPreview() {
    SleepTightTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            NewBabyStatusTitleAndDateContent(
                title = "",
                date = "",
                onTitleChange = {},
                onTitleDone = {},
                onDateClick = {},
                onOutsideClick = {},
                onNextButtonClick = {},
                onNavigationIconClick = {},
            )
        }
    }
}
