package com.example.ottov1.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun MapDialog(
    onDismiss: () -> Unit,
    location: String?
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Location Map") },
        text = {
            // TODO: Implement map view here
            Text("Map view will be implemented here for location: $location")
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
} 