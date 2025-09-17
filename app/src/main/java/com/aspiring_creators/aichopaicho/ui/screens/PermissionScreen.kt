package com.aspiring_creators.aichopaicho.ui.screens

import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.aspiring_creators.aichopaicho.R
import com.aspiring_creators.aichopaicho.ui.component.ButtonComponent
import com.aspiring_creators.aichopaicho.ui.component.LogoTopBar
import com.aspiring_creators.aichopaicho.ui.component.TextComponent
import android.Manifest
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.aspiring_creators.aichopaicho.viewmodel.PermissionViewModel
import kotlinx.coroutines.launch


@Composable
fun PermissionScreen(
    permissionViewModel: PermissionViewModel = hiltViewModel(),
    ShowDashboardScreen: () -> Unit
) {
    val context = LocalContext.current

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // State to track if permission is granted
    val contactsPermissionGranted = remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {

            scope.launch {
                snackbarHostState.showSnackbar("Contacts permission granted")
                permissionViewModel.markPermissionScreenShown()
                ShowDashboardScreen()
            }
        } else {
            // Show error snackbar with Retry
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = "Permission denied. Please try again later",
                )
            }
        }
    }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Surface(
            modifier = Modifier.fillMaxWidth().padding(paddingValues),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),

                ) {

                LogoTopBar(logo = R.drawable.logo_contacts, title = "Allow Contact Access")

                Spacer(modifier = Modifier.size(70.dp))

                TextComponent(
                    value = "Give us access to your contacts to quickly add friends and family when you record a transaction. We never store or share your information.",
                    textSize = 30.sp,
                    textColor = R.color.textColor
                )

                Spacer(modifier = Modifier.size(70.dp))


                ButtonComponent(
                    R.drawable.logo_contacts, text = "Give Contact Access",
                    onClick = {
                        when {
                            contactsPermissionGranted.value -> {
                                scope.launch {
                                    snackbarHostState.showSnackbar("Contacts permission already granted")
                                    permissionViewModel.markPermissionScreenShown()
                                    ShowDashboardScreen()
                                }
                            }

                            else -> {
                                requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
                            }
                        }
                    },
                    modifier = Modifier
                )

                ButtonComponent(
                    R.drawable.logo_skip, "Skip for Now",
                    onClick = {
                        scope.launch {

                            snackbarHostState.showSnackbar("Permission request skipped")
                            permissionViewModel.markPermissionScreenShown()
                            ShowDashboardScreen()
                        }
                    },
                    modifier = Modifier
                        .padding(horizontal = 75.dp)
                        .width(250.dp)
                )

            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun PermissionScreenPreview()
{
    PermissionScreen(ShowDashboardScreen = {})
}