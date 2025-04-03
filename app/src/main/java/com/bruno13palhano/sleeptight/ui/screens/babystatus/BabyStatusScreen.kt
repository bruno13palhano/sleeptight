package com.bruno13palhano.sleeptight.ui.screens.babystatus

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.ui.screens.shared.CommonMenu
import com.bruno13palhano.sleeptight.ui.screens.shared.CommonMenuItemIndex
import com.bruno13palhano.sleeptight.ui.screens.shared.clearFocusOnKeyboardDismiss
import com.bruno13palhano.sleeptight.ui.screens.login.HeightField
import com.bruno13palhano.sleeptight.ui.screens.login.WeightField
import com.bruno13palhano.sleeptight.ui.theme.SleepTightTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BabyStatusScreen(
    babyStatusId: Long,
    navigateUp: () -> Unit,
    babyStatusViewModel: BabyStatusViewModel = hiltViewModel(),
) {
    LaunchedEffect(key1 = null) {
        babyStatusViewModel.getBabyStatus(babyStatusId)
    }

    val configuration = LocalConfiguration.current
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var showDatePickerDialog by remember { mutableStateOf(false) }
    var datePickerState = rememberDatePickerState()

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
                            babyStatusViewModel.updateDate(it)
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
                initialSelectedDateMillis = babyStatusViewModel.dateInMillis,
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

    BabyStatusContent(
        orientation = configuration.orientation,
        title = babyStatusViewModel.title,
        date = babyStatusViewModel.date,
        height = babyStatusViewModel.height,
        weight = babyStatusViewModel.weight,
        onTitleChange = babyStatusViewModel::updateTitle,
        onHeightChange = babyStatusViewModel::updateHeight,
        onWeightChange = babyStatusViewModel::updateWeight,
        onTitleDone = { focusManager.moveFocus(FocusDirection.Next) },
        onHeightDone = { focusManager.moveFocus(FocusDirection.Next) },
        onWeightDone = { focusManager.clearFocus(force = true) },
        onDateClick = { show -> showDatePickerDialog = show },
        onOutsideClick = {
            keyboardController?.hide()
            focusManager.clearFocus()
        },
        onDeleteItemClick = { babyStatusViewModel.deleteBabyStatus(babyStatusId) },
        onShareItemClick = {},
        onDoneButtonClick = {
            babyStatusViewModel.updateBabyStatus(babyStatusId)
            navigateUp()
        },
        navigateUp = navigateUp,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BabyStatusContent(
    orientation: Int,
    title: String,
    date: String,
    height: String,
    weight: String,
    onTitleChange: (title: String) -> Unit,
    onHeightChange: (height: String) -> Unit,
    onWeightChange: (weight: String) -> Unit,
    onTitleDone: () -> Unit,
    onHeightDone: () -> Unit,
    onWeightDone: () -> Unit,
    onDateClick: (show: Boolean) -> Unit,
    onOutsideClick: () -> Unit,
    onDeleteItemClick: () -> Unit,
    onShareItemClick: () -> Unit,
    onDoneButtonClick: () -> Unit,
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
                title = { Text(text = stringResource(id = R.string.baby_status_label)) },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
                    onClick = onDoneButtonClick,
                ) {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = stringResource(id = R.string.add_button),
                    )
                }
            }
        },
    ) {
        Column(
            modifier = Modifier
                .padding(it)
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

            if (orientation != Configuration.ORIENTATION_PORTRAIT) {
                FloatingActionButton(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(16.dp),
                    onClick = navigateUp,
                ) {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = stringResource(id = R.string.add_button),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BabyStatusScreenPreview() {
    SleepTightTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            BabyStatusContent(
                orientation = 1,
                title = "",
                date = "",
                height = "",
                weight = "",
                onTitleChange = {},
                onHeightChange = {},
                onWeightChange = {},
                onTitleDone = {},
                onHeightDone = {},
                onWeightDone = {},
                onDateClick = {},
                onOutsideClick = {},
                onDeleteItemClick = {},
                onShareItemClick = {},
                onDoneButtonClick = {},
                navigateUp = {},
            )
        }
    }
}
