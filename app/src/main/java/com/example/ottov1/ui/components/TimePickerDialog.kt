package com.example.ottov1.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.ottov1.R
import java.util.Calendar

@Composable
fun TimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: (Int, Int) -> Unit,
    initialHour: Int = Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
    initialMinute: Int = Calendar.getInstance().get(Calendar.MINUTE)
) {
    // Ensure initial values are formatted correctly
    var hour by remember { mutableStateOf(initialHour.toString().padStart(2, '0')) }
    var minute by remember { mutableStateOf(initialMinute.toString().padStart(2, '0')) }
    
    // Combine error state for simplicity
    var hourError by remember { mutableStateOf(false) }
    var minuteError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    val h = hour.toIntOrNull()
                    val m = minute.toIntOrNull()
                    hourError = h == null || h !in 0..23
                    minuteError = m == null || m !in 0..59
                    
                    if (!hourError && !minuteError) {
                        onConfirm(h!!, m!!) // Use non-null assertion after check
                        // onDismiss is handled by parent
                    }
                }
            ) {
                Text(stringResource(R.string.ok_button))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel_button))
            }
        },
        title = { Text(stringResource(R.string.time_label)) }, // Use generic time label for title
        text = {
            Row(
                modifier = Modifier.fillMaxWidth(), // Let Row handle width
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium)), // Use resource
                verticalAlignment = Alignment.CenterVertically // Align items vertically
            ) {
                OutlinedTextField(
                    value = hour,
                    onValueChange = { value ->
                        // Allow only up to 2 digits
                        if (value.length <= 2) {
                            hour = value.filter { it.isDigit() }
                            hourError = false // Reset error on change
                        }
                    },
                    label = { Text(stringResource(R.string.time_picker_hour_label)) },
                    supportingText = if (hourError) {
                        { Text(stringResource(R.string.time_picker_hour_error)) }
                    } else null,
                    isError = hourError,
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword), // NumberPassword hides suggestions
                    textStyle = LocalTextStyle.current.copy(textAlign = androidx.compose.ui.text.style.TextAlign.Center),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )
                Text(
                    stringResource(R.string.time_separator), // Use resource
                    style = MaterialTheme.typography.headlineMedium
                )
                
                OutlinedTextField(
                    value = minute,
                    onValueChange = { value ->
                        if (value.length <= 2) {
                            minute = value.filter { it.isDigit() }
                            minuteError = false // Reset error on change
                        }
                    },
                    label = { Text(stringResource(R.string.time_picker_minute_label)) },
                    supportingText = if (minuteError) {
                        { Text(stringResource(R.string.time_picker_minute_error)) }
                    } else null,
                    isError = minuteError,
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    textStyle = LocalTextStyle.current.copy(textAlign = androidx.compose.ui.text.style.TextAlign.Center),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )
            }
        }
    )
} 