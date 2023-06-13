package com.bruno13palhano.sleeptight.ui.screens.naps

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun NewNapTitleAndObservationScreen(
    onNextButtonClick: () -> Unit
) {
    Column {
        Text(text = "New Nap Title and Observation Screen")
        Button(onClick = onNextButtonClick) {
            Text(text = "Go to New Nap date")
        }
    }
}