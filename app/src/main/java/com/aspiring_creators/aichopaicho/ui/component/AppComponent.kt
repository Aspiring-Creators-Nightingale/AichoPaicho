package com.aspiring_creators.aichopaicho.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aspiring_creators.aichopaicho.R


@Composable
fun LogoTopBar(logo: Int, title: String)
{
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ){
        Spacer(modifier = Modifier.size(36.dp))
        Icon(
            painter = painterResource(id = logo)
            , contentDescription = "Title",
            tint = Color.Unspecified
        )
        Spacer(modifier = Modifier.size(36.dp))

       TextComponent("AichoPaicho", textSize = 35.sp,)

    }
}
@Preview(showBackground = true)
@Composable
fun LogoTopBarPreview()
{
    LogoTopBar(logo = R.drawable.logo_aichopaicho, title = "Aicho Paicho")
}

@Composable
fun TextComponent(
    value: String,
    textSize: TextUnit = 12.sp,
    textColor: Int = R.color.textColor,
    lineHeight: TextUnit = TextUnit.Unspecified
) {


    val crimsonTextFamily = FontFamily(
        Font(R.font.crimson_regular, FontWeight.Normal),
         Font(R.font.crimson_bold, FontWeight.Bold),
         Font(R.font.crimson_italic, FontWeight.Normal, FontStyle.Italic)
    )

    Text(
        text = value,
        modifier = Modifier.padding(10.dp),
        fontFamily = crimsonTextFamily,
        color = colorResource(textColor),
        fontSize = textSize,
        fontWeight = FontWeight.Normal,
        textAlign = TextAlign.Center,
        lineHeight = lineHeight
    )
}
@Preview(showBackground = true)
@Composable
fun TextComponentPreview()
{
    TextComponent(value = "Welcome to Aicho Paicho dfg dfg", textSize = 27.sp,)
}


@Composable
fun ButtonComponent(
    logo: Int,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
         colors = ButtonDefaults.buttonColors(
             containerColor = colorResource(R.color.buttonColor),
//             contentColor = colorResource(R.color.black)
         ),
        enabled = enabled ,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp) // Control padding inside the button
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(logo),
                contentDescription = "$text logo",
                tint = Color.Unspecified,

                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))

            TextComponent(
                value = text,
                textSize = 20.sp,
                textColor = R.color.black,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ButtonComponentPreview()
{
    ButtonComponent(
        logo = R.drawable.logo_google, text = "Sign in with Google",
        onClick = {},
    )
}

@Composable
fun SnackbarComponent(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    SnackbarHost(
        hostState = snackbarHostState,
        modifier = modifier,
    ) { data ->
        Snackbar(
            containerColor = colorResource(R.color.buttonColor),
            contentColor = colorResource(R.color.black),
            actionOnNewLine = true,
            shape = RoundedCornerShape(12.dp),
            action = {
                data.visuals.actionLabel?.let { actionLabel ->
                    TextButton(onClick = { data.performAction() }) {
                        Text(
                            text = actionLabel,
                            color = colorResource(R.color.black)
                        )
                    }
                }
            }
        ) {
            Text(text = data.visuals.message)
        }
    }
}

@Composable
fun LoadingContent(text: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            TextComponent(
                value = text,
                textColor = R.color.black,
                textSize = 16.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingContextPreview()
{
    LoadingContent("Dashboard Screen...")
}

@Composable
 fun NotSignedInContent(
    onSignOut: (() -> Unit)?
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextComponent(
                value = "You are Not Signed In",
                textColor = R.color.black,
                textSize = 30.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            onSignOut?.let { signOut ->
                ButtonComponent(
                    R.drawable.logo_sign_in,
                    "Go to Sign In",
                    onClick = signOut
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotSignedInContentPreview()
{
    NotSignedInContent(onSignOut = {})
}