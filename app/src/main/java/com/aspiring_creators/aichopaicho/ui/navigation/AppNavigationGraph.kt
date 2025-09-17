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

    if (startDestination == null) {
        CircularProgressIndicator()
        } else {


        NavHost(
            navController = navController,
            startDestination = startDestination!!
        ) {
            composable(Routes.WELCOME_SCREEN) {
                WelcomeScreen(
                    ShowPermissionScreen = {
                        navController.navigate(Routes.PERMISSION_CONTACTS_SCREEN) {
                            launchSingleTop = true
                            popUpTo(Routes.WELCOME_SCREEN) { inclusive = true }
                        }
                    }
                )
            }

            composable(Routes.PERMISSION_CONTACTS_SCREEN) {
                PermissionScreen(
                    ShowDashboardScreen = {
                        navController.navigate(Routes.DASHBOARD_SCREEN) {
                            launchSingleTop = true
                            popUpTo(Routes.PERMISSION_CONTACTS_SCREEN) { inclusive = true }
                        }
                    }
                )
            }

            composable(Routes.DASHBOARD_SCREEN) {
                DashboardScreen()
            }
        }
    }
}
