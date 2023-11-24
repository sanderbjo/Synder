package com.example.synder.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.synder.Screen
import com.example.synder.models.ChatAndParticipant

@Composable
fun ChatTopNavigation(it: ChatAndParticipant, currentUser: String, curRoute: String, navController: NavHostController) {
    val usertoshow = if (!it.user1.id.equals(currentUser)) it.user1 else it.user2

    Row(
        modifier = Modifier
            .fillMaxWidth(1f)
            .padding(bottom = 5.dp)
            /*.background(Color(0xFFE4E4E4))*/,
        verticalAlignment = Alignment.CenterVertically, // Align vertically centered
        horizontalArrangement = Arrangement.Start // Align the icon button to the left
    ) {
        IconButton(onClick = {
            if (curRoute != Screen.Chats.name) {
                navController.navigate(Screen.Chats.name) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                }
            }
        }) {
            Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = null, modifier = Modifier.size(40.dp))
        }
        Spacer(modifier = Modifier.weight(1f))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Monogram(name = usertoshow.name, 45)
            Text(
                text = "Chat med ${usertoshow.name} ${usertoshow.age}",
                fontSize = 22.sp,
                textAlign = TextAlign.Center // Denne vil sørge for at teksten er sentrert hvis den er i flere linjer
            )
        }

        //Icon(imageVector = Icons.Default.AccountCircle, contentDescription = null, modifier = Modifier.size(40.dp).padding(end = 10.dp))
    }
}