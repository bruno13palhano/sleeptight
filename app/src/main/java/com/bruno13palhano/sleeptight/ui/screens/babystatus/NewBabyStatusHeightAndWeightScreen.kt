package com.bruno13palhano.sleeptight.ui.screens.babystatus

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun NewBabyStatusHeightAndWeightScreen(
    onDoneButtonClick: () -> Unit
) {
    Column {
        Text(text = "Baby Status Height And Weight Screen")

        Button(onClick = onDoneButtonClick) {
            Text(text = "Go to All Baby Status")
        }
    }
}