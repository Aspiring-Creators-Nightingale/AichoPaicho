package com.aspiring_creators.aichopaicho.ui.screens

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.aspiring_creators.aichopaicho.R
import com.aspiring_creators.aichopaicho.ui.component.ButtonComponent
import com.aspiring_creators.aichopaicho.ui.component.LoadingContent
import com.aspiring_creators.aichopaicho.ui.component.LogoTopBar
import com.aspiring_creators.aichopaicho.ui.component.UserProfileImage
import com.aspiring_creators.aichopaicho.viewmodel.DashboardScreenViewModel
import com.aspiring_creators.aichopaicho.viewmodel.data.DashboardScreenUiState
import kotlinx.coroutines.launch

@Composable
fun DashboardScreen(
    onSignOut: (() -> Unit)? = null,
    onNavigateToProfile: (() -> Unit)? = null,
    onNavigateToTransactions: (() -> Unit)? = null,
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
                        onNavigateToProfile = onNavigateToProfile,
                        onNavigateToTransactions = onNavigateToTransactions,
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



@Composable
private fun NotSignedInContent(
    onSignOut: (() -> Unit)?
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "You are not signed in",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            onSignOut?.let { signOut ->
                ButtonComponent(
                    R.drawable.logo_skip,
                    "Go to Sign In",
                    onClick = signOut
                )
            }
        }
    }
}

@Composable
private fun ErrorContent(
    errorMessage: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Error: $errorMessage",
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            ButtonComponent(
                R.drawable.logo_skip,
                "Retry",
                onClick = onRetry
            )
        }
    }
}

@Composable
private fun DashboardContent(
    uiState: DashboardScreenUiState,
    onSignOut: (() -> Unit)?,
    onNavigateToProfile: (() -> Unit)?,
    onNavigateToTransactions: (() -> Unit)?,
    onRefresh: () -> Unit,
    onSignOutClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Welcome Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // User Profile Image
                UserProfileImage(
                    photoUrl = uiState.user?.photoUrl.toString(),
                    userName = uiState.user?.name,
                    modifier = Modifier.size(64.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                // User Info
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Welcome back!",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Normal
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = uiState.user?.name ?: "User",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    uiState.user?.email?.let { email ->
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = email,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Quick Actions
        Text(
            text = "Quick Actions",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Navigation Buttons
        onNavigateToTransactions?.let { navigateToTransactions ->
            ButtonComponent(
                R.drawable.logo_aichopaicho,
                "View Transactions",
                onClick = navigateToTransactions,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        onNavigateToProfile?.let { navigateToProfile ->
            ButtonComponent(
                R.drawable.logo_aichopaicho,
                "Profile Settings",
                onClick = navigateToProfile,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        ButtonComponent(
            R.drawable.logo_skip,
            "Refresh Data",
            onClick = onRefresh,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        // Sign Out Button
        onSignOut?.let {
            ButtonComponent(
                R.drawable.logo_skip,
                "Sign Out",
                onClick = onSignOutClick,
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .width(200.dp)
            )
        }

        // Error message display
        uiState.errorMessage?.let { error ->
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    DashboardScreen(
        onSignOut = {},
        onNavigateToProfile = {},
        onNavigateToTransactions = {}
    )
}