package com.otto.ui.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.otto.data.model.ClimbingActivity
import org.osmdroid.views.MapView
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker

@Composable
fun ActivityMapView(
    activities: List<ClimbingActivity>,
    onActivityClick: (ClimbingActivity) -> Unit
) {
    val context = LocalContext.current
    var mapView by remember { mutableStateOf<MapView?>(null) }
    
    DisposableEffect(Unit) {
        mapView = MapView(context).apply {
            setMultiTouchControls(true)
            controller.setZoom(12.0)
            
            // Default to Seattle if no activities
            if (activities.isEmpty()) {
                controller.setCenter(GeoPoint(47.6062, -122.3321))
            }
        }
        
        onDispose {
            mapView?.onDetach()
        }
    }
    
    mapView?.let { map ->
        AndroidView(
            factory = { map },
            modifier = Modifier.fillMaxSize(),
            update = { view ->
                // Clear existing markers
                view.overlays.clear()
                
                // Add markers for all activities
                activities.forEach { activity ->
                    val marker = Marker(view).apply {
                        position = GeoPoint(activity.latitude, activity.longitude)
                        title = activity.name
                        snippet = activity.description
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        setOnMarkerClickListener { _, _ ->
                            onActivityClick(activity)
                            true
                        }
                    }
                    view.overlays.add(marker)
                }
                
                // If there are activities, center on the first one
                activities.firstOrNull()?.let { activity ->
                    view.controller.setCenter(GeoPoint(activity.latitude, activity.longitude))
                }
            }
        )
    }
} 