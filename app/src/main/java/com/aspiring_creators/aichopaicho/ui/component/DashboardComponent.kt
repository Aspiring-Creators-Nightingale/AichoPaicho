package com.aspiring_creators.aichopaicho.ui.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import coil3.request.placeholder
import com.aspiring_creators.aichopaicho.R
import com.aspiring_creators.aichopaicho.viewmodel.data.DashboardScreenUiState

@Composable
fun UserProfileImage(
    photoUrl: String,
    userName: String?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(photoUrl)
                .crossfade(true)
                .placeholder(R.drawable.placeholder_user_profile) // Add placeholder
                .error(R.drawable.placeholder_user_profile_error) // Add error fallback
                .build(),
            contentDescription = "Profile photo of ${userName ?: "user"}",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape),
            onState = { state ->
                when (state) {
                    is AsyncImagePainter.State.Loading -> {
                        Log.d("UserProfileImage", "Loading image: $photoUrl")
                    }

                    is AsyncImagePainter.State.Error -> {
                        // This is the crucial part for error logging
                        Log.e(
                            "UserProfileImage",
                            "Error loading image: $photoUrl",
                            state.result.throwable
                        )
                        // state.result.throwable will contain the actual CoilException or underlying cause
                    }

                    is AsyncImagePainter.State.Success -> {
                        Log.d("UserProfileImage", "Successfully loaded image: $photoUrl")
                    }

                    else -> {
                        // Log other states if needed, though Empty is common before loading starts
                        Log.i("UserProfileImage", "Image state: $state for url: $photoUrl")
                    }
                }
            }
        )
    }
}

@Preview()
@Composable
fun UserProfileImagePreview() {
    UserProfileImage(
        "https://images.pexels.com/photos/326055/pexels-photo-326055.jpeg",
        "Goodness", Modifier
    )
}


@Composable
fun ErrorContent(
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
            TextComponent(
                value = "Error: $errorMessage",
                textSize = 25.sp,
                textColor = R.color.error
            )

            Spacer(modifier = Modifier.height(16.dp))

            ButtonComponent(
                logo = R.drawable.logo_skip,
                text = "Retry",
                onClick = onRetry
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorContentPreview() {
    ErrorContent(
        errorMessage = "An error occurred",
        onRetry = {}
    )
}

@Composable
fun DashboardContent(
    uiState: DashboardScreenUiState,
    onSignOut: (() -> Unit)?,
    onNavigateToAddTransaction: (() -> Unit)?,
    onNavigateToViewTransactions: (() -> Unit)?,
    onNavigateToSettings: (() -> Unit)?,
    onRefresh: () -> Unit,
    onSignOutClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // User Card
        UserDashboardToast(uiState)


        Spacer(modifier = Modifier.height(24.dp))

        // Quick Actions
        Text(
            text = "Quick Actions",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {

            onNavigateToAddTransaction?.let { navigateToAddTransaction ->
                QuickActionButton(
                    text = "New \n Transaction",
                    onClick = navigateToAddTransaction,
                    modifier = Modifier.padding(bottom = 8.dp),
                    contentDescription = "Add new Record or Transaction"
                )
            }
            onNavigateToViewTransactions?.let { navigateToViewTransactions ->
                QuickActionButton(
                    text = "View \n Transactions",
                    onClick = navigateToViewTransactions,
                    modifier = Modifier.padding(bottom = 8.dp),
                    contentDescription = "View Transactions"
                )
            }
            onNavigateToSettings?.let { navigateToSettings ->
                QuickActionButton(
                    text = "Settings",
                    onClick = { navigateToSettings },
                    modifier = Modifier.padding(bottom = 8.dp),
                    contentDescription = "Settings"
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        /*    QuickActionButton(
               text = "Refresh \n Data",
               onClick = onRefresh,
               modifier = Modifier.padding(bottom = 8.dp),
               contentDescription = "Refresh the Data"
           ) TODO: Add refresh button */
        // Sign Out Button
        /*   onSignOut?.let {
               ButtonComponent(
                   R.drawable.logo_skip,
                   "Sign Out",
                   onClick = onSignOutClick,
                   modifier = Modifier
                       .padding(horizontal = 32.dp)
                       .width(200.dp)
               )
           }TODO : Add Sign Out Button*/

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

@Composable
fun UserDashboardToast(uiState: DashboardScreenUiState) {
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
            UserProfileImage(
                photoUrl = uiState.user?.photoUrl.toString(),
                userName = uiState.user?.name,
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // User Info
            Column(
                modifier = Modifier
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
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    DashboardContent(
        uiState = DashboardScreenUiState(),
        onSignOut = {},
        onNavigateToAddTransaction = {},
        onNavigateToViewTransactions = {},
        onRefresh = {},
        onSignOutClick = {},
        onNavigateToSettings = {}
    )
}