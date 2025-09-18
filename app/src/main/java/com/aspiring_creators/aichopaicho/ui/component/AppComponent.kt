package com.aspiring_creators.aichopaicho.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Label
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
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

val crimsonTextFamily = FontFamily(
    Font(R.font.crimson_regular, FontWeight.Normal),
    Font(R.font.crimson_bold, FontWeight.Bold),
    Font(R.font.crimson_italic, FontWeight.Normal, FontStyle.Italic)
)
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

       TextComponent("AichoPaicho", textSize = 35.sp)

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
    TextComponent(value = "Welcome to Aicho Paicho dfg dfg", textSize = 27.sp)
}


@Composable
fun ButtonComponent(
    modifier: Modifier = Modifier,
    logo: Int,
    text: String? = null,
    onClick: () -> Unit,
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
            if (text != null) {
                TextComponent(
                    value = text,
                    textSize = 20.sp,
                    textColor = R.color.black,
                )
            }
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
fun QuickActionButton(
    onClick: () -> Unit,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    text: String // Optional text for Extended FAB
) {
        // Use ExtendedFloatingActionButton if you want text next to the icon
        FloatingActionButton(
            onClick = onClick,
            modifier = modifier,
            containerColor = MaterialTheme.colorScheme.primaryContainer, // Or your custom color
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ) {
            Row()
            {
                Text(
                    text = "  $text",
                    textAlign = TextAlign.Center,
                    fontFamily = crimsonTextFamily,
                    fontStyle = FontStyle.Normal,
                    fontSize = 21.sp,
                    maxLines = 2,
                    modifier = Modifier.padding(5.dp)
                )
            }
        }

}


@Preview(showBackground = true)
@Composable
fun ExtendedQuickActionButtonPreview() {
    QuickActionButton(
        onClick = { /* Handle action */ },
        contentDescription = "Add new item",
        text = "Create \n Item",
        modifier = Modifier.padding(2.dp)
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
                    logo = R.drawable.logo_sign_in,
                    text = "Go to Sign In",
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

@Composable
fun LabelComponent(
    text: String,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(contentPadding),
            style = MaterialTheme.typography.labelLarge,
            fontFamily = crimsonTextFamily,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        )
    }
}


@Preview
@Composable
fun LabelComponentView()
{
    LabelComponent("Name")
}

object TypeConstants{
    const val TYPE_LENT = "Lent"
    const val TYPE_BORROWED = "Borrowed"
}


@Composable
fun SegmentedLentBorrowedToggle(
    onToggle: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    var isLent by remember { mutableStateOf(true) }


    Box(
        modifier = modifier
            .background(
                Color.Gray.copy(alpha = 0.2f),
                RoundedCornerShape(8.dp)
            )
            .padding(4.dp)
    ) {
        Row {
            // Lent button
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(
                        color = if (isLent) MaterialTheme.colorScheme.primary else Color.Transparent,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .clickable {
                        isLent = true
                        onToggle(TypeConstants.TYPE_LENT)
                    }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = TypeConstants.TYPE_LENT,
                    color = if (isLent) Color.White else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    fontWeight = if (isLent) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 16.sp
                )
            }

            // Borrowed button
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(
                        color = if (!isLent) MaterialTheme.colorScheme.primary else Color.Transparent,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .clickable {
                        isLent = false
                        onToggle(TypeConstants.TYPE_BORROWED)
                    }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = TypeConstants.TYPE_BORROWED,
                    color = if (!isLent) Color.White else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    fontWeight = if (!isLent) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Preview
@Composable
fun SegmentedLentBorrowedTogglePreview() {
    var isLent by remember { mutableStateOf("") }
    SegmentedLentBorrowedToggle(
        onToggle = { isLent =  it },
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.size(22.dp))
    Text(text = " Value is:- $isLent")
}