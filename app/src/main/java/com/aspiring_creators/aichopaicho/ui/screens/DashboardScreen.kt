package com.aspiring_creators.aichopaicho.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.aspiring_creators.aichopaicho.R
import com.aspiring_creators.aichopaicho.data.WorkerUtils
import com.aspiring_creators.aichopaicho.ui.component.DashboardContent
import com.aspiring_creators.aichopaicho.ui.component.ErrorContent
import com.aspiring_creators.aichopaicho.ui.component.LoadingContent
import com.aspiring_creators.aichopaicho.ui.component.NetBalanceCard
import com.aspiring_creators.aichopaicho.ui.component.NotSignedInContent
import com.aspiring_creators.aichopaicho.viewmodel.DashboardScreenViewModel
import kotlinx.coroutines.launch

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
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle sign out navigation
    LaunchedEffect(uiState.isSignedIn) {
        if (!uiState.isSignedIn && uiState.user == null && !uiState.isLoading) {
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
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Net Balance Section
                item {
                    uiState.recordSummary?.let { summary ->
                        NetBalanceCard(
                            summary = summary,
                            onNavigateToContactList = {  onNavigateToContactList(it)},
                        )
                    }
                }
                item {
                    val current = LocalContext.current
                    Button(onClick = {
                       WorkerUtils.enqueueOneTimeSync(current)
                    }) {
                        Text(text = "Run Background Worker")
                    }
                }

                // Spacer
                item {
                    Spacer(modifier = Modifier.size(15.dp))
                }

                // Content Section
                item {
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
                                onNavigateToAddTransaction = onNavigateToAddTransaction,
                                onNavigateToViewTransactions = onNavigateToViewTransactions,
                                onNavigateToSettings = onNavigateToSettings
                            )
                        }

                        else -> {
                            ErrorContent(
                                errorMessage = uiState.errorMessage ?: "Unknown error occurred",
                                onRetry = {},
                            )
                        }
                    }
                }
            }
        }
    }
}
