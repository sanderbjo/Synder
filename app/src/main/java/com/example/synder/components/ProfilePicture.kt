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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.request.ImageRequest
import coil.compose.AsyncImage


@Composable
fun ProfilePicture (url: String) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .crossfade(true)
            .build(),

        contentDescription = "profilbilde",
        modifier = Modifier.size(50.dp)
            .clip(CircleShape), // Set the size to create a circle

    )
}
@Composable
fun Monogram(name: String) {
    Card(
        modifier = Modifier.size(50.dp), // Set the size to create a circle
        shape = CircleShape, // Use CircleShape to make the card circular
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE46962),
            contentColor = Color.White,
        )
    ) {
        Text(
            text = name.substring(0, 1)/*name.substring(0, 1)*/,
            color = Color.White, // Set text color to white
            fontSize = 24.sp, // Adjust the font size as needed
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(), // Center text both vertically and horizontally
            textAlign = TextAlign.Center // Center align the text
        )
    }
}