package com.bruno13palhano.sleeptight.ui.screens.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bruno13palhano.sleeptight.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BabyBirthAccountScreen(
    onCreateAccountSuccess: () -> Unit
) {
    Scaffold(
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
            var date by remember { mutableStateOf(TextFieldValue("")) }
            DateField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp, start = 16.dp, end = 16.dp)
                    .clickable { println("Date was clicked!") },
                date = date
            )

            var time by remember { mutableStateOf(TextFieldValue("")) }
            TimeField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp)
                    .clickable { println("Time was clicked!") },
                time = time
            )

            val focusManager = LocalFocusManager.current
            var height by remember { mutableStateOf(TextFieldValue("")) }
            HeightField(
                height = height,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp),
                onHeightChange = { heightValue -> height = heightValue },
                onDone = { focusManager.moveFocus(FocusDirection.Next) }
            )

            var weight by remember { mutableStateOf(TextFieldValue("")) }
            WeightField(
                weight = weight,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp),
                onWeightChange = { weightValue -> weight = weightValue },
                onDone = { focusManager.clearFocus() },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun BabyBirthAccountScreenPreview() {
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
        Column(modifier = Modifier.padding(it)) {
            DateField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp, start = 16.dp, end = 16.dp),
                date = TextFieldValue("")
            )

            TimeField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp),
                time = TextFieldValue("")
            )

            HeightField(
                height = TextFieldValue(""),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp),
                onHeightChange = {},
                onDone = {}
            )

            WeightField(
                weight = TextFieldValue(""),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp),
                onWeightChange = {},
                onDone = {},
            )
        }
    }
}