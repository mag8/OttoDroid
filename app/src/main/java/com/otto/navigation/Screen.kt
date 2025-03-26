package com.otto.navigation

sealed class Screen(val route: String) {
    object ActivityList : Screen("activity_list")
    object ActivityMap : Screen("activity_map")
    object AddEditActivity : Screen("activity/{activityId}") {
        fun createRoute(activityId: Long = -1L) = "activity/$activityId"
    }
} 