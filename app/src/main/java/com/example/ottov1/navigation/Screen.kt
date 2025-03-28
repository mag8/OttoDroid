package com.example.ottov1.navigation

sealed class Screen(val route: String) {
    object ActivityList : Screen("activity_list")
    object ActivityMap : Screen("activity_map")
    object AddEditActivity : Screen("add_edit_activity/{activityId}") {
        fun createRoute(activityId: Long = -1L) = "add_edit_activity/$activityId"
    }
} 