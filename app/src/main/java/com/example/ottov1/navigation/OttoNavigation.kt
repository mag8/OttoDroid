package com.example.ottov1.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.compose.material3.FabPosition
import com.example.ottov1.R
import com.example.ottov1.ui.activitylist.ActivityListScreen
import com.example.ottov1.ui.addedit.AddEditActivityDialog
import com.example.ottov1.ui.map.ActivityMapScreen

internal const val NEW_ACTIVITY_ID = -1L

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OttoNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) }
    var showAddEditDialog by remember { mutableStateOf(false) }
    var editActivityId by remember { mutableStateOf(NEW_ACTIVITY_ID) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    editActivityId = NEW_ACTIVITY_ID
                    showAddEditDialog = true
                },
                containerColor = MaterialTheme.colorScheme.primary,
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = stringResource(R.string.nav_add_description),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 0.dp
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Home, contentDescription = stringResource(R.string.nav_home_description)) },
                    selected = selectedTab == 0,
                    onClick = {
                        selectedTab = 0
                        navController.navigate(Screen.ActivityList.route) {
                            popUpTo(Screen.ActivityList.route) { inclusive = true }
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                NavigationBarItem(
                    icon = { Icon(painterResource(id = R.drawable.ic_landscape_24), contentDescription = stringResource(R.string.nav_activities_description)) },
                    selected = selectedTab == 1,
                    onClick = {
                        selectedTab = 1
                        navController.navigate(Screen.ActivityMap.route) {
                            popUpTo(Screen.ActivityList.route)
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Notifications, contentDescription = stringResource(R.string.nav_notifications_description)) },
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Person, contentDescription = stringResource(R.string.nav_profile_description)) },
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.ActivityList.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.ActivityList.route) {
                ActivityListScreen(
                    onNavigateToMap = {
                        selectedTab = 1
                        navController.navigate(Screen.ActivityMap.route) {
                            popUpTo(Screen.ActivityList.route)
                        }
                    },
                    onNavigateToActivity = { activityId ->
                        editActivityId = activityId
                        showAddEditDialog = true
                    }
                )
            }

            composable(Screen.ActivityMap.route) {
                ActivityMapScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onActivityClick = { activityId ->
                        editActivityId = activityId
                        showAddEditDialog = true
                    }
                )
            }
        }

        if (showAddEditDialog) {
            AddEditActivityDialog(
                activityId = editActivityId,
                onDismiss = {
                    showAddEditDialog = false
                    editActivityId = NEW_ACTIVITY_ID
                }
            )
        }
    }
} 