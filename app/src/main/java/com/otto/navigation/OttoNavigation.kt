package com.otto.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.otto.ui.activitylist.ActivityListScreen
import com.otto.ui.addedit.AddEditActivityScreen
import com.otto.ui.map.ActivityMapScreen

@Composable
fun OttoNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.ActivityList.route,
        modifier = modifier
    ) {
        composable(Screen.ActivityList.route) {
            ActivityListScreen(
                onNavigateToMap = {
                    navController.navigate(Screen.ActivityMap.route)
                },
                onNavigateToActivity = { activityId ->
                    navController.navigate("${Screen.AddEditActivity.route}/$activityId")
                }
            )
        }

        composable(Screen.ActivityMap.route) {
            ActivityMapScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onActivityClick = { activityId ->
                    navController.navigate("${Screen.AddEditActivity.route}/$activityId")
                }
            )
        }

        composable(
            route = "${Screen.AddEditActivity.route}/{activityId}",
            arguments = listOf(
                navArgument("activityId") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val activityId = backStackEntry.arguments?.getLong("activityId") ?: -1L
            AddEditActivityScreen(
                activityId = activityId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
} 