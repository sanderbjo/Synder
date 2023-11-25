package com.example.synder.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.request.ImageRequest
import coil.compose.AsyncImage
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.firebase.storage.StorageReference


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProfilePicture (imgReferance: StorageReference, name: String, modifierSize: Int = 50) {
    if (imgReferance.path.isNullOrEmpty()) {
        Monogram(name = name)
    } else {
        GlideImage(
            model = imgReferance,
            contentDescription = "profilbilde",
            modifier = Modifier
                .size(modifierSize.dp)
                .clip(CircleShape),

            )
    }
}
@Composable
fun Monogram(name: String, modifierSize: Int = 50) {
    val initial = if (name.isNotEmpty()) name.substring(0, 1) else "?"
    Card(
        modifier = Modifier.size(modifierSize.dp),
        shape = CircleShape,
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE46962),
            contentColor = Color.White,
        )
    ) {
        Text(
            text = initial,
            color = Color.White,
            fontSize = 24.sp,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
            textAlign = TextAlign.Center
        )
    }
}