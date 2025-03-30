package com.example.ottov1.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.ottov1.R
import java.text.SimpleDateFormat // Keep for helper
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: (Long) -> Unit,
    initialDate: Long
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDate,
        // Set initial display mode if needed, e.g.,
        // initialDisplayMode = DisplayMode.Picker
    )

    androidx.compose.material3.DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
             TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let {
                        onConfirm(it) // Pass selected date
                    }
                    // onDismiss is called implicitly by DatePickerDialog
                },
                enabled = datePickerState.selectedDateMillis != null // Enable only when a date is selected
            ) {
                Text(stringResource(R.string.ok_button))
            }
        },
        dismissButton = {
             TextButton(onClick = onDismiss) {
                 Text(stringResource(R.string.cancel_button))
             }
        }
        // Properties can be set here if needed, like usePlatformDefaultWidth
    ) {
        DatePicker(
            state = datePickerState,
            // showModeToggle = true, // Keep default behavior (usually true)
            title = {
                Text(
                    text = stringResource(R.string.select_date_title),
                    modifier = Modifier.padding(start = 24.dp, end = 12.dp, top = 16.dp),
                    style = MaterialTheme.typography.titleMedium
                )
            },
            headline = {
                // Use state's formatter if available, otherwise fallback
                val formatter = remember { SimpleDateFormat("MMM d, yyyy", Locale.getDefault()) }
                val selectedDateText = datePickerState.selectedDateMillis?.let { formatter.format(Date(it)) } ?: ""
                Text(
                    text = selectedDateText,
                    modifier = Modifier.padding(start = 24.dp, end = 12.dp, bottom = 12.dp),
                    style = MaterialTheme.typography.headlineMedium
                )
            }
            // Colors can be customized further using DatePickerDefaults.colors()
            // Default colors usually adapt well to the theme
        )
    }
}

private fun formatDate(timestamp: Long): String {
    val dateFormat = java.text.SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())
    return dateFormat.format(Date(timestamp))
} 