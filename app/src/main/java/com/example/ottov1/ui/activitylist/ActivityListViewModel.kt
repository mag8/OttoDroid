package com.example.ottov1.ui.activitylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ottov1.data.model.ClimbingActivity
import com.example.ottov1.data.repository.ActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ActivityListViewModel @Inject constructor(
    repository: ActivityRepository
) : ViewModel() {
    val activities: StateFlow<List<ClimbingActivity>> = repository.getAllActivities()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
} 