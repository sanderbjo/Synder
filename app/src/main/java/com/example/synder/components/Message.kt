package com.example.synder.components;

import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable;

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.foundation.layout.Arrangement

@Composable
fun Message(it: Message) {
        Column(
                modifier = Modifier
                        .border(
                                shape = if (!it.sentbyuser) {
                                        RoundedCornerShape(30.dp, 30.dp, 30.dp, 0.dp)
                                } else {
                                        RoundedCornerShape(30.dp, 30.dp, 0.dp, 30.dp)
                                },
                                border = BorderStroke(1.dp, Color.Black),
                        )
                        .wrapContentSize()
                        .background(if (!it.sentbyuser) Color.Gray else Color.Blue)
                        .clip(RoundedCornerShape(8.dp))
                        .padding(16.dp)
                        .padding(
                                start = if (it.sentbyuser) 8.dp else 0.dp,
                                end = if (!it.sentbyuser) 8.dp else 0.dp
                        )
        ) {
                Text(text = it.name, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.White)
                Text(text = it.message, fontSize = 16.sp, color = Color.White)
                Text(text = it.date, fontSize = 12.sp, color = Color.White)
        }
}
