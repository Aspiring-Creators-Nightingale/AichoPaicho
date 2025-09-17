package com.aspiring_creators.aichopaicho.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.aspiring_creators.aichopaicho.ui.component.TextComponent
import com.aspiring_creators.aichopaicho.viewmodel.DashboardScreenViewModel


@Composable
fun DashboardScreen(
     dashboardScreenViewModel: DashboardScreenViewModel = hiltViewModel()
)
{
    Surface(
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            TextComponent(value = "Dasboard Screen", textSize = 30.sp)

            TextComponent(value = "${{ dashboardScreenViewModel.getUserName() }}")
        }

    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview()
{
//    DashboardScreen(hiltViewModel())
}