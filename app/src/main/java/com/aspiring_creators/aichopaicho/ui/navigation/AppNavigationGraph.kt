package com.aspiring_creators.aichopaicho.ui.navigation

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aspiring_creators.aichopaicho.ui.screens.DashboardScreen
import com.aspiring_creators.aichopaicho.ui.screens.PermissionScreen
import com.aspiring_creators.aichopaicho.ui.screens.WelcomeScreen
import com.aspiring_creators.aichopaicho.viewmodel.AppNavigationViewModel

@Composable
fun AppNavigationGraph(
    appNavigationViewModel: AppNavigationViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val startDestination by appNavigationViewModel.startDestination.collectAsState()

    // Show loading while determining start destination
    if (startDestination == null) {
        CircularProgressIndicator()
    } else {
        NavHost(
            navController = navController,
            startDestination = startDestination!!
        ) {
            composable(Routes.WELCOME_SCREEN) {
                WelcomeScreen(
                    onNavigateToPermissions = {
                        navController.navigate(Routes.PERMISSION_CONTACTS_SCREEN) {
                            popUpTo(Routes.WELCOME_SCREEN) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(Routes.PERMISSION_CONTACTS_SCREEN) {
                PermissionScreen(
                    onNavigateToDashboard = {
                        navController.navigate(Routes.DASHBOARD_SCREEN) {
                            popUpTo(Routes.PERMISSION_CONTACTS_SCREEN) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable(Routes.DASHBOARD_SCREEN) {
                DashboardScreen(
                    onSignOut = {
                        navController.navigate(Routes.WELCOME_SCREEN) {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onNavigateToProfile = {
                        // Add when you have profile screen
                        // navController.navigate(Routes.PROFILE_SCREEN)
                    },
                    onNavigateToTransactions = {
                        // Add when you have transactions screen
                        // navController.navigate(Routes.TRANSACTIONS_SCREEN)
                    }
                )
            }
        }
    }
}