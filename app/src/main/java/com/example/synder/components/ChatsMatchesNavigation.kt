package com.example.synder.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.synder.Screen
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.compose.md_theme_dark_tertiary
import com.example.compose.md_theme_light_tertiary
import com.example.synder.R

@Composable
fun SegmentedButton(curRoute: String, navController: NavHostController, checked: Number, isDarkTheme: Boolean) {
    val activeColor = if (isDarkTheme) md_theme_dark_tertiary else md_theme_light_tertiary;
    val inactiveColor = Color.Transparent
    val text1Color = if (checked == 1) Color.White else Color.Black
    val text2Color = if (checked == 2) Color.White else Color.Black

    val leftcard = RoundedCornerShape(
        topStart = 20.dp,
        topEnd =  0.dp,
        bottomStart = 20.dp,
        bottomEnd = 0.dp
    )
    val rightcard = RoundedCornerShape(
        topStart = 0.dp,
        topEnd =  20.dp,
        bottomStart = 0.dp,
        bottomEnd = 20.dp
    )


    val iconSize = 20.dp
    val buttonHeight = 40.dp

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center)
            .padding(5.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        OutlinedCard(
            modifier = Modifier
                .weight(1f)
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
                .height(buttonHeight),
            border = BorderStroke(1.dp, Color.Gray),
            shape = leftcard,
            colors = CardDefaults.cardColors(
                containerColor = if (checked == 1) activeColor else inactiveColor,
                contentColor = if (checked == 1) activeColor else inactiveColor,
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 5.dp, start = 5.dp)
                    .fillMaxHeight(),
                horizontalArrangement = Arrangement.Center,
            )  {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_chat_bubble_outline_24),
                    contentDescription = null,
                    modifier = Modifier.size(iconSize),
                    tint = text1Color,
                )
                Text(
                    text = "Chats",
                    color = text1Color,
                    fontWeight = if (checked == 1) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 15.sp
                )
            }
        }

        OutlinedCard(
            modifier = Modifier
                .weight(1f)
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
                .height(buttonHeight),
            border = BorderStroke(1.dp, Color.Gray),
            shape = rightcard,
            colors = CardDefaults.cardColors(
                containerColor = if (checked == 2) activeColor else inactiveColor,
                contentColor = if (checked == 2) activeColor else inactiveColor,
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 5.dp, start = 5.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_perm_identity_24),
                    contentDescription = null,
                    modifier = Modifier.size(iconSize),
                    tint = text2Color
                )
                Text(
                    text = "Syndere",
                    color = text2Color,
                    fontWeight = if (checked == 2) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 15.sp,
                )
            }
        }
    }
}