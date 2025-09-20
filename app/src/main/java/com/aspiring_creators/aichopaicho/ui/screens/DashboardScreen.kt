package com.aspiring_creators.aichopaicho.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
// import androidx.compose.foundation.layout.fillMaxWidth // Not directly used in LazyColumn like this
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
// import androidx.compose.material3.Button // Not directly used
import androidx.compose.material3.MaterialTheme // Added
import androidx.compose.material3.Scaffold
// import androidx.compose.material3.SnackbarHost // Replaced by SnackbarComponent
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
// import androidx.compose.material3.Text // Not directly used
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
// import androidx.compose.runtime.rememberCoroutineScope // Not used directly here for snackbar, handled by components
import androidx.compose.ui.Modifier
// import androidx.compose.ui.platform.LocalContext // Not directly used
// import androidx.compose.ui.res.colorResource // No longer needed
import androidx.compose.ui.tooling.preview.Preview // Added for Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
// import com.aspiring_creators.aichopaicho.R // No longer needed for colors
// import com.aspiring_creators.aichopaicho.data.BackgroundSyncWorker // Not directly used
import com.aspiring_creators.aichopaicho.ui.component.DashboardContent
import com.aspiring_creators.aichopaicho.ui.component.ErrorContent
import com.aspiring_creators.aichopaicho.ui.component.LoadingContent
import com.aspiring_creators.aichopaicho.ui.component.NetBalanceCard
import com.aspiring_creators.aichopaicho.ui.component.NotSignedInContent
import com.aspiring_creators.aichopaicho.ui.component.SnackbarComponent // Added
import com.aspiring_creators.aichopaicho.ui.theme.AichoPaichoTheme // Added for Preview
import com.aspiring_creators.aichopaicho.viewmodel.DashboardScreenViewModel
// import kotlinx.coroutines.launch // Not directly used

@Composable
fun DashboardScreen(
    onSignOut: (() -> Unit)? = null,
    onNavigateToAddTransaction: (() -> Unit)?,
    onNavigateToViewTransactions: (() -> Unit)?,
    onNavigateToSettings: (() -> Unit)?,
    onNavigateToContactList: (String) -> Unit,
    dashboardScreenViewModel: DashboardScreenViewModel = hiltViewModel()
) {
    val uiState by dashboardScreenViewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.isSignedIn, uiState.user, uiState.isLoading) {
        if (!uiState.isSignedIn && uiState.user == null && !uiState.isLoading) {
            onSignOut?.invoke()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarComponent(snackbarHostState = snackbarHostState) } // Use themed SnackbarComponent
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background // Use theme background
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    uiState.recordSummary?.let { summary ->
                        NetBalanceCard(
                            summary = summary,
                            onNavigateToContactList = { onNavigateToContactList(it) },
                        )
                    }
                }
                item {
                    Spacer(modifier = Modifier.size(15.dp)) // This spacer might be redundant due to LazyColumn's spacedBy
                }

                item {
                    when {
                        uiState.isLoading -> {
                            LoadingContent(text = "Loading Dashboard...") // Updated text
                        }
                        !uiState.isSignedIn -> {
                            NotSignedInContent(onSignOut = onSignOut)
                        }
                        uiState.user != null -> {
                            DashboardContent(
                                uiState = uiState,
                                onNavigateToAddTransaction = onNavigateToAddTransaction,
                                onNavigateToViewTransactions = onNavigateToViewTransactions,
                                onNavigateToSettings = onNavigateToSettings
                                // Consider passing snackbarHostState if DashboardContent needs to show snackbars
                            )
                        }
                        else -> {
                            ErrorContent(
                                errorMessage = uiState.errorMessage ?: "An unexpected error occurred.", // More user-friendly default
                                onRetry = {
                                    dashboardScreenViewModel.loadUserData()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    AichoPaichoTheme {
        DashboardScreen(
            onSignOut = {},
            onNavigateToAddTransaction = {},
            onNavigateToViewTransactions = {},
            onNavigateToSettings = {},
            onNavigateToContactList = {}
        )
    }
}
