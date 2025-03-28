package com.example.ottov1.ui.addedit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ottov1.data.model.ClimbingActivity
import com.example.ottov1.data.repository.ActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditActivityViewModel @Inject constructor(
    private val repository: ActivityRepository
) : ViewModel() {
    private val _activity = MutableStateFlow(ClimbingActivity())
    val activity: StateFlow<ClimbingActivity> = _activity

    fun loadActivity(id: Long) {
        if (id == -1L) return
        viewModelScope.launch {
            repository.getActivityById(id)?.let { activity ->
                _activity.value = activity
            }
        }
    }

    fun updateName(name: String) {
        _activity.value = _activity.value.copy(name = name)
    }

    fun updateDescription(description: String) {
        _activity.value = _activity.value.copy(description = description)
    }

    fun saveActivity() {
        viewModelScope.launch {
            val currentActivity = _activity.value
            if (currentActivity.id == 0L) {
                repository.insertActivity(currentActivity)
            } else {
                repository.updateActivity(currentActivity)
            }
        }
    }
} 