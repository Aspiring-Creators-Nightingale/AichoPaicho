package com.aspiring_creators.aichopaicho.ui.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import java.nio.file.WatchEvent
import kotlin.math.log


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
fun TextComponent(value: String, textSize: TextUnit = 12.sp, textColor: Int = R.color.textColor) {


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
        textAlign = TextAlign.Center
    )
}
@Preview(showBackground = true)
@Composable
fun TextComponentPreview()
{
    TextComponent(value = "Welcome to Aicho Paicho ffdfgfddgdfgdfgdgdfgdfgdfgdfg dfgdfgd fg dgdfg dfdgd gdf fdgdfg dfg", textSize = 27.sp)
}


@Composable
fun ButtonComponent(
    logo: Int,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
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
                textColor = R.color.black
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
        modifier = Modifier
    )
}





