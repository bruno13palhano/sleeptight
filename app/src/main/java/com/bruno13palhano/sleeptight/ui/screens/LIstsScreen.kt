package com.bruno13palhano.sleeptight.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ListsScreen(
    onBabyStatusClick: () -> Unit,
    onNapsClick: () -> Unit,
    onNotificationsClick: () -> Unit
) {
    Column {
        Text(text = "Lists Screen")
        Button(
            onClick = {
                onBabyStatusClick()
            }
        ) {
            Text(text = "Baby Status NavGraph")
        }

        Button(
            onClick = {
                onNapsClick()
            }
        ) {
            Text(text = "Naps NavGraph")
        }

        Button(
            onClick = {
                onNotificationsClick()
            }
        ) {
            Text(text = "Notifications NavGraph")
        }
    }
}