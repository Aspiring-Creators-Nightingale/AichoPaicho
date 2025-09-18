package com.aspiring_creators.aichopaicho.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.aspiring_creators.aichopaicho.R
import com.aspiring_creators.aichopaicho.ui.component.DashboardContent
import com.aspiring_creators.aichopaicho.ui.component.ErrorContent
import com.aspiring_creators.aichopaicho.ui.component.LoadingContent
import com.aspiring_creators.aichopaicho.ui.component.NotSignedInContent
import com.aspiring_creators.aichopaicho.viewmodel.DashboardScreenViewModel
import kotlinx.coroutines.launch

@Composable
fun DashboardScreen(
    onSignOut: (() -> Unit)? = null,
    onNavigateToAddTransaction: (() -> Unit)?,
    onNavigateToViewTransactions: (() -> Unit)?,
    onNavigateToSettings: (() -> Unit)?,
    dashboardScreenViewModel: DashboardScreenViewModel = hiltViewModel()
) {
    val uiState by dashboardScreenViewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle sign out navigation
    LaunchedEffect(uiState.isSignedIn) {
        if (!uiState.isSignedIn && uiState.user == null && !uiState.isLoading) {
            // User has been signed out
            onSignOut?.invoke()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = colorResource(R.color.appThemeColor)
        ) {
            when {
                uiState.isLoading -> {
                    LoadingContent(text = "Dashboard Screen ....")
                }

                !uiState.isSignedIn -> {
                    NotSignedInContent(onSignOut = onSignOut)
                }

                uiState.user != null -> {
                    DashboardContent(
                        uiState = uiState,
                        onSignOut = onSignOut,
                        onNavigateToAddTransaction = onNavigateToAddTransaction,
                        onNavigateToViewTransactions =  onNavigateToViewTransactions,
                        onNavigateToSettings = onNavigateToSettings,
                        onRefresh = {
                            scope.launch {
                                val result = dashboardScreenViewModel.refreshUserData()
                                if (result.isFailure) {
                                    snackbarHostState.showSnackbar("Failed to refresh data")
                                }
                            }
                        },
                        onSignOutClick = {
                            scope.launch {
                                val result = dashboardScreenViewModel.signOut()
                                if (result.isSuccess) {
                                    snackbarHostState.showSnackbar("Signed out successfully")
                                    onSignOut?.invoke()
                                } else {
                                    snackbarHostState.showSnackbar("Failed to sign out")
                                }
                            }
                        }
                    )
                }

                else -> {
                    ErrorContent(
                        errorMessage = uiState.errorMessage ?: "Unknown error occurred",
                        onRetry = {
                            scope.launch {
                                dashboardScreenViewModel.refreshUserData()
                            }
                        }
                    )
                }
            }
        }
    }
}