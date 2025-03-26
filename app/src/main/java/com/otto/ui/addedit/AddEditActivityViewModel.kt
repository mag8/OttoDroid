package com.otto.ui.addedit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.otto.data.model.ClimbingActivity
import com.otto.data.repository.ActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddEditActivityUiState(
    val id: Long = -1L,
    val name: String = "",
    val description: String = "",
    val latitude: Double = 37.7749, // San Francisco
    val longitude: Double = -122.4194
)

@HiltViewModel
class AddEditActivityViewModel @Inject constructor(
    private val repository: ActivityRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEditActivityUiState())
    val uiState: StateFlow<AddEditActivityUiState> = _uiState.asStateFlow()

    fun loadActivity(activityId: Long) {
        if (activityId == -1L) return
        
        viewModelScope.launch {
            repository.getActivityById(activityId)?.let { activity ->
                _uiState.value = AddEditActivityUiState(
                    id = activity.id,
                    name = activity.name,
                    description = activity.description ?: "",
                    latitude = activity.latitude,
                    longitude = activity.longitude
                )
            }
        }
    }

    fun updateName(name: String) {
        _uiState.value = _uiState.value.copy(name = name)
    }

    fun updateDescription(description: String) {
        _uiState.value = _uiState.value.copy(description = description)
    }

    fun updateLocation(latitude: Double, longitude: Double) {
        _uiState.value = _uiState.value.copy(
            latitude = latitude,
            longitude = longitude
        )
    }

    fun saveActivity() {
        val activity = ClimbingActivity(
            id = _uiState.value.id,
            name = _uiState.value.name,
            description = _uiState.value.description,
            latitude = _uiState.value.latitude,
            longitude = _uiState.value.longitude,
            date = System.currentTimeMillis()
        )

        viewModelScope.launch {
            if (activity.id == -1L) {
                repository.insertActivity(activity)
            } else {
                repository.updateActivity(activity)
            }
        }
    }
} 