package com.bruno13palhano.sleeptight.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.bruno13palhano.sleeptight.R

object CommonMenuItemIndex {
    const val DELETE_ITEM_INDEX = 0
    const val SHARE_ITEM_INDEX = 1
}

@Composable
fun CircularProgress() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier.padding(16.dp),
            strokeWidth = 4.dp
        )
    }
}

@Composable
fun CommonMenu(
    expanded: Boolean,
    onDismissRequest: (expanded: Boolean) -> Unit,
    onClick: (index: Int) -> Unit
) {
    val commonMenuItems = arrayOf(
        stringResource(id = R.string.delete_nap_label),
        stringResource(id = R.string.share_label)
    )

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { onDismissRequest(false) }
    ) {
        commonMenuItems.forEachIndexed { index, itemValue ->
            DropdownMenuItem(
                text = { Text(text = itemValue) },
                onClick = {
                    onDismissRequest(false)
                    onClick(index)
                }
            )
        }
    }
}

@Composable
fun TimePickerDialog(
    title: String,
    onCancelButton: () -> Unit,
    onConfirmButton: () -> Unit,
    toggle: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onCancelButton,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .background(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = MaterialTheme.colorScheme.surface
                ),
        ) {
            toggle()
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    text = title,
                    style = MaterialTheme.typography.labelMedium
                )
                content()
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(
                        onClick = onCancelButton
                    ) { Text(text = stringResource(id = R.string.cancel_label)) }
                    TextButton(
                        onClick = onConfirmButton
                    ) { Text(text = stringResource(id = R.string.ok_label)) }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemList(
    id: Long,
    title: String,
    date: String,
    onItemClick: () -> Unit,
    onDeleteItemClick: (id: Long) -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 8.dp, end = 8.dp, bottom = 8.dp),
        onClick = onItemClick
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column {
                Text(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, top = 8.dp),
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 8.dp),
                    text = date,
                    fontFamily = FontFamily.Serif,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            IconButton(
                modifier = Modifier
                    .align(Alignment.CenterEnd),
                onClick = { onDeleteItemClick(id) }
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = stringResource(id = R.string.delete_label)
                )
            }
        }
    }
}