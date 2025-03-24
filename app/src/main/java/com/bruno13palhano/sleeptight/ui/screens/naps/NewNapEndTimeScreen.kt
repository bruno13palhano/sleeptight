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
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.TimerOff
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
fun NewNapEndTimeScreen(
    onDoneButtonClick: () -> Unit,
    onNavigationIconClick: () -> Unit,
    newNapViewModel: NewNapViewModel,
) {
    val configuration = LocalConfiguration.current
    var showEndTimePickerDialog by remember { mutableStateOf(false) }
    val endTimePickerState = rememberTimePickerState(
        initialHour = newNapViewModel.endTimeHour,
        initialMinute = newNapViewModel.endTimeMinute,
        is24Hour = true,
    )

    if (showEndTimePickerDialog) {
        TimePickerDialog(
            title = stringResource(id = R.string.end_time_label),
            onCancelButton = {
                showEndTimePickerDialog = false
            },
            onConfirmButton = {
                endTimePickerState.let {
                    newNapViewModel.updateEndTime(it.hour, it.minute)
                }
                showEndTimePickerDialog = false
            },
        ) {
            if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                TimePicker(state = endTimePickerState)
            } else {
                TimeInput(state = endTimePickerState)
            }
        }
    }

    NewNapEndTimeContent(
        endTime = newNapViewModel.endTime,
        onEndTimeClick = { show -> showEndTimePickerDialog = show },
        onNavigationIconClick = onNavigationIconClick,
        onDoneButtonClick = {
            newNapViewModel.insertNap()
            onDoneButtonClick()
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewNapEndTimeContent(
    endTime: String,
    onEndTimeClick: (show: Boolean) -> Unit,
    onNavigationIconClick: () -> Unit,
    onDoneButtonClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.end_time_label)) },
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
            FloatingActionButton(onClick = onDoneButtonClick) {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = stringResource(id = R.string.done_label),
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
                onClick = { onEndTimeClick(true) },
            ) {
                Icon(
                    modifier = Modifier
                        .size(200.dp)
                        .padding(8.dp)
                        .align(Alignment.CenterHorizontally),
                    imageVector = Icons.Filled.TimerOff,
                    contentDescription = stringResource(id = R.string.end_time_label),
                )

                Text(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    text = endTime,
                    textAlign = TextAlign.Center,
                    fontSize = 22.sp,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NewNapEndTimeScreenPreview() {
    SleepTightTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            NewNapEndTimeContent(
                endTime = "13:10",
                onEndTimeClick = {},
                onNavigationIconClick = {},
                onDoneButtonClick = {},
            )
        }
    }
}
