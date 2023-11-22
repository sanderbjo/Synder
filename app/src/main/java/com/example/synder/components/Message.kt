package com.example.synder.components;

import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable;

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.example.synder.models.FromFirebase.MessagesFromFirebase
import com.example.synder.models.UserProfile

@Composable
fun Message(it: MessagesFromFirebase, userProfile: UserProfile, sentByUser: Boolean) {
        val shape = RoundedCornerShape(
                topStart = 15.dp,
                topEnd =  15.dp,
                bottomStart = if (sentByUser) 15.dp else 0.dp,
                bottomEnd = if (sentByUser) 0.dp else 15.dp
        )

        Row {
                if (sentByUser) {
                        Spacer(modifier = Modifier.weight(1f))
                }

                OutlinedCard(
                        colors = CardDefaults.cardColors(
                                containerColor = if (sentByUser) Color(0xFF4F378B) else Color(0xFF901D1D),
                                contentColor = Color.White,
                        ),
                        border = BorderStroke(
                                width = 2.dp,
                                color = if (sentByUser) Color(0xFF8B6CE3) else Color(0xFFD3454E),
                        ),
                        shape = shape,
                        modifier = Modifier
                                .padding(15.dp)
                                .fillMaxWidth(0.9f) // Limit width to 90% of the available space
                ) {
                        Row(
                                modifier = Modifier
                                        .fillMaxWidth() // Ensure the row takes the full width of the card
                                        .padding(start = 15.dp, end = 15.dp, top = 2.dp, bottom = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                        ) {
                                if (!sentByUser) {
                                        if (userProfile.profileImageUrl === "") {
                                                Monogram(name = userProfile.name)
                                        } else {
                                                ProfilePicture(url = userProfile.profileImageUrl)
                                        }
                                }

                                Column(
                                        modifier = Modifier
                                                .padding(10.dp)
                                                .weight(1f) // Allow the column to take up the available space
                                ) {
                                        val userText = if (sentByUser) " (Du)" else ""
                                        Text(
                                                /*text = if (sentByUser) "Du" else userProfile.name,*/
                                                text = userProfile.name + userText,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 16.sp,
                                                color = Color.White,
                                                textAlign = if (sentByUser) TextAlign.End else TextAlign.Start // Align the name text to the right
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                                text = it.text,
                                                fontSize = 20.sp,
                                                color = Color.White,
                                                maxLines = 2,
                                                overflow = TextOverflow.Ellipsis,
                                                textAlign = if (sentByUser) TextAlign.End else TextAlign.Start // Align the name text to the right
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                                text = it.sent,
                                                fontSize = 14.sp,
                                                color = Color.White,
                                                maxLines = 2,
                                                overflow = TextOverflow.Ellipsis,
                                                textAlign = if (sentByUser) TextAlign.End else TextAlign.Start // Align the name text to the right
                                        )
                                }
                        }
                }

                if (!sentByUser) {
                        Spacer(modifier = Modifier.weight(1f))
                }
        }
}