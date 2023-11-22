package com.example.synder.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.synder.Screen
import com.example.synder.models.ChatAndParticipant
import com.example.synder.models.UserProfile
import com.google.firebase.storage.StorageReference

@Composable
fun Chat(it: ChatAndParticipant = ChatAndParticipant(), curRoute: String, storageRef: StorageReference, navController: NavHostController) {
    OutlinedCard(
        modifier = Modifier
            .wrapContentHeight()
            .background(Color.White)
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(8.dp)
            )
            .clickable {
                navController.navigate(Screen.Chat.name) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                }
            }
    ) {
        Row (
            modifier = Modifier
                .padding(start = 15.dp, top = 2.dp, bottom = 2.dp)
                .fillMaxSize(), // Center text both vertically and horizontally
            verticalAlignment = Alignment.CenterVertically

        ) {
                ProfilePicture(imgReferance = storageRef.child("images/${it.user1.id}.jpg"))
            //} else {
            //    Monogram(name = it.user1.name)
            //}

            Column(modifier = Modifier.padding(10.dp)) {
                Text(text = it.user1.name, fontWeight = FontWeight.Bold, fontSize = 20.sp) // Use "sp" for text size
                Text(text = it.latestmessage, fontSize = 16.sp) // Adjust text size as needed

                Card(
                    modifier = Modifier.padding(top = 10.dp)
                ) {
                    Text(text = it.latestmessage, fontSize = 12.sp, color = Color.Black, modifier = Modifier.padding(5.dp)) // Adjust text size as needed
                }
            }
        }
    }
}

@Composable
fun Chat(it: UserProfile, curRoute: String, storageRef: StorageReference, navController: NavHostController) {
    OutlinedCard(
        modifier = Modifier
            .wrapContentHeight()
            .background(Color.White)
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(8.dp)
            )
            .clickable {
                navController.navigate(Screen.Chat.name) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                }
            }
    ) {
        Row (
            modifier = Modifier
                .padding(start = 15.dp, top = 2.dp, bottom = 2.dp)
                .fillMaxSize(), // Center text both vertically and horizontally
            verticalAlignment = Alignment.CenterVertically

        ) {

            ProfilePicture(imgReferance = storageRef.child("images/${it.id}.jpg"))
            //if (it.profileImageUrl != null) {
            //    ProfilePicture(ulr = it.profileImageUrl)
            //} else {
            //    Monogram(name = it.name)
            //}

            Column(modifier = Modifier.padding(10.dp)) {
                Text(text = it.name, fontWeight = FontWeight.Bold, fontSize = 20.sp) // Use "sp" for text size
                Text(text = it.bio, fontSize = 16.sp) // Adjust text size as needed

                Card(
                    modifier = Modifier.padding(top = 10.dp)
                ) {
                    Text(text = it.name, fontSize = 12.sp, color = Color.Black, modifier = Modifier.padding(5.dp)) // Adjust text size as needed
                }
            }
            Spacer(modifier = Modifier.weight(1f)) // This spacer will push the OutlinedCard to the right

            OutlinedCard(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent, // Set background to transparent
                    contentColor = Color.White, // Set content color to white
                ),
                border = BorderStroke(1.dp, Color(0xFFFFC700)), // Set border color to #FFC700
                modifier = Modifier.padding(end = 10.dp)

            ) {
                Text(text = "Ny!", color = Color(0xFFFFC700), modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp)) // Set text color to #FFC700
            }
        }
    }
}