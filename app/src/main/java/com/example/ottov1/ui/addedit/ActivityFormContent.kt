package com.example.ottov1.ui.addedit

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.example.ottov1.R
import com.example.ottov1.data.local.ActivityEntity
import com.example.ottov1.data.model.ClimbingActivity
import com.example.ottov1.ui.components.ActivityTypeSelectionDialog
import com.example.ottov1.ui.components.DatePickerDialog
import com.example.ottov1.ui.components.LocationSelectionDialog
import com.example.ottov1.ui.components.MapDialog
import com.example.ottov1.ui.components.TimePickerDialog
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ArrowDropDown
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityFormContent(
    modifier: Modifier = Modifier,
    activity: ClimbingActivity,
    isNameError: Boolean,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onActivityTypeChange: (ActivityType) -> Unit,
    onDateChange: (Long) -> Unit,
    onStartTimeChange: (Int, Int) -> Unit,
    onEndTimeChange: (Int, Int) -> Unit,
    onLocationChange: (String) -> Unit,
    onGradeChange: (String) -> Unit,
    availableGrades: List<String>,
    showDatePicker: MutableState<Boolean>,
    showStartTimePicker: MutableState<Boolean>,
    showEndTimePicker: MutableState<Boolean>,
    showLocationDialog: MutableState<Boolean>,
    showMapDialog: MutableState<Boolean>
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_large))
    ) {
        // --- Name Section ---
        OutlinedTextField(
            value = activity.name,
            onValueChange = onNameChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.activity_name_label)) },
            supportingText = if (isNameError) {
                { Text(stringResource(R.string.activity_name_required)) }
            } else null,
            isError = isNameError,
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = stringResource(R.string.content_description_activity_info),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
        )

        // --- Description Section ---
        OutlinedTextField(
            value = activity.description ?: "",
            onValueChange = onDescriptionChange,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = dimensionResource(R.dimen.text_field_min_height)),
            label = { Text(stringResource(R.string.description_label)) },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_text_snippet),
                    contentDescription = stringResource(R.string.content_description_activity_description),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
        )

        // --- Activity Type Section (Dropdown) ---
        var activityTypeExpanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = activityTypeExpanded,
            onExpandedChange = { activityTypeExpanded = !activityTypeExpanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = stringResource(activity.type.stringResId),
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(R.string.activity_type_label)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.List,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = activityTypeExpanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )
            ExposedDropdownMenu(
                expanded = activityTypeExpanded,
                onDismissRequest = { activityTypeExpanded = false }
            ) {
                ActivityType.values().forEach { type ->
                    DropdownMenuItem(
                        text = { Text(stringResource(type.stringResId)) },
                        onClick = {
                            onActivityTypeChange(type)
                            activityTypeExpanded = false
                        }
                    )
                }
            }
        }

        // --- Grade Section ---
        var gradeDropdownExpanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = gradeDropdownExpanded,
            onExpandedChange = { gradeDropdownExpanded = !gradeDropdownExpanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = activity.grade ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(R.string.grade_label)) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = gradeDropdownExpanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )
            ExposedDropdownMenu(
                expanded = gradeDropdownExpanded,
                onDismissRequest = { gradeDropdownExpanded = false }
            ) {
                availableGrades.forEach { grade ->
                    DropdownMenuItem(
                        text = { Text(grade) },
                        onClick = {
                            onGradeChange(grade)
                            gradeDropdownExpanded = false
                        }
                    )
                }
            }
        }

        // --- Date Section ---
        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDatePicker.value = true },
            border = BorderStroke(dimensionResource(R.dimen.card_border_thickness), MaterialTheme.colorScheme.outline),
            shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.spacing_large)),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_large)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_calendar),
                    contentDescription = stringResource(R.string.date_label),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.date_label),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = formatDate(activity.date),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = stringResource(R.string.content_description_navigate_forward),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // --- Time Section ---
        OutlinedCard(
            modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(dimensionResource(R.dimen.card_border_thickness), MaterialTheme.colorScheme.outline),
            shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.spacing_large), vertical = dimensionResource(R.dimen.spacing_medium)),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_large)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_access_time),
                        contentDescription = stringResource(R.string.time_label),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = stringResource(R.string.time_label),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_large))
                ) {
                    // Start Time
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { showStartTimePicker.value = true },
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(dimensionResource(R.dimen.card_internal_padding))
                        ) {
                            Text(
                                text = stringResource(R.string.start_time_label),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(Modifier.height(dimensionResource(R.dimen.spacing_extra_small)))
                            Text(
                                text = formatTime(activity.startHour, activity.startMinute),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    // End Time
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { showEndTimePicker.value = true },
                         colors = CardDefaults.cardColors(
                             containerColor = MaterialTheme.colorScheme.surfaceVariant
                         ),
                        shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(dimensionResource(R.dimen.card_internal_padding))
                        ) {
                            Text(
                                text = stringResource(R.string.end_time_label),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(Modifier.height(dimensionResource(R.dimen.spacing_extra_small)))
                            Text(
                                text = formatTime(activity.endHour, activity.endMinute),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }


        // --- Location Section ---
        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth(),
            border = BorderStroke(dimensionResource(R.dimen.card_border_thickness), MaterialTheme.colorScheme.outline),
            shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.spacing_large)),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_large))
                ) {
                     Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = stringResource(R.string.location_label),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Column {
                        Text(
                            text = stringResource(R.string.location_label),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = activity.location ?: "",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Row {
                    IconButton(onClick = { showLocationDialog.value = true }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = stringResource(R.string.edit_location_button),
                             tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

    }

    // --- Dialogs ---
    if (showDatePicker.value) {
        DatePickerDialog(
            onDismiss = { showDatePicker.value = false },
            onConfirm = { timestamp ->
                onDateChange(timestamp)
                showDatePicker.value = false
            },
            initialDate = activity.date
        )
    }

     if (showStartTimePicker.value) {
        TimePickerDialog(
            onDismiss = { showStartTimePicker.value = false },
            onConfirm = { hour, minute ->
                onStartTimeChange(hour, minute)
                showStartTimePicker.value = false
            },
            initialHour = activity.startHour,
            initialMinute = activity.startMinute
        )
    }

    if (showEndTimePicker.value) {
        TimePickerDialog(
            onDismiss = { showEndTimePicker.value = false },
            onConfirm = { hour, minute ->
                onEndTimeChange(hour, minute)
                showEndTimePicker.value = false
            },
            initialHour = activity.endHour,
            initialMinute = activity.endMinute
        )
    }

     if (showLocationDialog.value) {
        LocationSelectionDialog(
            onDismiss = { showLocationDialog.value = false },
            onConfirm = { location ->
                onLocationChange(location)
                showLocationDialog.value = false
            },
            currentLocation = activity.location
        )
    }

    if (showMapDialog.value) {
        // Assuming MapDialog just needs dismissal logic for now
        MapDialog(
            onDismiss = { showMapDialog.value = false },
            location = activity.location
        )
    }
}

// --- Helper Functions ---

private fun formatDate(timestamp: Long): String {
    return try {
        val instant = Instant.ofEpochMilli(timestamp)
        val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).format(localDateTime)
    } catch (e: Exception) {
        SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault()).format(Date(timestamp))
    }
}

private fun formatTime(hour: Int, minute: Int): String {
    return String.format(Locale.getDefault(), "%02d:%02d", hour, minute)
}

// --- Resource IDs (to be moved to strings.xml) ---
// Example - will move these in the next step
/*
R.string.activity_name_label = "Activity Name"
R.string.activity_name_required = "Activity name is required"
R.string.description_label = "Description"
R.string.activity_type_label = "Activity Type"
R.string.date_label = "Date"
R.string.time_label = "Time"
R.string.start_time_label = "Start Time"
R.string.end_time_label = "End Time"
R.string.location_label = "Location"
R.string.edit_location_button = "Edit Location"
R.string.map_location_label = "Map Location"
*/ 