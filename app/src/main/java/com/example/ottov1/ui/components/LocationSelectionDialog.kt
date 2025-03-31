package com.example.ottov1.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LocationSelectionDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
    currentLocation: String?
) {
    var location by remember { mutableStateOf(currentLocation ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Enter Location") },
        text = {
            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Location") },
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(location)
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
} 