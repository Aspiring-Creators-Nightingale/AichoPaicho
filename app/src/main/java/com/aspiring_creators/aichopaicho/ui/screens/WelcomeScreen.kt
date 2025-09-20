package com.aspiring_creators.aichopaicho.ui.screens

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.aspiring_creators.aichopaicho.R
import com.aspiring_creators.aichopaicho.ui.component.ButtonComponent
import com.aspiring_creators.aichopaicho.ui.component.LogoTopBar
import com.aspiring_creators.aichopaicho.ui.component.TextComponent
import com.aspiring_creators.aichopaicho.viewmodel.WelcomeViewModel
import kotlinx.coroutines.launch
import kotlin.math.log


@Composable
fun WelcomeScreen(
    onNavigateToPermissions: () -> Unit,
    welcomeViewModel: WelcomeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val activity = context as ComponentActivity
    val uiState by welcomeViewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    // Auto-navigate if user is already authenticated
    LaunchedEffect(Unit) {
        if (welcomeViewModel.shouldAutoNavigate()) {
            onNavigateToPermissions()
        }
    }

    val scrollState = rememberScrollState()

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState),
        color = colorResource(R.color.appThemeColor)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.size(33.dp))

            LogoTopBar(logo = R.drawable.logo_aichopaicho, title = "AichoPaicho")

            Spacer(modifier = Modifier.size(33.dp))

            Image(
                painter = painterResource(id = R.drawable.welcome_screen_1),
                contentDescription = "welcome screen",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(horizontal = 22.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.size(33.dp))

            TextComponent(
                value = "Never forget a loan or a debt",
                textSize = 33.sp,
                lineHeight = 33.sp
            )

            Spacer(modifier = Modifier.size(33.dp))

            // Show error message if any
            uiState.errorMessage.let { error ->
                if (error != null) {
                    TextComponent(
                        value = error,
                       textColor = R.color.error,
                    )
                }

                Spacer(modifier = Modifier.size(16.dp))
            }

            // Google Sign In Button
            ButtonComponent(
                logo =  R.drawable.logo_google,
                text = "Sign in with Google",
                onClick = {
                    scope.launch {
                        val result = welcomeViewModel.signInWithGoogle(activity, false)
                        if (result.isSuccess) {
                            onNavigateToPermissions()
                        }
                        // Error is automatically shown via uiState
                    }
                },
                enabled = !uiState.isLoading,
                modifier = Modifier
            )

            // Skip Button
            ButtonComponent(
                logo = R.drawable.logo_skip,
               text = "Skip for Now",
                onClick = {
                    scope.launch {
                        val result = welcomeViewModel.skipSignIn()
                        if (result.isSuccess) {
                            onNavigateToPermissions()
                        }
                    }
                },
                enabled = !uiState.isLoading,
                modifier = Modifier.padding(horizontal = 75.dp).width(250.dp)
            )

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

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview()
{
//    WelcomeScreen(hiltViewModel())
}