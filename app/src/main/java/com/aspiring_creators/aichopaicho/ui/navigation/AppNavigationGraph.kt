package com.aspiring_creators.aichopaicho.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigationGraph()
{
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.WELCOME_SCREEN){

        composable(Routes.WELCOME_SCREEN) {

        }
    }

}