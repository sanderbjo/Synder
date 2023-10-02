package com.example.synder.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.synder.R
import com.example.synder.models.UserProfile

@Composable
fun SwipeScreen() {
    val profiles by remember { mutableStateOf(testprofiles()) }
    var currentIndex by remember { mutableIntStateOf(0) }

    @Composable
    fun likeDislikeButtons(onLike: () -> Unit, onSuperLike: () -> Unit, onDislike: () -> Unit) {
        if (currentIndex < profiles.size) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(
                    onClick = onDislike,
                    modifier = Modifier.size(50.dp)
                ) {
                    Icon(imageVector = Icons.Default.Clear, contentDescription = "Dislike",
                        modifier = Modifier.size(50.dp))
                }
                Spacer(modifier = Modifier.width(32.dp))

                IconButton(
                    onClick = onSuperLike,
                    modifier = Modifier.size(50.dp)
                ) {
                    Icon(imageVector = Icons.Default.Star, contentDescription = "Super Like",
                        modifier = Modifier.size(50.dp))
                }
                Spacer(modifier = Modifier.width(32.dp))

                IconButton(
                    onClick = onLike,
                    modifier = Modifier.size(50.dp)
                ) {
                    Icon(imageVector = Icons.Default.Favorite, contentDescription = "Like",
                        modifier = Modifier.size(50.dp))
                }
            }
        }
    }

    @Composable
    fun profileCard(userProfile: UserProfile) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = userProfile.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .background(MaterialTheme.colorScheme.background)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = userProfile.name, style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Age: ${userProfile.age}", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = userProfile.bio, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(16.dp))
            }


        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(profiles.size) { index ->
            if (index == currentIndex) {
                profileCard(
                    userProfile = profiles[index])

            }
        }
        item {
            Card (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                likeDislikeButtons(
                    onLike = {
                        currentIndex ++ },
                    onDislike = {
                        currentIndex ++ },
                    onSuperLike = {
                        currentIndex ++
                    })
            }
        }

    }


}

private fun testprofiles(): List<UserProfile> {
    return listOf(
        UserProfile(1, "Katinka", 18, "I love hiking and traveling.", "https://example.com/alice.jpg"),
        UserProfile(2, "Kari", 25, "Passionate about photography.", "https://example.com/bob.jpg"),
        UserProfile(3, "Eline", 30, "Foodie and adventurer.", "https://example.com/charlie.jpg"),
        UserProfile(4, "Ida", 32, "Tech enthusiast.", "https://example.com/david.jpg"),
    )
}

@Composable
@Preview
fun Previewpage() {
    SwipeScreen()
}