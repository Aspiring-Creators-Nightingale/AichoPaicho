package com.aspiring_creators.aichopaicho.ui.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import android.widget.Space
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.aspiring_creators.aichopaicho.R
import com.aspiring_creators.aichopaicho.data.repository.UserRepository
import com.aspiring_creators.aichopaicho.ui.component.ButtonComponent
import com.aspiring_creators.aichopaicho.ui.component.LogoTopBar
import com.aspiring_creators.aichopaicho.ui.component.TextComponent
import com.aspiring_creators.aichopaicho.viewmodel.WelcomeViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject



fun Context.findActivity(): FragmentActivity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is FragmentActivity) return context
        context = context.baseContext
    }
    return null
}

@Composable
fun WelcomeScreen(
     welcomeViewModel: WelcomeViewModel = hiltViewModel()
)
{
    val context = LocalContext.current
    val activity = context as ComponentActivity

    val scrollState = rememberScrollState()
    Surface(
        modifier = Modifier.fillMaxWidth()
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

            TextComponent(value = "Never forget a loan or a debt", textSize = 33.sp, lineHeight = 33.sp)

            Spacer(modifier = Modifier.size(33.dp))

            ButtonComponent(
                R.drawable.logo_google, "Sign in with Google",
                onClick = {
                   welcomeViewModel.handleGoogleIdToken(activity)
                },
                modifier = Modifier
            )

            ButtonComponent(
                R.drawable.logo_skip, "Skip for Now",
                onClick = {
                    welcomeViewModel.handleSkipNow()
                },
                modifier = Modifier.padding(horizontal = 75.dp).width(250.dp)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview()
{
    WelcomeScreen(hiltViewModel())
}