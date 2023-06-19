package com.bruno13palhano.sleeptight.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.ui.screens.login.BabyNameField
import com.bruno13palhano.sleeptight.ui.screens.login.BirthplaceField
import com.bruno13palhano.sleeptight.ui.screens.login.DateField
import com.bruno13palhano.sleeptight.ui.screens.login.HeightField
import com.bruno13palhano.sleeptight.ui.screens.login.TimeField
import com.bruno13palhano.sleeptight.ui.screens.login.WeightField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    val menuItems = arrayOf(
        stringResource(id = R.string.edit_profile_label),
        stringResource(id = R.string.share_profile_label),
        stringResource(id = R.string.logout_label)
    )
    var expanded by remember { mutableStateOf(false) }

    val configuration = LocalConfiguration.current
    val focusManager = LocalFocusManager.current
    var babyName by remember { mutableStateOf(TextFieldValue("")) }
    var birthplace by remember { mutableStateOf(TextFieldValue("")) }
    var birthtime by remember { mutableStateOf(TextFieldValue("")) }
    var birthdate by remember { mutableStateOf(TextFieldValue("")) }
    var height by remember { mutableStateOf(TextFieldValue("")) }
    var weight by remember { mutableStateOf(TextFieldValue("")) }

    when(configuration.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(text = stringResource(id = R.string.settings_label)) },
                        actions = {
                            IconButton(
                                onClick = {
                                    expanded = true
                                }
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Filled.MoreVert,
                                        contentDescription = stringResource(id = R.string.more_options_label)
                                    )

                                    Column(
                                        modifier = Modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        DropdownMenu(
                                            expanded = expanded,
                                            onDismissRequest = { expanded = false }
                                        ) {
                                            menuItems.forEachIndexed { index, itemValue ->
                                                DropdownMenuItem(
                                                    text = { Text(text = itemValue) },
                                                    onClick = { expanded = false },
                                                )
                                            }
                                        }
                                    }
                                }
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
                CommonFields(
                    modifier = Modifier.padding(it),
                    babyName = babyName,
                    birthplace = birthplace,
                    birthtime = birthtime,
                    birthdate = birthdate,
                    height = height,
                    weight = weight,
                    onBabyNameChange = { babyNameValue -> babyName = babyNameValue },
                    onBirthplaceChange = { birthplaceValue -> birthplace = birthplaceValue },
                    onHeightChange = { heightValue -> height = heightValue },
                    onWeightChange = { weightValue ->  weight = weightValue },
                    onBabyNameDone = { focusManager.clearFocus() },
                    onBirthPlaceDone = { focusManager.clearFocus() },
                    onHeightDone = { focusManager.clearFocus() },
                    onWeightDone = { focusManager.clearFocus() },
                    onBirthtimeClick = { focusManager.clearFocus() },
                    onBirthdateClick = { focusManager.clearFocus() }
                )
            }
        }
        else -> {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(text = stringResource(id = R.string.settings_label)) },
                        actions = {
                            IconButton(
                                onClick = {
                                    expanded = true
                                }
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Filled.MoreVert,
                                        contentDescription = stringResource(id = R.string.more_options_label)
                                    )

                                    Column(
                                        modifier = Modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        DropdownMenu(
                                            expanded = expanded,
                                            onDismissRequest = { expanded = false }
                                        ) {
                                            menuItems.forEachIndexed { index, itemValue ->
                                                DropdownMenuItem(
                                                    text = { Text(text = itemValue) },
                                                    onClick = { expanded = false },
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    )
                }
            ) {
                Column(
                    modifier = Modifier
                        .padding(it)
                        .verticalScroll(rememberScrollState())
                ) {
                    CommonFields(
                        modifier = Modifier,
                        babyName = babyName,
                        birthplace = birthplace,
                        birthtime = birthtime,
                        birthdate = birthdate,
                        height = height,
                        weight = weight,
                        onBabyNameChange = { babyNameValue -> babyName = babyNameValue },
                        onBirthplaceChange = { birthplaceValue -> birthplace = birthplaceValue },
                        onHeightChange = { heightValue -> height = heightValue },
                        onWeightChange = { weightValue -> weight = weightValue },
                        onBabyNameDone = { focusManager.clearFocus() },
                        onBirthPlaceDone = { focusManager.clearFocus() },
                        onHeightDone = { focusManager.clearFocus() },
                        onWeightDone = { focusManager.clearFocus() },
                        onBirthtimeClick = { focusManager.clearFocus() },
                        onBirthdateClick = { focusManager.clearFocus() }
                    )

                    FloatingActionButton(
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(16.dp),
                        onClick = {}
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = stringResource(id = R.string.done_label)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CommonFields(
    modifier: Modifier,
    babyName: TextFieldValue,
    birthplace: TextFieldValue,
    birthtime: TextFieldValue,
    birthdate: TextFieldValue,
    height: TextFieldValue,
    weight: TextFieldValue,
    onBabyNameChange: (babyName: TextFieldValue) -> Unit,
    onBirthplaceChange: (birthplace: TextFieldValue) -> Unit,
    onHeightChange: (height: TextFieldValue) -> Unit,
    onWeightChange: (weight: TextFieldValue) -> Unit,
    onBabyNameDone: () -> Unit,
    onBirthPlaceDone: () -> Unit,
    onHeightDone: () -> Unit,
    onWeightDone: () -> Unit,
    onBirthtimeClick: () -> Unit,
    onBirthdateClick: () -> Unit
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .size(128.dp)
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp)
                    .clip(CircleShape)
                    .clickable { },
                imageVector = Icons.Filled.Image,
                contentDescription = stringResource(id = R.string.baby_photo_label)
            )

            Text(text = stringResource(id = R.string.username_label))
        }

        BabyNameField(
            babyName = babyName,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 16.dp, end = 16.dp),
            onBabyNameChange = { babyNameValue -> onBabyNameChange(babyNameValue) },
            onDone = onBabyNameDone
        )

        BirthplaceField(
            birthplace = birthplace,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 16.dp, end = 16.dp),
            onBirthplaceChange = { birthplaceValue -> onBirthplaceChange(birthplaceValue) },
            onDone = onBirthPlaceDone
        )

        HeightField(
            height = height,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 16.dp, end = 16.dp),
            onHeightChange = { heightValue -> onHeightChange(heightValue) },
            onDone = onHeightDone
        )

        WeightField(
            weight = weight,
            modifier = Modifier
                .fillMaxWidth()
                .padding( top = 8.dp, start = 16.dp, end = 16.dp),
            onWeightChange = { weightValue -> onWeightChange(weightValue) },
            onDone = onWeightDone
        )

        Row(
            modifier = Modifier.sizeIn(maxHeight = 70.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TimeField(
                modifier = Modifier
                    .fillMaxWidth(0.5F)
                    .padding(top = 8.dp, start = 16.dp, end = 8.dp)
                    .clickable { onBirthtimeClick() },
                time = birthtime
            )

            DateField(
                modifier = Modifier
                    .fillMaxWidth(1F)
                    .padding(top = 8.dp, start = 8.dp, end = 16.dp)
                    .clickable { onBirthdateClick() },
                date = birthdate
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    val menuItems = arrayOf(
        stringResource(id = R.string.edit_profile_label),
        stringResource(id = R.string.share_profile_label),
        stringResource(id = R.string.logout_label)
    )
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.settings_label)) },
                actions = {
                    IconButton(
                        onClick = {
                            expanded = true
                        }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = stringResource(id = R.string.more_options_label)
                            )

                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }
                                ) {
                                    menuItems.forEachIndexed { index, itemValue ->
                                        DropdownMenuItem(
                                            text = { Text(text = itemValue) },
                                            onClick = { expanded = false },
                                        )
                                    }
                                }
                            }
                        }
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
        CommonFields(
            modifier = Modifier.padding(it),
            babyName = TextFieldValue(""),
            birthplace = TextFieldValue(""),
            birthtime = TextFieldValue(""),
            birthdate = TextFieldValue(""),
            height = TextFieldValue(""),
            weight = TextFieldValue(""),
            onBabyNameChange = {},
            onBirthplaceChange = {},
            onHeightChange = {},
            onWeightChange = {},
            onBabyNameDone = {},
            onBirthPlaceDone = {},
            onHeightDone = {},
            onWeightDone = {},
            onBirthtimeClick = {},
            onBirthdateClick = {}
        )
    }
}