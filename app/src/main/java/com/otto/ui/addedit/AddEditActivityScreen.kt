package com.otto.ui.addedit

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import org.osmdroid.views.MapView
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditActivityScreen(
    activityId: Long,
    onNavigateBack: () -> Unit,
    viewModel: AddEditActivityViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    var showSaveConfirmation by remember { mutableStateOf(false) }
    var mapView by remember { mutableStateOf<MapView?>(null) }

    LaunchedEffect(activityId) {
        viewModel.loadActivity(activityId)
    }

    DisposableEffect(Unit) {
        mapView = MapView(context).apply {
            setMultiTouchControls(true)
            controller.setZoom(15.0)
            controller.setCenter(GeoPoint(47.6062, -122.3321)) // Default to Seattle
        }
        
        onDispose {
            mapView?.onDetach()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (activityId == -1L) "Add Activity" else "Edit Activity") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.saveActivity()
                        showSaveConfirmation = true
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = "Save Activity"
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                mapView?.let { map ->
                    AndroidView(
                        factory = { map },
                        modifier = Modifier.fillMaxSize()
                    )
                    
                    // Add marker for selected location
                    val marker = Marker(map).apply {
                        position = GeoPoint(uiState.latitude, uiState.longitude)
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    }
                    map.overlays.add(marker)
                }
            }

            OutlinedTextField(
                value = uiState.name,
                onValueChange = { viewModel.updateName(it) },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.description,
                onValueChange = { viewModel.updateDescription(it) },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5
            )
        }
    }

    if (showSaveConfirmation) {
        AlertDialog(
            onDismissRequest = {
                showSaveConfirmation = false
                onNavigateBack()
            },
            title = { Text("Success") },
            text = { Text("Activity saved successfully") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showSaveConfirmation = false
                        onNavigateBack()
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }
} 