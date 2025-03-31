package com.example.ottov1.ui.addedit

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ottov1.R
import java.text.SimpleDateFormat
import java.util.*
import com.example.ottov1.ui.components.TimePickerDialog

private const val TAG = "AddEditActivityScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditActivityScreen(
    activityId: Long,
    onNavigateBack: () -> Unit,
    viewModel: AddEditActivityViewModel = hiltViewModel()
) {
    Log.d(TAG, "Composing AddEditActivityScreen with activityId: $activityId")
    
    val activity by viewModel.activity.collectAsState()
    val saveResult by viewModel.saveResult.collectAsState()
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val showDatePicker = remember { mutableStateOf(false) }
    val showStartTimePicker = remember { mutableStateOf(false) }
    val showEndTimePicker = remember { mutableStateOf(false) }
    val showLocationDialog = remember { mutableStateOf(false) }
    val showMapDialog = remember { mutableStateOf(false) }
    
    LaunchedEffect(activityId) {
        Log.d(TAG, "LaunchedEffect triggered with activityId: $activityId")
        if (activityId != -1L) {
            viewModel.loadActivity(activityId)
        } else {
            viewModel.resetActivity()
        }
    }

    LaunchedEffect(saveResult) {
        Log.d(TAG, "SaveResult changed: $saveResult")
        when (val result = saveResult) {
            is SaveResult.Success -> {
                Log.d(TAG, "Save successful, navigating back")
                onNavigateBack()
                viewModel.clearSaveResult()
            }
            is SaveResult.Error -> {
                errorMessage = result.message
                Log.e(TAG, "Save error: $errorMessage")
                showErrorDialog = true
            }
            null -> Log.d(TAG, "SaveResult is null")
        }
    }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = {
                showErrorDialog = false
                viewModel.clearSaveResult()
            },
            title = { Text(stringResource(R.string.error_dialog_title)) },
            text = { Text(errorMessage) },
            confirmButton = {
                TextButton(onClick = {
                    showErrorDialog = false
                    viewModel.clearSaveResult()
                }) {
                    Text(stringResource(R.string.ok_button))
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(if (activityId == -1L) R.string.add_activity_title else R.string.edit_activity_title),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back_button)
                        )
                    }
                },
                actions = {
                    if (activityId != -1L) {
                        IconButton(onClick = { /* TODO: Implement delete confirmation if needed */ }) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = stringResource(R.string.delete_button)
                            )
                        }
                    }
                    IconButton(
                        onClick = {
                            Log.d(TAG, "Save button clicked")
                            if (!viewModel.saveActivity()) {
                                Log.w(TAG, "Save validation failed")
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = stringResource(R.string.save_button)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->
        ActivityFormContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            activity = activity,
            isNameError = activity.name.isBlank() && saveResult != null,
            onNameChange = { viewModel.updateName(it) },
            onDescriptionChange = { viewModel.updateDescription(it) },
            onActivityTypeChange = { viewModel.updateActivityType(it) },
            onDateChange = { viewModel.updateDate(it) },
            onStartTimeChange = { h, m -> viewModel.updateStartTime(h, m) },
            onEndTimeChange = { h, m -> viewModel.updateEndTime(h, m) },
            onLocationChange = { viewModel.updateLocation(it) },
            onGradeChange = { viewModel.updateGrade(it) },
            availableGrades = viewModel.availableGrades,
            onPeopleChange = { min, max -> viewModel.updatePeople(min, max) },
            showDatePicker = showDatePicker,
            showStartTimePicker = showStartTimePicker,
            showEndTimePicker = showEndTimePicker,
            showLocationDialog = showLocationDialog,
            showMapDialog = showMapDialog
        )
    }
}

private fun formatDate(timestamp: Long): String {
    val dateFormat = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())
    return dateFormat.format(Date(timestamp))
}