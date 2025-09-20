package com.aspiring_creators.aichopaicho.ui.navigation

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.aspiring_creators.aichopaicho.ui.screens.AddTransactionScreen
import com.aspiring_creators.aichopaicho.ui.screens.ContactListScreen
import com.aspiring_creators.aichopaicho.ui.screens.ContactTransactionScreen
import com.aspiring_creators.aichopaicho.ui.screens.DashboardScreen
import com.aspiring_creators.aichopaicho.ui.screens.PermissionScreen
import com.aspiring_creators.aichopaicho.ui.screens.TransactionDetailScreen
import com.aspiring_creators.aichopaicho.ui.screens.ViewTransactionScreen
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
                    onNavigateToAddTransaction = {
                        navController.navigate(Routes.ADD_TRANSACTION_SCREEN){
                            launchSingleTop = true
                        }
                    },
                    onNavigateToViewTransactions = {
                        navController.navigate(Routes.VIEW_TRANSACTION_SCREEN){
                            launchSingleTop = true
                        }
                    },
                    onNavigateToSettings = {},
                    onNavigateToContactList = {
                        navController.navigate("${Routes.CONTACT_LIST_SCREEN}/$it"){
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(Routes.ADD_TRANSACTION_SCREEN) {
                AddTransactionScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable(Routes.VIEW_TRANSACTION_SCREEN){
                ViewTransactionScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onNavigateToIndividualRecord = {
                        navController.navigate("${Routes.TRANSACTION_DETAIL_SCREEN}/$it"){
                            launchSingleTop = true
                        }
                    },
                    onNavigateToContactList ={
                        navController.navigate("${Routes.CONTACT_TRANSACTION_SCREEN}/$it"){
                            launchSingleTop = true
                        }
                    },
                    onNavigateToContact = {
                        navController.navigate("${Routes.CONTACT_LIST_SCREEN}/"){
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(
                "${Routes.TRANSACTION_DETAIL_SCREEN}/{${Routes.TRANSACTION_ID}}",
                arguments = listOf(
                    navArgument(name = Routes.TRANSACTION_ID){type = NavType.StringType}
                )
            ){
                val transactionId = it.arguments?.getString(Routes.TRANSACTION_ID)
                TransactionDetailScreen(
                    transactionId = transactionId!!,
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
            composable(
                "${Routes.CONTACT_TRANSACTION_SCREEN}/{${Routes.CONTACT_ID}}",
                arguments = listOf(
                    navArgument(name = Routes.CONTACT_ID){type = NavType.StringType}
                )
            ){
                val contactId = it.arguments?.getString(Routes.CONTACT_ID)
                ContactTransactionScreen(
                    contactId = contactId!!,
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onNavigateToRecord = {
                        navController.navigate("${Routes.TRANSACTION_DETAIL_SCREEN}/$it"){
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(
                "${Routes.CONTACT_LIST_SCREEN}/{${Routes.CONTACT_LIST_TYPE}}",
                arguments = listOf(
                    navArgument(name = Routes.CONTACT_LIST_TYPE){type = NavType.StringType}
                )
            ) {
                val type = it.arguments?.getString(Routes.CONTACT_LIST_TYPE)
                ContactListScreen(
                    type = type!!,
                    onContactClicked = {
                        navController.navigate("${Routes.CONTACT_TRANSACTION_SCREEN}/$it"){
                            launchSingleTop = false
                        }
                    },
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }

        }
    }
}