package com.example.synder.screen.ChatList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.synder.Screen
import com.example.synder.models.Chat
import com.example.synder.components.Chat
import com.example.synder.components.Chatbar
import com.example.synder.components.Message
import com.example.synder.models.Message

@Composable
fun chatScreen(curRoute: String, navController: NavHostController, modifier: Modifier = Modifier) {
    val userChats = listOf(
        Chat("Emma 28", "Emma: Hei der! Hvordan har dagen din vært?", "Sendt 10:15", Icons.Default.AccountBox),
        Chat("Sophie 32", "Sophie: Hei, hva skjer? :)", "Sendt 11:30", Icons.Default.AccountBox),
        Chat("Olivia 22", "Olivia: Wow, fremgangen din innen trening er fantastisk!", "Sendt 13:45", Icons.Default.AccountBox),
        Chat("Nora 25", "Nora: Hei! Hvordan har du det?", "Sendt 15:20", Icons.Default.AccountBox),
        Chat("Mia 29", "Mia: Håper du har det bra! :)", "Sendt 16:45", Icons.Default.AccountBox),
        Chat("Emma 28", "Du: Vil du ha bli med ut på date :)", "Sendt 10:15", Icons.Default.AccountBox),
        Chat("Sophie 32", "Sophie: Hei jeg liker bicepen din >-<", "Sendt 11:30", Icons.Default.AccountBox),
        Chat("Olivia 22", "Olivia: Hei kjekken ;)", "Sendt 13:45", Icons.Default.AccountBox),
        Chat("Nora 25", "Nora: Har jeg sett deg før? Er du han gutten jeg pratet med på byen sist lørdag:)", "Sendt 15:20", Icons.Default.AccountBox),
        Chat("Mia 29", "Mia: Hei har du hatt en fin dag:)", "Sendt 16:45", Icons.Default.AccountBox)


    )
    //Column
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)) {
        item { Text(text = "All Chats", fontSize = 32.sp) }

        items(userChats) { it ->
            Chat(it, curRoute, navController)
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
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )
        }
    }
}
@Composable
fun matchScreen(curRoute: String, navController: NavHostController, modifier: Modifier = Modifier) {
    val matches = listOf(
        Chat("Christine 21", "Matchet 4 dager siden", "", Icons.Default.AccountBox),
        Chat("Monica 45", "Matchet 4 dager siden", "", Icons.Default.AccountBox),
        Chat("Dudan 19", "Matchet 2 uker siden", "", Icons.Default.AccountBox),
        Chat("Polkan 23", "Matchet 4 år siden", "", Icons.Default.AccountBox),
    )
    //Column
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)) {
        item { Text(text = "All Chats", fontSize = 32.sp) }

        items(matches) { it ->
            Chat(it, curRoute, navController)
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
                text = "Ingen flere Syndere!",
                fontSize = 15.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )
        }
    }
}


@Composable
fun conversationWindow(modifier: Modifier = Modifier) {
    val messages = listOf(
        Message("Christine", "Hello", "Sent 4:00PM", false),
        Message("Du", "Hvordan går det?", "Sent 4:01PM", true),
        Message("Christine", "Ja", "Sent 4:00PM", false),
        Message("Du", "Ser du er interessert i å ha en samtale ass", "Sent 4:01PM", true),
        Message("Christine", "Ja", "Sent 4:00PM", false),
        Message("Du", ":|", "Sent 4:01PM", true),
        Message("Christine", "Ok", "Sent 4:00PM", false),
        Message("Du", "...", "Sent 4:01PM", true),
        Message("Christine", "hade", "Sent 4:00PM", false),
        )
    //Column
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)) {

        items(messages) { it ->
            Message(it)
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
                text = "Siste melding 4:00PM",
                fontSize = 15.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )
        }
    }
}