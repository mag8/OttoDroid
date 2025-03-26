package com.otto.ui.activitylist

import androidx.lifecycle.ViewModel
import com.otto.data.model.ClimbingActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@HiltViewModel
class ActivityListViewModel @Inject constructor(
    // TODO: Inject repository
) : ViewModel() {
    val activities: Flow<List<ClimbingActivity>> = flowOf(emptyList())
} 