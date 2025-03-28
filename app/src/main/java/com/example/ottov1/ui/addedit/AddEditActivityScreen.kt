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
    var showDatePicker by remember { mutableStateOf(false) }
    
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
                Log.d(TAG, "Save successful, navigating back")
                onNavigateBack()
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

    Scaffold(
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
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF1B3252)
                        )
                    }
                },
                actions = {
                    IconButton(
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
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Save",
                            tint = Color(0xFF1B3252)
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
            var showTypeDialog by remember { mutableStateOf(false) }
            
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showTypeDialog = true },
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
                        imageVector = Icons.Default.List,
                        contentDescription = "Activity Type",
                        tint = Color(0xFF1B3252)
                    )
                    Column {
                        Text(
                            text = "Activity Type",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF1B3252)
                        )
                        Text(
                            text = activity.type.toString(),
                            fontSize = 16.sp,
                            color = Color(0xFF666666)
                        )
                    }
                }
            }

            if (showTypeDialog) {
                AlertDialog(
                    onDismissRequest = { showTypeDialog = false },
                    title = { Text("Select Activity Type") },
                    text = {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            ActivityType.values().forEach { type ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            viewModel.updateActivityType(type)
                                            showTypeDialog = false
                                        }
                                        .padding(vertical = 12.dp),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = activity.type == type,
                                        onClick = {
                                            viewModel.updateActivityType(type)
                                            showTypeDialog = false
                                        }
                                    )
                                    Text(
                                        text = type.toString(),
                                        fontSize = 16.sp,
                                        color = Color(0xFF1B3252)
                                    )
                                }
                            }
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = { showTypeDialog = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }

            // Date Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true },
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
                        painter = painterResource(id = R.drawable.ic_calendar),
                        contentDescription = "Date",
                        tint = Color(0xFF1B3252)
                    )
                    Column {
                        Text(
                            text = "Date",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF1B3252)
                        )
                        Text(
                            text = formatDate(activity.date),
                            fontSize = 16.sp,
                            color = Color(0xFF666666)
                        )
                    }
                }
            }

            if (showDatePicker) {
                val datePickerState = rememberDatePickerState(
                    initialSelectedDateMillis = activity.date
                )
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            datePickerState.selectedDateMillis?.let { timestamp ->
                                viewModel.updateDate(timestamp)
                            }
                            showDatePicker = false
                        }) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker = false }) {
                            Text("Cancel")
                        }
                    }
                ) {
                    DatePicker(
                        state = datePickerState,
                        showModeToggle = false
                    )
                }
            }

            // Time Section
            var showStartTimePicker by remember { mutableStateOf(false) }
            var showEndTimePicker by remember { mutableStateOf(false) }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF5F5F5)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Time",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1B3252)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { showStartTimePicker = true },
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = "Start Time",
                                    fontSize = 12.sp,
                                    color = Color(0xFF666666)
                                )
                                Text(
                                    text = String.format(
                                        "%02d:%02d",
                                        activity.startHour,
                                        activity.startMinute
                                    ),
                                    fontSize = 16.sp,
                                    color = Color(0xFF1B3252),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { showEndTimePicker = true },
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = "End Time",
                                    fontSize = 12.sp,
                                    color = Color(0xFF666666)
                                )
                                Text(
                                    text = String.format(
                                        "%02d:%02d",
                                        activity.endHour,
                                        activity.endMinute
                                    ),
                                    fontSize = 16.sp,
                                    color = Color(0xFF1B3252),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            if (showStartTimePicker) {
                TimePickerDialog(
                    onDismiss = { showStartTimePicker = false },
                    onConfirm = { hour, minute -> 
                        viewModel.updateStartTime(hour, minute)
                        showStartTimePicker = false
                    },
                    initialHour = activity.startHour,
                    initialMinute = activity.startMinute
                )
            }

            if (showEndTimePicker) {
                TimePickerDialog(
                    onDismiss = { showEndTimePicker = false },
                    onConfirm = { hour, minute -> 
                        viewModel.updateEndTime(hour, minute)
                        showEndTimePicker = false
                    },
                    initialHour = activity.endHour,
                    initialMinute = activity.endMinute
                )
            }

            // Location Section
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
                            contentDescription = "Location",
                            tint = Color(0xFF1B3252)
                        )
                        Column {
                            Text(
                                text = "Location",
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

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

private fun formatDate(timestamp: Long): String {
    val dateFormat = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())
    return dateFormat.format(Date(timestamp))
}