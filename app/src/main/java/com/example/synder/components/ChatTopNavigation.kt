package com.example.synder.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.synder.Screen

@Composable
fun ChatTopNavigation(curRoute: String, navController: NavHostController) {
    Row {
        Button(onClick = { if (curRoute != Screen.Chats.name) {
            navController.navigate(Screen.Chats.name){
                popUpTo(navController.graph.findStartDestination().id){
                    saveState = true
                }
                launchSingleTop = true
            }
        } }) {
            Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = null)
            Text(text = "tilbake")
        }
        Text(text = "Cathrine 25")
    }
}