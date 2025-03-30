package com.example.ottov1.ui.addedit

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ottov1.R
import com.example.ottov1.navigation.NEW_ACTIVITY_ID

private const val TAG = "AddEditActivityDialog"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditActivityDialog(
    activityId: Long,
    onDismiss: () -> Unit,
    viewModel: AddEditActivityViewModel = hiltViewModel()
) {
    val activity by viewModel.activity.collectAsState()
    val saveResult by viewModel.saveResult.collectAsState()

    // State for dialog visibility - passed to ActivityFormContent
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val showDatePicker = remember { mutableStateOf(false) }
    val showActivityTypeDialog = remember { mutableStateOf(false) }
    val showStartTimePicker = remember { mutableStateOf(false) }
    val showEndTimePicker = remember { mutableStateOf(false) }
    val showLocationDialog = remember { mutableStateOf(false) }
    val showMapDialog = remember { mutableStateOf(false) }
    var showDeleteConfirmationDialog by remember { mutableStateOf(false) }

    // Load activity effect
    LaunchedEffect(activityId) {
        Log.d(TAG, "LaunchedEffect triggered with activityId: $activityId")
        if (activityId != NEW_ACTIVITY_ID) {
            viewModel.loadActivity(activityId)
        } else {
            viewModel.resetActivity() // Ensure clean state for new activity
        }
    }

    // Save result effect
    LaunchedEffect(saveResult) {
        Log.d(TAG, "SaveResult changed: $saveResult")
        when (val result = saveResult) { // Use local val for smart cast
            is SaveResult.Success -> {
                Log.d(TAG, "Save successful, dismissing dialog")
                onDismiss()
                viewModel.clearSaveResult() // Clear result after handling
            }
            is SaveResult.Error -> {
                errorMessage = result.message
                Log.e(TAG, "Save error: $errorMessage")
                showErrorDialog = true
                // Don't clear result here, let the error dialog do it
            }
            null -> Log.d(TAG, "SaveResult is null")
        }
    }

    // Error Dialog
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = {
                showErrorDialog = false
                viewModel.clearSaveResult() // Clear result when dialog dismissed
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

    // Main Dialog
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false, // Keep false to prevent accidental dismiss
            usePlatformDefaultWidth = false // Use full screen
        )
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(if (activityId == NEW_ACTIVITY_ID) R.string.add_activity_title else R.string.edit_activity_title),
                            style = MaterialTheme.typography.titleLarge // Use theme typography
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onDismiss) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = stringResource(R.string.close_button)
                                // Tint will be handled by theme
                            )
                        }
                    },
                    actions = {
                        // Only show delete button if editing an existing activity
                        if (activityId != NEW_ACTIVITY_ID) {
                             IconButton(onClick = { showDeleteConfirmationDialog = true }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = stringResource(R.string.delete_button)
                                )
                            }
                        }
                        TextButton(
                            onClick = {
                                Log.d(TAG, "Save button clicked")
                                // Validation is now primarily handled within ViewModel's saveActivity
                                if (!viewModel.saveActivity()) {
                                     Log.w(TAG, "Save validation failed (likely empty name)")
                                     // saveResult state change will trigger error dialog if needed
                                }
                            }
                        ) {
                            Text(stringResource(R.string.save_button))
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface, // Use theme color
                        titleContentColor = MaterialTheme.colorScheme.onSurface,
                        navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                        actionIconContentColor = MaterialTheme.colorScheme.primary // Make save button prominent
                    )
                )
            }
        ) { paddingValues ->
            // Use the extracted form content
            ActivityFormContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(dimensionResource(R.dimen.activity_horizontal_margin)) // Use resource
                    .verticalScroll(rememberScrollState()),
                activity = activity,
                isNameError = activity.name.isBlank() && saveResult != null, // Pass error state
                onNameChange = { viewModel.updateName(it) },
                onDescriptionChange = { viewModel.updateDescription(it) },
                onActivityTypeChange = { viewModel.updateActivityType(it) },
                onDateChange = { viewModel.updateDate(it) },
                onStartTimeChange = { h, m -> viewModel.updateStartTime(h, m) },
                onEndTimeChange = { h, m -> viewModel.updateEndTime(h, m) },
                onLocationChange = { viewModel.updateLocation(it) },
                showDatePicker = showDatePicker,
                showActivityTypeDialog = showActivityTypeDialog,
                showStartTimePicker = showStartTimePicker,
                showEndTimePicker = showEndTimePicker,
                showLocationDialog = showLocationDialog,
                showMapDialog = showMapDialog
            )
        }
    }

     // Delete Confirmation Dialog
    if (showDeleteConfirmationDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmationDialog = false },
            icon = { Icon(Icons.Default.Warning, contentDescription = stringResource(R.string.content_description_delete_warning)) },
            title = { Text(stringResource(R.string.delete_confirmation_title)) },
            text = { Text(stringResource(R.string.delete_confirmation_message)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteActivity()
                        showDeleteConfirmationDialog = false
                        // SaveResult Success will trigger dismiss via LaunchedEffect
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(stringResource(R.string.delete_button))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmationDialog = false }) {
                    Text(stringResource(R.string.cancel_button))
                }
            }
        )
    }
} 