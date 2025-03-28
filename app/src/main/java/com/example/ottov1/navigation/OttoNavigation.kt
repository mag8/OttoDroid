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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.ottov1.R
import com.example.ottov1.ui.activitylist.ActivityListScreen
import com.example.ottov1.ui.addedit.AddEditActivityDialog
import com.example.ottov1.ui.map.ActivityMapScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OttoNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) }
    var showAddEditDialog by remember { mutableStateOf(false) }
    var editActivityId by remember { mutableStateOf(-1L) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 0.dp
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Home, "Home") },
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
                    icon = { Icon(painterResource(id = R.drawable.ic_landscape_24), "Activities") },
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
                    icon = { 
                        FloatingActionButton(
                            onClick = { 
                                editActivityId = -1L
                                showAddEditDialog = true
                            },
                            containerColor = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Add",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    },
                    selected = false,
                    onClick = { 
                        editActivityId = -1L
                        showAddEditDialog = true
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Notifications, "Notifications") },
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Person, "Profile") },
                    selected = selectedTab == 4,
                    onClick = { selectedTab = 4 },
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
                    editActivityId = -1L
                }
            )
        }
    }
} 