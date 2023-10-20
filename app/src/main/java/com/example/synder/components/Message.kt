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
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun Message(it: Message) {
        val shape = RoundedCornerShape(
                topStart = if (it.sentbyuser) 8.dp else 0.dp,
                topEnd = if (it.sentbyuser) 0.dp else 8.dp,
                bottomStart = 0.dp,
                bottomEnd = if (it.sentbyuser) 0.dp else 8.dp
        )

        OutlinedCard(
                colors = CardDefaults.cardColors(
                        containerColor = if (it.sentbyuser) Color(0xFF4F378B) else Color(0xFF901D1D),
                        contentColor = Color.White,
                ),
                border = BorderStroke(1.dp, if (it.sentbyuser) Color(0xFF8B6CE3) else Color(0xFFD3454E)),
                shape = shape,
                modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
        ) {
                Column(modifier = Modifier.padding(5.dp)) {
                        if (!it.sentbyuser) {
                                Text(
                                        text = it.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp,
                                        color = Color.White,
                                )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                                text = it.message,
                                fontSize = 16.sp,
                                color = Color.White,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                                text = it.date,
                                fontSize = 12.sp,
                                color = Color.White,
                        )
                }
        }
}