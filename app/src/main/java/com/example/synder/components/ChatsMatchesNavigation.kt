package com.example.synder.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.synder.Screen
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun SegmentedButton(curRoute: String, navController: NavHostController, checked: Number) {
    // Define colors for active and inactive buttons
    val activeColor = Color.Blue
    val inactiveColor = Color.Transparent // Transparent background for unselected button

    val icon1Color = if (checked == 1) Color.White else Color.Black
    val text1Color = if (checked == 1) Color.White else Color.Black
    val icon2Color = if (checked == 2) Color.White else Color.Black
    val text2Color = if (checked == 2) Color.White else Color.Black

    val iconSize = 28.dp // Increase the icon size
    val buttonHeight = 40.dp // Increase the button height

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center)
            .padding(5.dp)
    ) {
        // Chats Button
        Row(
            modifier = Modifier
                .weight(1f) // Equal width for both buttons
                .clickable {
                    if (curRoute != Screen.Chats.name) {
                        navController.navigate(Screen.Chats.name) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                        }
                    }
                }
                .border(1.dp, Color.Gray, RoundedCornerShape(30.dp, 0.dp, 0.dp, 30.dp)) // Round the left side
                .background(if (checked == 1) activeColor else inactiveColor)
                .height(buttonHeight)
                .padding(4.dp)
                .padding(start = 6.dp) // Add some additional left padding
        ) {
            Icon(imageVector = Icons.Default.Email, contentDescription = null, modifier = Modifier.size(iconSize), tint = icon1Color)
            Text(
                text = "Chats",
                color = text1Color,
                fontWeight = if (checked == 1) FontWeight.Bold else FontWeight.Normal,
                fontSize = 20.sp // Increase font size
            )
        }

        // Matches Button
        Row(
            modifier = Modifier
                .weight(1f) // Equal width for both buttons
                .clickable {
                    if (curRoute != Screen.Matches.name) {
                        navController.navigate(Screen.Matches.name) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                        }
                    }
                }
                .border(1.dp, Color.Gray, RoundedCornerShape(0.dp, 30.dp, 30.dp, 0.dp)) // Round the right side
                .background(if (checked == 2) activeColor else inactiveColor)
                .height(buttonHeight)
                .padding(4.dp)
                .padding(end = 6.dp) // Add some additional right padding
        ) {
            Icon(imageVector = Icons.Default.AccountCircle, contentDescription = null, modifier = Modifier.size(iconSize), tint = icon2Color)
            Text(
                text = "Syndere",
                color = text2Color,
                fontWeight = if (checked == 2) FontWeight.Bold else FontWeight.Normal,
                fontSize = 20.sp // Increase font size
            )
        }
    }
}