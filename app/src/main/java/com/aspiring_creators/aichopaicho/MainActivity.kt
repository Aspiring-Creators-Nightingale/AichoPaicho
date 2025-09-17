package com.aspiring_creators.aichopaicho

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.aspiring_creators.aichopaicho.ui.navigation.AppNavigationGraph
import com.aspiring_creators.aichopaicho.ui.theme.AichoPaichoTheme
import com.aspiring_creators.aichopaicho.viewmodel.WelcomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AichoPaichoTheme {
             AichoPaicho()
            }
        }
    }
}

@Composable
fun AichoPaicho(
)
{
    Surface(modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.statusBars)) {
        AppNavigationGraph()
    }
}
