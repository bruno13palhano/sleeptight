package com.bruno13palhano.sleeptight.ui.screens.naps

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun NapsScreen(
    onItemClick: (napId: Long) -> Unit,
    onAddButtonClick: () -> Unit
) {
    Column {
        Text(text = "Naps Screen")
        Button(onClick = { onItemClick(10L) }) {
            Text(text = "Go to Nap")
        }

        Button(onClick = onAddButtonClick) {
            Text(text = "Go to New Nap")
        }
    }
}