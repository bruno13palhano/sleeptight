package com.bruno13palhano.sleeptight.ui.screens

import android.content.res.Configuration
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.ui.screens.login.BabyNameField
import com.bruno13palhano.sleeptight.ui.screens.login.BirthplaceField
import com.bruno13palhano.sleeptight.ui.screens.login.DateField
import com.bruno13palhano.sleeptight.ui.screens.login.HeightField
import com.bruno13palhano.sleeptight.ui.screens.login.TimeField
import com.bruno13palhano.sleeptight.ui.screens.login.WeightField
import com.bruno13palhano.sleeptight.ui.theme.SleepTightTheme
import com.bruno13palhano.sleeptight.ui.util.getBytes

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SettingsScreen(
    navigateToLogin: () -> Unit,
    settingsViewModel: SettingsViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val focusManager = LocalFocusManager.current
    val isEditable by settingsViewModel.isEditable.collectAsStateWithLifecycle()
    val keyboardController = LocalSoftwareKeyboardController.current
    var showDatePickerDialog by remember { mutableStateOf(false) }
    var datePickerState = rememberDatePickerState()
    var showTimePickerDialog by remember { mutableStateOf(false) }
    var timePickerState = rememberTimePickerState()

    val galleryLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                settingsViewModel.updatePhotoUri(it)
                getBytes(context, it)?.let { imageByteArray ->
                    settingsViewModel.updatePhotoByteArray(imageByteArray)
                }
            }
        }

    if (showDatePickerDialog) {
        DatePickerDialog(
            onDismissRequest = {
                showDatePickerDialog = false
                focusManager.clearFocus(force = true)
            },
            confirmButton = {
                Button(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            settingsViewModel.updateBirthdate(it)
                        }
                        showDatePickerDialog = false
                        focusManager.clearFocus(force = true)
                    },
                ) {
                    Text(text = stringResource(id = R.string.date_label))
                }
            },
        ) {
            datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = settingsViewModel.birthdateInMillis,
                initialDisplayMode = if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    DisplayMode.Picker
                } else {
                    DisplayMode.Input
                },
            )
            DatePicker(
                state = datePickerState,
                showModeToggle = configuration.orientation == Configuration.ORIENTATION_PORTRAIT,
            )
        }
    }

    if (showTimePickerDialog) {
        TimePickerDialog(
            title = stringResource(id = R.string.birth_time_label),
            onCancelButton = {
                showTimePickerDialog = false
                focusManager.clearFocus(force = true)
            },
            onConfirmButton = {
                timePickerState.let {
                    settingsViewModel.updateBirthtime(it.hour, it.minute)
                }
                showTimePickerDialog = false
                focusManager.clearFocus(force = true)
            },
        ) {
            timePickerState = rememberTimePickerState(
                initialHour = settingsViewModel.birthtimeHour,
                initialMinute = settingsViewModel.birthtimeMinute,
                is24Hour = true,
            )

            if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                TimePicker(state = timePickerState)
            } else {
                TimeInput(state = timePickerState)
            }
        }
    }

    SettingsContent(
        orientation = configuration.orientation,
        isEditable = isEditable,
        username = settingsViewModel.username,
        babyName = settingsViewModel.babyName,
        photoUri = settingsViewModel.photoUri,
        birthplace = settingsViewModel.birthplace,
        height = settingsViewModel.height,
        weight = settingsViewModel.weight,
        birthtime = settingsViewModel.birthtime,
        birthdate = settingsViewModel.birthdate,
        onBabyNameChange = settingsViewModel::updateBabyName,
        onPhotoClick = { galleryLauncher.launch("image/*") },
        onBirthplaceChange = settingsViewModel::updateBirthplace,
        onHeightChange = settingsViewModel::updateHeight,
        onWeightChange = settingsViewModel::updateWeight,
        onBabyNameDone = { focusManager.clearFocus(force = true) },
        onBirthplaceDone = { focusManager.clearFocus(force = true) },
        onHeightDone = { focusManager.clearFocus(force = true) },
        onWeightDone = { focusManager.clearFocus(force = true) },
        onBirthtimeDone = { showTimePickerDialog = true },
        onBirthdateDone = { showDatePickerDialog = true },
        onOutsideClick = {
            keyboardController?.hide()
            focusManager.clearFocus()
        },
        onItemClick = { index ->
            when (index) {
                SettingsMenuIndex.EDIT_ITEM_INDEX -> {
                    settingsViewModel.activeEditable()
                }
                SettingsMenuIndex.SHARE_ITEM_INDEX -> {
                }
                SettingsMenuIndex.LOGOUT_ITEM_INDEX -> {
                    settingsViewModel.logout()
                    navigateToLogin()
                }
                else -> {}
            }
        },
        onActionButtonClick = {
            if (isEditable) {
                settingsViewModel.updateUserValues()
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsContent(
    orientation: Int,
    isEditable: Boolean,
    username: String,
    babyName: String,
    photoUri: Uri,
    birthplace: String,
    height: String,
    weight: String,
    birthtime: String,
    birthdate: String,
    onBabyNameChange: (babyName: String) -> Unit,
    onPhotoClick: () -> Unit,
    onBirthplaceChange: (birthplace: String) -> Unit,
    onHeightChange: (height: String) -> Unit,
    onWeightChange: (weight: String) -> Unit,
    onBabyNameDone: () -> Unit,
    onBirthplaceDone: () -> Unit,
    onHeightDone: () -> Unit,
    onWeightDone: () -> Unit,
    onBirthtimeDone: () -> Unit,
    onBirthdateDone: () -> Unit,
    onOutsideClick: () -> Unit,
    onItemClick: (index: Int) -> Unit,
    onActionButtonClick: () -> Unit,
) {
    val menuItems = arrayOf(
        stringResource(id = R.string.edit_profile_label),
        stringResource(id = R.string.share_profile_label),
        stringResource(id = R.string.logout_label),
    )
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) { onOutsideClick() },
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.settings_label)) },
                actions = {
                    IconButton(
                        onClick = {
                            expanded = true
                        },
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = stringResource(
                                    id = R.string.more_options_label,
                                ),
                            )

                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false },
                                ) {
                                    menuItems.forEachIndexed { index, itemValue ->
                                        DropdownMenuItem(
                                            text = { Text(text = itemValue) },
                                            onClick = {
                                                onItemClick(index)
                                                expanded = false
                                            },
                                        )
                                    }
                                }
                            }
                        }
                    }
                },
            )
        },
        floatingActionButton = {
            if (orientation == Configuration.ORIENTATION_PORTRAIT && isEditable) {
                FloatingActionButton(onClick = onActionButtonClick) {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = stringResource(id = R.string.done_label),
                    )
                }
            }
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(rememberScrollState()),
        ) {
            Surface(
                modifier = Modifier
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp),
                color = MaterialTheme.colorScheme.inverseOnSurface,
                shape = RoundedCornerShape(8.dp),
            ) {
                Row(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        modifier = Modifier
                            .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
                            .size(128.dp)
                            .clip(CircleShape)
                            .border(2.dp, MaterialTheme.colorScheme.secondaryContainer, CircleShape)
                            .clickable {
                                if (isEditable) {
                                    onPhotoClick()
                                }
                            },
                        contentScale = ContentScale.Crop,
                        painter = rememberAsyncImagePainter(photoUri),
                        contentDescription = stringResource(id = R.string.baby_photo_label),
                    )

                    Text(
                        text = username,
                        fontSize = 22.sp,
                    )
                }
            }

            BabyNameField(
                babyName = babyName,
                isEnabled = isEditable,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp)
                    .clearFocusOnKeyboardDismiss(),
                onBabyNameChange = { babyNameValue -> onBabyNameChange(babyNameValue) },
                onDone = onBabyNameDone,
            )

            BirthplaceField(
                birthplace = birthplace,
                isEnabled = isEditable,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp)
                    .clearFocusOnKeyboardDismiss(),
                onBirthplaceChange = { birthplaceValue -> onBirthplaceChange(birthplaceValue) },
                onDone = onBirthplaceDone,
            )

            HeightField(
                height = height,
                isEnabled = isEditable,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp)
                    .clearFocusOnKeyboardDismiss(),
                onHeightChange = { heightValue -> onHeightChange(heightValue) },
                onDone = onHeightDone,
            )

            WeightField(
                weight = weight,
                isEnabled = isEditable,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp)
                    .clearFocusOnKeyboardDismiss(),
                onWeightChange = { weightValue -> onWeightChange(weightValue) },
                onDone = onWeightDone,
            )

            Row(
                modifier = Modifier.sizeIn(maxHeight = 70.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TimeField(
                    modifier = Modifier
                        .fillMaxWidth(0.5F)
                        .padding(top = 8.dp, start = 16.dp, end = 8.dp)
                        .onFocusChanged { focusState ->
                            if (focusState.hasFocus) {
                                onBirthtimeDone()
                            }
                        },
                    time = birthtime,
                    isEnabled = isEditable,
                )

                DateField(
                    modifier = Modifier
                        .fillMaxWidth(1F)
                        .padding(top = 8.dp, start = 8.dp, end = 16.dp)
                        .onFocusChanged { focusState ->
                            if (focusState.hasFocus) {
                                onBirthdateDone()
                            }
                        },
                    date = birthdate,
                    isEnabled = isEditable,
                )
            }

            if (orientation != Configuration.ORIENTATION_PORTRAIT) {
                if (isEditable) {
                    FloatingActionButton(
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(16.dp),
                        onClick = onActionButtonClick,
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = stringResource(id = R.string.done_label),
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.size(8.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SleepTightTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            SettingsContent(
                orientation = 1,
                isEditable = false,
                username = "",
                babyName = "",
                photoUri = Uri.EMPTY,
                birthplace = "",
                height = "",
                weight = "",
                birthtime = "",
                birthdate = "",
                onBabyNameChange = {},
                onPhotoClick = {},
                onBirthplaceChange = {},
                onHeightChange = {},
                onWeightChange = {},
                onBabyNameDone = {},
                onBirthplaceDone = {},
                onHeightDone = {},
                onWeightDone = {},
                onBirthtimeDone = {},
                onBirthdateDone = {},
                onOutsideClick = {},
                onItemClick = {},
                onActionButtonClick = {},
            )
        }
    }
}

object SettingsMenuIndex {
    const val EDIT_ITEM_INDEX = 0
    const val SHARE_ITEM_INDEX = 1
    const val LOGOUT_ITEM_INDEX = 2
}
