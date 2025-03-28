package com.example.ottov1.ui.addedit

import android.util.Log
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ottov1.R
import com.example.ottov1.ui.components.ActivityTypeSelectionDialog
import com.example.ottov1.ui.components.DatePickerDialog
import com.example.ottov1.ui.components.LocationSelectionDialog
import com.example.ottov1.ui.components.MapDialog
import com.example.ottov1.ui.components.MaterialTimePickerDialog
import java.text.SimpleDateFormat
import java.util.*

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
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    var showActivityTypeDialog by remember { mutableStateOf(false) }
    var showLocationDialog by remember { mutableStateOf(false) }
    var showMapDialog by remember { mutableStateOf(false) }
    var showDeleteConfirmationDialog by remember { mutableStateOf(false) }
    
    LaunchedEffect(activityId) {
        Log.d(TAG, "LaunchedEffect triggered with activityId: $activityId")
        if (activityId != -1L) {
            viewModel.loadActivity(activityId)
        }
    }

    LaunchedEffect(saveResult) {
        Log.d(TAG, "SaveResult changed: $saveResult")
        when (saveResult) {
            is SaveResult.Success -> {
                Log.d(TAG, "Save successful, dismissing dialog")
                onDismiss()
            }
            is SaveResult.Error -> {
                val message = (saveResult as SaveResult.Error).message
                Log.e(TAG, "Save error: $message")
                errorMessage = message
                showErrorDialog = true
            }
            null -> {
                Log.d(TAG, "SaveResult is null")
            }
        }
    }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = {
                showErrorDialog = false
                viewModel.clearSaveResult()
            },
            title = { Text("Error") },
            text = { Text(errorMessage) },
            confirmButton = {
                TextButton(onClick = {
                    showErrorDialog = false
                    viewModel.clearSaveResult()
                }) {
                    Text("OK")
                }
            }
        )
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = if (activityId == -1L) "Add Activity" else "Edit Activity",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1B3252)
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onDismiss) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color(0xFF1B3252)
                            )
                        }
                    },
                    actions = {
                        TextButton(
                            onClick = {
                                Log.d(TAG, "Save button clicked")
                                try {
                                    if (viewModel.saveActivity()) {
                                        Log.d(TAG, "Save validation passed")
                                    } else {
                                        Log.w(TAG, "Save validation failed")
                                    }
                                } catch (e: Exception) {
                                    Log.e(TAG, "Unexpected error during save", e)
                                    errorMessage = "Unexpected error: ${e.message}"
                                    showErrorDialog = true
                                }
                            }
                        ) {
                            Text(
                                text = "Save",
                                color = Color(0xFF1B3252),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                
                // Name Section
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = activity.name,
                        onValueChange = { viewModel.updateName(it) },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Activity Name") },
                        supportingText = if (activity.name.isBlank() && saveResult != null) {
                            { Text("Activity name is required") }
                        } else null,
                        isError = activity.name.isBlank() && saveResult != null,
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_text_snippet),
                                contentDescription = null,
                                tint = Color(0xFF1B3252)
                            )
                        }
                    )
                }

                // Description Section
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = activity.description ?: "",
                        onValueChange = { viewModel.updateDescription(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        label = { Text("Description") },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_text_snippet),
                                contentDescription = null,
                                tint = Color(0xFF1B3252)
                            )
                        }
                    )
                }

                // Activity Type Section
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showActivityTypeDialog = true },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 0.dp
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.List,
                            contentDescription = "Activity Type",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Column {
                            Text(
                                text = "Activity Type",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = activity.type.toString(),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                if (showActivityTypeDialog) {
                    ActivityTypeSelectionDialog(
                        onDismiss = { showActivityTypeDialog = false },
                        onSelect = { type ->
                            viewModel.updateActivityType(type)
                            showActivityTypeDialog = false
                        },
                        currentType = activity.type
                    )
                }

                // Date Section
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showDatePicker = true },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 0.dp
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_calendar),
                            contentDescription = "Date",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Column {
                            Text(
                                text = "Date",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = formatDate(activity.date),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                if (showDatePicker) {
                    DatePickerDialog(
                        onDismiss = { showDatePicker = false },
                        onConfirm = { timestamp ->
                            viewModel.updateDate(timestamp)
                            showDatePicker = false
                        },
                        initialDate = activity.date
                    )
                }

                // Time Section
                var showStartTimePicker by remember { mutableStateOf(false) }
                var showEndTimePicker by remember { mutableStateOf(false) }

                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 0.dp
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_access_time),
                                contentDescription = "Time",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Time",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { showStartTimePicker = true },
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp)
                                ) {
                                    Text(
                                        text = "Start Time",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = String.format(
                                            "%02d:%02d",
                                            activity.startHour,
                                            activity.startMinute
                                        ),
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }

                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { showEndTimePicker = true },
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp)
                                ) {
                                    Text(
                                        text = "End Time",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = String.format(
                                            "%02d:%02d",
                                            activity.endHour,
                                            activity.endMinute
                                        ),
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }

                if (showStartTimePicker) {
                    MaterialTimePickerDialog(
                        title = "Select Start Time",
                        onDismiss = { showStartTimePicker = false },
                        onConfirm = { hour, minute -> 
                            viewModel.updateStartTime(hour, minute)
                            showStartTimePicker = false
                        },
                        initialHour = activity.startHour,
                        initialMinute = activity.startMinute,
                        useKeyboardInput = true
                    )
                }

                if (showEndTimePicker) {
                    MaterialTimePickerDialog(
                        title = "Select End Time",
                        onDismiss = { showEndTimePicker = false },
                        onConfirm = { hour, minute -> 
                            viewModel.updateEndTime(hour, minute)
                            showEndTimePicker = false
                        },
                        initialHour = activity.endHour,
                        initialMinute = activity.endMinute,
                        useKeyboardInput = true
                    )
                }

                // Location Section
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = activity.location ?: "",
                        onValueChange = { viewModel.updateLocation(it) },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Location") },
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = Color(0xFF1B3252)
                            )
                        }
                    )
                }

                if (showLocationDialog) {
                    LocationSelectionDialog(
                        onDismiss = { showLocationDialog = false },
                        onConfirm = { location ->
                            viewModel.updateLocation(location)
                            showLocationDialog = false
                        },
                        currentLocation = activity.location
                    )
                }

                // Map Location Section (if coordinates are available)
                if (activity.latitude != null && activity.longitude != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFF5F5F5)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Map Location",
                                tint = Color(0xFF1B3252)
                            )
                            Column {
                                Text(
                                    text = "Map Location",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF1B3252)
                                )
                                Text(
                                    text = "${activity.latitude}, ${activity.longitude}",
                                    fontSize = 16.sp,
                                    color = Color(0xFF666666)
                                )
                            }
                        }
                    }
                }

                if (showMapDialog) {
                    MapDialog(
                        onDismiss = { showMapDialog = false },
                        location = activity.location
                    )
                }

                if (showDeleteConfirmationDialog) {
                    AlertDialog(
                        onDismissRequest = { showDeleteConfirmationDialog = false },
                        title = { Text("Delete Activity") },
                        text = { Text("Are you sure you want to delete this activity?") },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    viewModel.deleteActivity()
                                    onDismiss()
                                }
                            ) {
                                Text("Delete")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDeleteConfirmationDialog = false }) {
                                Text("Cancel")
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

private fun formatDate(timestamp: Long): String {
    val dateFormat = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())
    return dateFormat.format(Date(timestamp))
} 