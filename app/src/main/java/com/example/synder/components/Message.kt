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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun Message(it: Message) {
        val shape = RoundedCornerShape(
                topStart = 15.dp,
                topEnd =  15.dp,
                bottomStart = if (it.sentbyuser) 15.dp else 0.dp,
                bottomEnd = if (it.sentbyuser) 0.dp else 15.dp
        )
        Row {
                if (it.sentbyuser) {
                        Spacer(modifier = Modifier.weight(1f))
                }

                OutlinedCard(
                        colors = CardDefaults.cardColors(
                                containerColor = if (it.sentbyuser) Color(0xFF4F378B) else Color(0xFF901D1D),
                                contentColor = Color.White,
                        ),
                        border = BorderStroke(
                                width = 2.dp,
                                color = if (it.sentbyuser) Color(0xFF8B6CE3) else Color(0xFFD3454E),
                        ),
                        shape = shape,
                        modifier = Modifier
                                .padding(15.dp)
                                .fillMaxWidth(0.9f) // Limit width to 90% of the available space
                ) {
                        Row(
                                modifier = Modifier
                                        .fillMaxWidth() // Ensure the row takes the full width of the card
                                        .padding(start = 15.dp, top = 2.dp, bottom = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                        ) {
                                if (!it.sentbyuser) {
                                        Monogram(name = it.name)
                                }

                                Column(
                                        modifier = Modifier
                                                .padding(10.dp)
                                                .weight(1f) // Allow the column to take up the available space
                                ) {
                                        Text(
                                                text = it.name + "(${it.date})",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 20.sp,
                                                color = Color.White,
                                                textAlign = if (it.sentbyuser) TextAlign.End else TextAlign.Start // Align the name text to the right
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                                text = it.message,
                                                fontSize = 16.sp,
                                                color = Color.White,
                                                maxLines = 2,
                                                overflow = TextOverflow.Ellipsis,
                                                textAlign = if (it.sentbyuser) TextAlign.End else TextAlign.Start // Align the name text to the right
                                        )
                                }
                                if (it.sentbyuser) {
                                        Monogram(name = it.name)
                                }
                        }
                }


                if (!it.sentbyuser) {
                        Spacer(modifier = Modifier.weight(1f))
                }
        }
}