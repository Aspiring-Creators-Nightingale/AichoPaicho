package com.aspiring_creators.aichopaicho.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.aspiring_creators.aichopaicho.R
import com.aspiring_creators.aichopaicho.ui.component.ButtonComponent
import com.aspiring_creators.aichopaicho.ui.component.LogoTopBar
import com.aspiring_creators.aichopaicho.ui.component.TextComponent
import com.aspiring_creators.aichopaicho.viewmodel.PermissionViewModel
import kotlinx.coroutines.launch

@Composable
fun PermissionScreen(
    onNavigateToDashboard: () -> Unit,
    onNavigateBack: (() -> Unit)? = null,
    permissionViewModel: PermissionViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val uiState by permissionViewModel.uiState.collectAsState()

    // Track permission state
    var contactsPermissionGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    // Update ViewModel when permission state changes
    LaunchedEffect(contactsPermissionGranted) {
        permissionViewModel.setPermissionGranted(contactsPermissionGranted)
    }

    // Permission request launcher
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        contactsPermissionGranted = isGranted

        scope.launch {
            if (isGranted) {
                snackbarHostState.showSnackbar("Contacts permission granted!")
                val result = permissionViewModel.grantPermissionAndProceed()
                if (result.isSuccess) {
                    onNavigateToDashboard()
                }
            } else {
                snackbarHostState.showSnackbar("Permission denied. You can still use the app without contact access.")
            }
        }
    }

    // Auto-navigate if permission is already granted
    LaunchedEffect(contactsPermissionGranted) {
        if (contactsPermissionGranted) {
            val result = permissionViewModel.grantPermissionAndProceed()
            if (result.isSuccess) {
                onNavigateToDashboard()
            }
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.Center
            ) {

                LogoTopBar(
                    logo = R.drawable.logo_contacts,
                    title = stringResource(R.string.allow_contact_access)
                )

                Spacer(modifier = Modifier.size(70.dp))

                TextComponent(
                    value = stringResource(R.string.ask_contact_access),
                    textSize = 30.sp,
                    textColor = R.color.textColor
                )

                Spacer(modifier = Modifier.size(70.dp))

                // Show error message if any
                uiState.errorMessage?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(horizontal = 16.dp),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.size(16.dp))
                }

                // Permission Button
                ButtonComponent(
                    logo = R.drawable.logo_contacts,
                    text = if (contactsPermissionGranted) "Permission Already Granted" else "Give Contact Access",
                    onClick = {
                        when {
                            contactsPermissionGranted -> {
                                scope.launch {
                                    snackbarHostState.showSnackbar("Contacts permission already granted!")
                                    val result = permissionViewModel.grantPermissionAndProceed()
                                    if (result.isSuccess) {
                                        onNavigateToDashboard()
                                    }
                                }
                            }
                            else -> {
                                requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
                            }
                        }
                    },
                    enabled = !uiState.isLoading
                )

                Spacer(modifier = Modifier.size(16.dp))

                // Skip Button
                ButtonComponent(
                    logo = R.drawable.logo_skip,
                    text = "Skip for Now",
                    onClick = {
                        scope.launch {
                            snackbarHostState.showSnackbar("You can grant permission later in settings")
                            val result = permissionViewModel.skipPermissionAndProceed()
                            if (result.isSuccess) {
                                onNavigateToDashboard()
                            }
                        }
                    },
                    enabled = !uiState.isLoading,
                    modifier = Modifier
                        .padding(horizontal = 75.dp)
                        .width(250.dp)
                )

                // Back Button (if provided)
                onNavigateBack?.let { navigateBack ->
                    Spacer(modifier = Modifier.size(16.dp))

                    ButtonComponent(
                        logo = R.drawable.logo_skip, // or use a back icon
                      text =  "Back",
                        onClick = navigateBack,
                        enabled = !uiState.isLoading,
                        modifier = Modifier
                            .padding(horizontal = 100.dp)
                            .width(200.dp)
                    )
                }

                // Loading indicator
                if (uiState.isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PermissionScreenPreview() {
    PermissionScreen(
        onNavigateToDashboard = {},
        onNavigateBack = {}
    )
}