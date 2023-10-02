package com.example.synder.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.synder.models.Chat

@Composable
fun Chat(it: Chat) {
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .background(Color.White)
            .fillMaxWidth()
            //.padding(8.dp) // Add some padding for spacing
            .clip(
                RoundedCornerShape(8.dp)
            )
            .padding(16.dp) // Add padding inside the box
    ) {
        Row() {
            Icon(imageVector = it.icon, contentDescription = null)
            Text(text = it.name, fontWeight = FontWeight.Bold, fontSize = 20.sp) // Use "sp" for text size
        }
        Text(text = it.latestChat, fontSize = 16.sp) // Adjust text size as needed
        Text(text = it.latestRecieved, fontSize = 12.sp) // Adjust text size as needed
    }
}

/*val userChats = listOf(
        chat("Christine 21", "Christine: Hello", "Sent 4:00PM", "", Icons.Default.AccountBox),
        chat("Monica 45", "Monica: Heisann storegutten ;)", "Sent 4:00PM","", Icons.Default.AccountBox),
        chat("Dudan 19", "Dudan: Omg så store biceps!!!", "Sent 4:00PM","", Icons.Default.AccountBox),
        chat("Polkan 23", "Du: Jævla fitte", "Sent 4:00PM","", Icons.Default.AccountBox),
    )

    Surface (
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)){

        LazyColumn(

        ) {
            item { Text(text = "All Chats", fontSize = 32.sp) }

            items(userChats) { it ->
                Chat(it)
            }

            item {
                Divider(
                    color = Color.Black,
                    thickness = 2.dp,
                    modifier = Modifier
                        .fillMaxWidth(0.8f) // 80% total width
                        .wrapContentWidth(Alignment.CenterHorizontally)
                        .padding(start = 1.dp, end = 1.dp)
                )
            }

            item {
                Text(
                    text = "Ingen Flere Chats!",
                    fontSize = 15.sp,
                    modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.CenterHorizontally)
                )
            }
        }
    }*/