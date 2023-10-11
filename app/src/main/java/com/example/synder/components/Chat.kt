package com.example.synder.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.synder.Screen
import com.example.synder.models.Chat

@Composable
fun Chat(it: Chat, curRoute: String, navController: NavHostController) {
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .background(Color.White)
            .fillMaxWidth()
            //.padding(8.dp) // Add some padding for spacing
            .clip(
                RoundedCornerShape(8.dp)
            )
            //.padding(16.dp) // Add padding inside the box
            .clickable { // Add click listener here
                // Handle the click action
                navController.navigate(Screen.Chat.name) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                }
            }
    ) {
        Row() {
            Icon(imageVector = it.icon, contentDescription = null)
            Text(text = it.name, fontWeight = FontWeight.Bold, fontSize = 20.sp) // Use "sp" for text size
        }
        Text(text = it.latestChat, fontSize = 16.sp) // Adjust text size as needed
        Text(text = it.latestRecieved, fontSize = 12.sp) // Adjust text size as needed
    }

}