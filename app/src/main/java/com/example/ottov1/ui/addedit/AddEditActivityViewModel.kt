package com.example.ottov1.ui.addedit

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ottov1.data.model.ClimbingActivity
import com.example.ottov1.data.repository.ActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "AddEditActivityVM"

sealed class SaveResult {
    object Success : SaveResult()
    data class Error(val message: String) : SaveResult()
}

@HiltViewModel
class AddEditActivityViewModel @Inject constructor(
    private val repository: ActivityRepository
) : ViewModel() {
    private val _activity = MutableStateFlow(ClimbingActivity())
    val activity: StateFlow<ClimbingActivity> = _activity

    private val _saveResult = MutableStateFlow<SaveResult?>(null)
    val saveResult: StateFlow<SaveResult?> = _saveResult

    fun loadActivity(id: Long) {
        Log.d(TAG, "Loading activity with id: $id")
        if (id == -1L) {
            Log.d(TAG, "New activity, skipping load")
            return
        }
        viewModelScope.launch {
            try {
                repository.getActivityById(id)?.let { activity ->
                    Log.d(TAG, "Activity loaded: $activity")
                    _activity.value = activity
                } ?: run {
                    Log.e(TAG, "Activity not found for id: $id")
                    _saveResult.value = SaveResult.Error("Activity not found")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading activity: ${e.message}", e)
                _saveResult.value = SaveResult.Error("Failed to load activity: ${e.message}")
            }
        }
    }

    fun updateName(name: String) {
        Log.d(TAG, "Updating name to: $name")
        _activity.value = _activity.value.copy(name = name)
    }

    fun updateDescription(description: String) {
        Log.d(TAG, "Updating description to: $description")
        _activity.value = _activity.value.copy(description = description)
    }

    fun saveActivity(): Boolean {
        val currentActivity = _activity.value
        Log.d(TAG, "Attempting to save activity: $currentActivity")
        
        // Validate input
        if (currentActivity.name.isBlank()) {
            Log.w(TAG, "Save failed: Activity name is blank")
            _saveResult.value = SaveResult.Error("Activity name cannot be empty")
            return false
        }

        viewModelScope.launch {
            try {
                if (currentActivity.id == 0L) {
                    Log.d(TAG, "Inserting new activity")
                    val newId = repository.insertActivity(currentActivity)
                    Log.d(TAG, "New activity inserted with id: $newId")
                } else {
                    Log.d(TAG, "Updating existing activity with id: ${currentActivity.id}")
                    repository.updateActivity(currentActivity)
                    Log.d(TAG, "Activity updated successfully")
                }
                _saveResult.value = SaveResult.Success
            } catch (e: Exception) {
                Log.e(TAG, "Error saving activity: ${e.message}", e)
                _saveResult.value = SaveResult.Error("Failed to save activity: ${e.message}")
            }
        }
        return true
    }

    fun clearSaveResult() {
        Log.d(TAG, "Clearing save result")
        _saveResult.value = null
    }
} 