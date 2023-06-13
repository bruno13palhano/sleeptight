package com.bruno13palhano.sleeptight.ui.screens.babystatus

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun BabyStatusListScreen(
    onItemClick: (babyStatusId: Long) -> Unit,
    onAddButtonClick: () -> Unit
) {
    Column {
        Text(text = "Baby Status List Screen")

        Button(onClick = { onItemClick(3L) }) {
            Text(text = "Go to BabyStatus")
        }

        Button(onClick = onAddButtonClick) {
            Text(text = "Go to New Baby Status")
        }
    }
}