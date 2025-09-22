/*
 * Copyright 2025 aspiring_creators_nightingale
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */




package com.aspiring_creators.aichopaicho

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.aspiring_creators.aichopaicho.data.BackgroundSyncWorker
import com.aspiring_creators.aichopaicho.ui.navigation.AppNavigationGraph
import com.aspiring_creators.aichopaicho.ui.theme.AichoPaichoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                scrim = ContextCompat.getColor(applicationContext, R.color.lightStatusBarColor),
                darkScrim = ContextCompat.getColor(applicationContext, R.color.darkStatusBarColor)
            )
        )


        BackgroundSyncWorker.schedulePeriodicSync(applicationContext)
        setContent {
            AichoPaichoTheme {
                AichoPaicho()
            }
        }
    }
}


@Composable
fun AichoPaicho(
) {
    Surface(modifier = Modifier
        .fillMaxSize()
        .windowInsetsPadding(WindowInsets.statusBars)
        .padding(WindowInsets.navigationBars.asPaddingValues())
    ) {
        AppNavigationGraph()
    }
}
