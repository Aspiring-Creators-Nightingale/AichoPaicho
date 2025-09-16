package com.aspiring_creators.aichopaicho.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aspiring_creators.aichopaicho.R
import com.aspiring_creators.aichopaicho.ui.component.ButtonComponent
import com.aspiring_creators.aichopaicho.ui.component.LogoTopBar
import com.aspiring_creators.aichopaicho.ui.component.TextComponent


@Composable
fun WelcomeScreen()
{
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = colorResource(R.color.appThemeColor)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

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

            TextComponent(value = "Never forget a loan or a debt", textSize = 37.sp)

            Spacer(modifier = Modifier.size(33.dp))

            ButtonComponent(
                R.drawable.logo_google, "Sign in with Google",
                onClick = {},
                modifier = Modifier
            )

            ButtonComponent(
                R.drawable.logo_skip, "Skip for Now",
                onClick = {},
                modifier = Modifier.padding(horizontal = 85.dp).width(200.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview()
{
    WelcomeScreen()
}