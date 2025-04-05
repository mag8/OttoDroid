package com.example.ottov1.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.ottov1.R

@Composable
fun LocationSelectionDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
    currentLocation: String?
) {
    var location by remember { mutableStateOf(currentLocation ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.enter_location_title)) },
        text = {
            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.location_label)) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedContainerColor = Color(0xFFE8F5E9),
                    unfocusedContainerColor = Color(0xFFE8F5E9)
                ),
                trailingIcon = {
                    IconButton(onClick = {
                        // TODO: Implement location search functionality
                    }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search location"
                        )
                    }
                }
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(location)
                }
            ) {
                Text(stringResource(R.string.ok_button))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel_button))
            }
        }
    )
} 