package com.example.synder.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.synder.models.Chat
import com.example.synder.ui.Chat

@Composable
fun chatScreen(modifier: Modifier = Modifier) {
    val userChats = listOf(
        Chat("Christine 21", "Christine: Hello", "Sent 4:00PM", Icons.Default.AccountBox),
        Chat("Monica 45", "Monica: Heisann storegutten ;)", "Sent 4:00PM", Icons.Default.AccountBox),
        Chat("Dudan 19", "Dudan: Omg så store biceps!!!", "Sent 4:00PM", Icons.Default.AccountBox),
        Chat("Polkan 23", "Du: Jævla fitte", "Sent 4:00PM", Icons.Default.AccountBox),
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
    }
}