package com.bruno13palhano.sleeptight.ui.screens.naps

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberTimePickerState
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
import com.bruno13palhano.sleeptight.ui.screens.TimePickerDialog
import com.bruno13palhano.sleeptight.ui.theme.SleepTightTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewNapStartTimeScreen(
    onNextButtonClick: () -> Unit,
    onNavigationIconClick: () -> Unit,
    newNapViewModel: NewNapViewModel,
) {
    val configuration = LocalConfiguration.current
    var showStartTimePickerDialog by remember { mutableStateOf(false) }
    val startTimePickerState = rememberTimePickerState(
        initialHour = newNapViewModel.startTimeHour,
        initialMinute = newNapViewModel.startTimeMinute,
        is24Hour = true,
    )

    if (showStartTimePickerDialog) {
        TimePickerDialog(
            title = stringResource(id = R.string.start_time_label),
            onCancelButton = {
                showStartTimePickerDialog = false
            },
            onConfirmButton = {
                startTimePickerState.let {
                    newNapViewModel.updateStartTime(it.hour, it.minute)
                }
                showStartTimePickerDialog = false
            },
        ) {
            if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                TimePicker(state = startTimePickerState)
            } else {
                TimeInput(state = startTimePickerState)
            }
        }
    }

    NewNapStartTimeContent(
        startTime = newNapViewModel.startTime,
        onStartTimeClick = { show -> showStartTimePickerDialog = show },
        onNavigationIconClick = onNavigationIconClick,
        onNextButtonClick = onNextButtonClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewNapStartTimeContent(
    startTime: String,
    onStartTimeClick: (show: Boolean) -> Unit,
    onNavigationIconClick: () -> Unit,
    onNextButtonClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.start_time_label)) },
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
            FloatingActionButton(onClick = onNextButtonClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.NavigateNext,
                    contentDescription = stringResource(id = R.string.next_label),
                )
            }
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
        ) {
            ElevatedCard(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .sizeIn(maxWidth = 200.dp)
                    .align(Alignment.CenterHorizontally),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
                onClick = { onStartTimeClick(true) },
            ) {
                Icon(
                    modifier = Modifier
                        .size(200.dp)
                        .padding(8.dp)
                        .align(Alignment.CenterHorizontally),
                    imageVector = Icons.Filled.Timer,
                    contentDescription = stringResource(id = R.string.start_time_label),
                )

                Text(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    text = startTime,
                    textAlign = TextAlign.Center,
                    fontSize = 22.sp,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NewNapStartTimeScreenPreview() {
    SleepTightTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            NewNapStartTimeContent(
                startTime = "12:00",
                onStartTimeClick = {},
                onNavigationIconClick = {},
                onNextButtonClick = {},
            )
        }
    }
}
