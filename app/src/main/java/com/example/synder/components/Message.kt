package com.example.synder.components;

import androidx.compose.runtime.Composable;

import androidx.compose.foundation.background
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
import androidx.compose.material3.Text
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
import com.example.synder.models.Chat
import com.example.synder.models.Message


@Composable
fun Message(it: Message) {
        Column(
                modifier = Modifier
                        .wrapContentHeight()
                        .background(if (it.sentbyuser === true) {
                                Color.Gray
                        } else {
                                Color.Blue
                        })
                        .fillMaxWidth()
                        //.padding(8.dp) // Add some padding for spacing
                        .clip(
                                RoundedCornerShape(8.dp)
                        )
                        .padding(16.dp) // Add padding inside the box
        ) {
                Text(text = it.name, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.White) // Use "sp" for text size
                Text(text = it.message, fontSize = 16.sp, color = Color.White) // Adjust text size as needed
                Text(text = it.date, fontSize = 12.sp, color = Color.White) // Adjust text size as needed
        }
}