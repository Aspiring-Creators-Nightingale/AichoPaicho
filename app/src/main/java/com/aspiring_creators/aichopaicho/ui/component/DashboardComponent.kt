package com.aspiring_creators.aichopaicho.ui.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import coil3.request.placeholder
import com.aspiring_creators.aichopaicho.R

@Composable
 fun UserProfileImage(
    photoUrl: String,
    userName: String?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(photoUrl)
                .crossfade(true)
                    .placeholder(R.drawable.placeholder_user_profile) // Add placeholder
                    .error(R.drawable.placeholder_user_profile_error) // Add error fallback
                .build(),
            contentDescription = "Profile photo of ${userName ?: "user"}",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape),
            onState = { state ->
                when (state) {
                    is AsyncImagePainter.State.Loading -> {
                        Log.d("UserProfileImage", "Loading image: $photoUrl")
                    }
                    is AsyncImagePainter.State.Error -> {
                        // This is the crucial part for error logging
                        Log.e("UserProfileImage", "Error loading image: $photoUrl", state.result.throwable)
                        // state.result.throwable will contain the actual CoilException or underlying cause
                    }
                    is AsyncImagePainter.State.Success -> {
                        Log.d("UserProfileImage", "Successfully loaded image: $photoUrl")
                    }
                    else -> {
                        // Log other states if needed, though Empty is common before loading starts
                        Log.i("UserProfileImage", "Image state: $state for url: $photoUrl")
                    }
                }
            }
        )
    }
}

@Preview()
@Composable
fun UserProfileImagePreview()
{
    UserProfileImage("https://images.pexels.com/photos/326055/pexels-photo-326055.jpeg",
        "Goodness", Modifier)
}


