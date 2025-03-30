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
import androidx.annotation.StringRes
import com.example.ottov1.R
import com.example.ottov1.util.StringProvider

private const val TAG = "AddEditActivityVM"
private const val NEW_ACTIVITY_ID = -1L

enum class ActivityType(@StringRes val stringResId: Int) {
    SPORT(R.string.activity_type_sport),
    TRAD(R.string.activity_type_trad),
    BOULDER(R.string.activity_type_boulder),
    MULTI_PITCH(R.string.activity_type_multi_pitch),
    CLIMBING_GYM(R.string.activity_type_climbing_gym)
}

fun ActivityType.getString(provider: StringProvider): String {
    return provider.getString(this.stringResId)
}

sealed class SaveResult {
    object Success : SaveResult()
    data class Error(val message: String) : SaveResult()
}

@HiltViewModel
class AddEditActivityViewModel @Inject constructor(
    private val repository: ActivityRepository,
    private val stringProvider: StringProvider
) : ViewModel() {
    private val _activity = MutableStateFlow(ClimbingActivity())
    val activity: StateFlow<ClimbingActivity> = _activity

    private val _saveResult = MutableStateFlow<SaveResult?>(null)
    val saveResult: StateFlow<SaveResult?> = _saveResult

    fun loadActivity(id: Long) {
        Log.d(TAG, "Loading activity with id: $id")
        if (id == NEW_ACTIVITY_ID) {
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
                    _saveResult.value = SaveResult.Error(stringProvider.getString(R.string.error_activity_not_found))
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading activity: ${e.message}", e)
                _saveResult.value = SaveResult.Error(
                    stringProvider.getString(R.string.error_loading_activity, e.message ?: "Unknown error")
                )
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

    fun updateDate(timestamp: Long) {
        Log.d(TAG, "Updating date to: $timestamp")
        _activity.value = _activity.value.copy(date = timestamp)
    }

    fun updateStartTime(hour: Int, minute: Int) {
        Log.d(TAG, "Updating start time to: $hour:$minute")
        _activity.value = _activity.value.copy(startHour = hour, startMinute = minute)
    }

    fun updateEndTime(hour: Int, minute: Int) {
        Log.d(TAG, "Updating end time to: $hour:$minute")
        _activity.value = _activity.value.copy(endHour = hour, endMinute = minute)
    }

    fun updateActivityType(type: ActivityType) {
        Log.d(TAG, "Updating activity type to: ${type.getString(stringProvider)}")
        _activity.value = _activity.value.copy(type = type)
    }

    fun updateLocation(location: String) {
        Log.d(TAG, "Updating location to: $location")
        _activity.value = _activity.value.copy(location = location)
    }

    fun saveActivity(): Boolean {
        val currentActivity = _activity.value
        Log.d(TAG, "Attempting to save activity: $currentActivity")
        
        if (currentActivity.name.isBlank()) {
            Log.w(TAG, "Save failed: Activity name is blank")
            _saveResult.value = SaveResult.Error(stringProvider.getString(R.string.activity_name_required))
            return false
        }

        viewModelScope.launch {
            try {
                if (currentActivity.id == 0L) {
                    Log.d(TAG, "Inserting new activity")
                    repository.insertActivity(currentActivity)
                    Log.d(TAG, "New activity inserted")
                } else {
                    Log.d(TAG, "Updating existing activity with id: ${currentActivity.id}")
                    repository.updateActivity(currentActivity)
                    Log.d(TAG, "Activity updated successfully")
                }
                _saveResult.value = SaveResult.Success
            } catch (e: Exception) {
                Log.e(TAG, "Error saving activity: ${e.message}", e)
                _saveResult.value = SaveResult.Error(
                    stringProvider.getString(R.string.error_saving_activity, e.message ?: "Unknown error")
                )
            }
        }
        return true
    }

    fun clearSaveResult() {
        Log.d(TAG, "Clearing save result")
        _saveResult.value = null
    }

    fun deleteActivity() {
        val activityToDelete = activity.value
        if (activityToDelete.id == 0L) {
             Log.w(TAG, "Attempted to delete unsaved activity")
             return
        }
        viewModelScope.launch {
            try {
                Log.d(TAG, "Deleting activity with id: ${activityToDelete.id}")
                repository.deleteActivity(activityToDelete.id)
                Log.d(TAG, "Activity deleted successfully")
                _saveResult.value = SaveResult.Success
            } catch (e: Exception) {
                 Log.e(TAG, "Error deleting activity: ${e.message}", e)
                _saveResult.value = SaveResult.Error(
                    stringProvider.getString(R.string.error_deleting_activity, e.message ?: "Unknown error")
                )
            }
        }
    }

    fun resetActivity() {
        _activity.value = ClimbingActivity()
        Log.d(TAG, "Activity state reset")
    }
} 