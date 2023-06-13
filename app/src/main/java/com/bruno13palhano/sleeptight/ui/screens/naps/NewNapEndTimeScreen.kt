package com.bruno13palhano.sleeptight.ui.screens.naps

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun NewNapEndTimeScreen(
    onDoneButtonClick: () -> Unit
) {
    Column {
        Text(text = "New Nap End Time Screen")
        Button(onClick = onDoneButtonClick) {
            Text(text = "Go to Naps")
        }
    }
}