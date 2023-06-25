package com.bruno13palhano.sleeptight.ui.screens.babystatus

import android.icu.text.DecimalFormat
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Balance
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.SquareFoot
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bruno13palhano.sleeptight.R
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewBabyStatusHeightAndWeightScreen(
    onDoneButtonClick: () -> Unit,
    onNavigationIconClick: () -> Unit,
    newBabyStatusViewModel: NewBabyStatusViewModel
) {
    val decimalFormat = DecimalFormat.getInstance(Locale.getDefault()) as DecimalFormat
    val decimalSeparator = decimalFormat.decimalFormatSymbols.decimalSeparator
    val pattern = remember { Regex("^\\d*\\$decimalSeparator?\\d*\$") }
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.baby_status_height_and_weight)) },
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
            FloatingActionButton(
                onClick = {
                    newBabyStatusViewModel.insertBabyStatus()
                    onDoneButtonClick()
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = stringResource(id = R.string.done_label)
                )
            }
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp),
                value = newBabyStatusViewModel.height,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.SquareFoot,
                        contentDescription = stringResource(id = R.string.birth_height_label)
                    )
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Decimal
                ),
                keyboardActions = KeyboardActions(onDone = {
                    this.defaultKeyboardAction(ImeAction.Done)
                    focusManager.moveFocus(FocusDirection.Next)
                }),
                onValueChange = { heightValue ->
                    if (heightValue.isEmpty() || heightValue.matches(pattern)) {
                        newBabyStatusViewModel.updateHeight(heightValue)
                    }
                },
                singleLine = true,
                label = { Text(text = stringResource(id = R.string.birth_height_label)) },
                placeholder = { Text(text = stringResource(id = R.string.insert_height_label)) }
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp),
                value = newBabyStatusViewModel.weight,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Balance,
                        contentDescription = stringResource(id = R.string.birth_weight_label)
                    )
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Decimal
                ),
                keyboardActions = KeyboardActions(onDone = {
                    this.defaultKeyboardAction(ImeAction.Done)
                    focusManager.clearFocus()
                }),
                onValueChange = { weightValue ->
                    if (weightValue.isEmpty() || weightValue.matches(pattern)) {
                        newBabyStatusViewModel.updateWeight(weightValue)
                    }
                },
                singleLine = true,
                label = { Text(text = stringResource(id = R.string.birth_weight_label)) },
                placeholder = { Text(text = stringResource(id = R.string.insert_weight_label)) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun NewBabyStatusHeightAndWeightScreenPreview() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.baby_status_height_and_weight)) },
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
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp),
                value = "",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.SquareFoot,
                        contentDescription = stringResource(id = R.string.birth_height_label)
                    )
                },
                onValueChange = {},
                singleLine = true,
                label = { Text(text = stringResource(id = R.string.birth_height_label)) },
                placeholder = { Text(text = stringResource(id = R.string.insert_height_label)) }
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp),
                value = "",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Balance,
                        contentDescription = stringResource(id = R.string.birth_weight_label)
                    )
                },
                onValueChange = {},
                singleLine = true,
                label = { Text(text = stringResource(id = R.string.birth_weight_label)) },
                placeholder = { Text(text = stringResource(id = R.string.insert_weight_label)) }
            )
        }
    }
}