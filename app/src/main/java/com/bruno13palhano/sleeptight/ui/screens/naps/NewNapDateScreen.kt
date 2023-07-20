package com.bruno13palhano.sleeptight.ui.screens.naps

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.ui.theme.SleepTightTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewNapDateScreen(
    onNextButtonClick: () -> Unit,
    onNavigationIconClick: () -> Unit,
    newNapViewModel: NewNapViewModel
) {
    val configuration = LocalConfiguration.current
    var showDatePickerDialog by remember { mutableStateOf(false) }
    var datePickerState = rememberDatePickerState()

    if (showDatePickerDialog) {
        DatePickerDialog(
            onDismissRequest = {
                showDatePickerDialog = false
            },
            confirmButton = {
                Button(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            newNapViewModel.updateDate(it)
                        }
                        showDatePickerDialog = false
                    }
                ) {
                    Text(text = stringResource(id = R.string.date_label))
                }
            }
        ) {
            datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = newNapViewModel.dateInMillis,
                initialDisplayMode = if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                        DisplayMode.Picker
                    } else {
                        DisplayMode.Input
                    }
            )
            DatePicker(
                state = datePickerState,
                showModeToggle = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
            )
        }
    }

    NewNapDateContent(
        date = newNapViewModel.date,
        onDateClick = { dateValue -> showDatePickerDialog = dateValue },
        onNavigationIconClick = onNavigationIconClick,
        onNextButtonClick = onNextButtonClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewNapDateContent(
    date: String,
    onDateClick: (show: Boolean) -> Unit,
    onNavigationIconClick: () -> Unit,
    onNextButtonClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.date_label)) },
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(it)
        ) {
            ElevatedCard(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .sizeIn(maxWidth = 200.dp)
                    .align(Alignment.CenterHorizontally),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                onClick = { onDateClick(true) }
            ) {
                Icon(
                    modifier = Modifier
                        .size(200.dp)
                        .padding(8.dp)
                        .align(Alignment.CenterHorizontally),
                    imageVector = Icons.Filled.CalendarMonth,
                    contentDescription = stringResource(id = R.string.date_label)
                )

                Text(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    text = date,
                    textAlign = TextAlign.Center,
                    fontSize = 22.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NewNapDateScreenPreview() {
    SleepTightTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            NewNapDateContent(
                date = "April 7, 2023",
                onDateClick = {},
                onNavigationIconClick = {},
                onNextButtonClick = {}
            )
        }
    }
}