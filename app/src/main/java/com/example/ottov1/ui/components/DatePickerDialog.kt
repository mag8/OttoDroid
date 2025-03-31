package com.example.ottov1.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: (Long) -> Unit,
    initialDate: Long
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDate
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.fillMaxWidth(),
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                DatePicker(
                    state = datePickerState,
                    showModeToggle = true,
                    title = {
                        Text(
                            text = "Select date",
                            modifier = Modifier.padding(start = 24.dp, end = 12.dp, top = 16.dp),
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    headline = {
                        if (datePickerState.selectedDateMillis != null) {
                            Text(
                                text = formatDate(datePickerState.selectedDateMillis!!),
                                modifier = Modifier.padding(start = 24.dp, end = 12.dp, bottom = 12.dp),
                                style = MaterialTheme.typography.headlineMedium
                            )
                        }
                    },
                    colors = DatePickerDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        headlineContentColor = MaterialTheme.colorScheme.onSurface,
                        weekdayContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        subheadContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        yearContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        currentYearContentColor = MaterialTheme.colorScheme.primary,
                        selectedYearContentColor = MaterialTheme.colorScheme.onPrimary,
                        selectedYearContainerColor = MaterialTheme.colorScheme.primary,
                        dayContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        selectedDayContentColor = MaterialTheme.colorScheme.onPrimary,
                        selectedDayContainerColor = MaterialTheme.colorScheme.primary,
                        todayContentColor = MaterialTheme.colorScheme.primary,
                        todayDateBorderColor = MaterialTheme.colorScheme.primary
                    )
                )
                
                // Buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onDismiss
                    ) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(
                        onClick = {
                            datePickerState.selectedDateMillis?.let { timestamp ->
                                onConfirm(timestamp)
                            }
                            onDismiss()
                        }
                    ) {
                        Text("OK")
                    }
                }
            }
        }
    }
}

private fun formatDate(timestamp: Long): String {
    val dateFormat = java.text.SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())
    return dateFormat.format(Date(timestamp))
} 