package com.example.ottov1.navigation

sealed class Screen(val route: String) {
    object ActivityList : Screen("activity_list")
    object ActivityMap : Screen("activity_map")
} 