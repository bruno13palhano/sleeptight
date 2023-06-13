package com.bruno13palhano.sleeptight.ui.screens.babystatus

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun NewBabyStatusTitleAndDateScreen(
    onNextButtonClick: () -> Unit
) {
    Column {
        Text(text = "Baby Status Title And Date Screen")

        Button(onClick = onNextButtonClick) {
            Text(text = "Go to New Baby Status Height And Weight")
        }
    }
}