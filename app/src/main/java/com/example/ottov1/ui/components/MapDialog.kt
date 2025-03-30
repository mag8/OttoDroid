package com.example.ottov1.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.ottov1.R

@Composable
fun MapDialog(
    onDismiss: () -> Unit,
    location: String? // Keep location if needed for future map implementation
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(Icons.Filled.Place, contentDescription = null) },
        title = { Text(stringResource(R.string.location_map_title)) },
        text = {
            // TODO: Implement actual map view here using Google Maps SDK or similar
            Text(stringResource(R.string.map_placeholder_text, location ?: "Unknown"))
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.close_button))
            }
        }
    )
}

// Add a placeholder string resource in strings.xml:
// <string name="map_placeholder_text">Map view for location: %1$s (Implementation Pending)</string> 