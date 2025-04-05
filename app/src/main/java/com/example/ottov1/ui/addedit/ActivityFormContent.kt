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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.example.ottov1.R
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
// import androidx.compose.material.icons.filled.People
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.math.roundToInt
import com.example.ottov1.util.Constants

// Constants for People Slider
private const val MIN_PEOPLE = 1
private const val MAX_PEOPLE = 8
private const val PEOPLE_SLIDER_STEPS = MAX_PEOPLE - MIN_PEOPLE - 1 // = 6

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
    onPeopleChange: (min: Int, max: Int) -> Unit,
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
            modifier = Modifier
                .fillMaxWidth()
                .testTag("name_field"),
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
                .heightIn(min = dimensionResource(R.dimen.text_field_min_height))
                .testTag("description_field"),
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
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .testTag("activity_type_field"),
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
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .testTag("grade_field"),
                label = { Text(stringResource(R.string.grade_label)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = gradeDropdownExpanded)
                },
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

        // --- People Range Slider Section ---
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.people_label),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = dimensionResource(R.dimen.spacing_small))
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
            ) {
                // Remove Icon usage
                /* Icon(
                    imageVector = Icons.Default.People,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                ) */
                var sliderPositions by remember {
                    // Ensure initial values are within the valid range
                    val initialMin = activity.minPeople.toFloat().coerceIn(MIN_PEOPLE.toFloat(), MAX_PEOPLE.toFloat())
                    val initialMax = activity.maxPeople.toFloat().coerceIn(initialMin, MAX_PEOPLE.toFloat())
                    mutableStateOf(initialMin..initialMax)
                }

                RangeSlider(
                    value = sliderPositions,
                    onValueChange = { range -> sliderPositions = range },
                    valueRange = MIN_PEOPLE.toFloat()..MAX_PEOPLE.toFloat(), // Use constants
                    steps = PEOPLE_SLIDER_STEPS, // Use constant
                    onValueChangeFinished = {
                        val min = sliderPositions.start.roundToInt().coerceIn(MIN_PEOPLE, MAX_PEOPLE)
                        val max = sliderPositions.endInclusive.roundToInt().coerceIn(min, MAX_PEOPLE)
                        onPeopleChange(min, max)
                    },
                    modifier = Modifier.weight(1f)
                )
                Text(
                    // Use formatted string resource
                    text = stringResource(
                        R.string.people_range_display,
                        sliderPositions.start.roundToInt(),
                        sliderPositions.endInclusive.roundToInt()
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        // --- Date Section ---
        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("date_field"),
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
                    .padding(dimensionResource(R.dimen.spacing_large))
            ) {
                Text(
                    text = stringResource(R.string.time_label),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_large))
                ) {
                    // Start Time
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { showStartTimePicker.value = true }
                            .testTag("start_time_field"),
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
                            .clickable { showEndTimePicker.value = true }
                            .testTag("end_time_field"),
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
                .fillMaxWidth()
                .testTag("location_field"),
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
        MapDialog(
            onDismiss = { showMapDialog.value = false },
            location = activity.location
        )
    }
}

// --- Helper Functions ---

// Shared date formatter (consistent with list view)
private fun formatDate(timestamp: Long): String {
    val instant = Instant.ofEpochMilli(timestamp)
    val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
    return SimpleDateFormat(Constants.DATE_FORMAT_LONG, Locale.getDefault())
        .format(Date(timestamp))
}

// Use locale-aware time formatting
private fun formatTime(hour: Int, minute: Int): String {
    val localTime = LocalDateTime.now().withHour(hour).withMinute(minute) // Use LocalDateTime to format
    return DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).format(localTime)
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