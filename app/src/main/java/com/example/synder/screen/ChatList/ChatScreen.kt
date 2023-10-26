package com.example.synder.screen.ChatList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.synder.models.Chat
import com.example.synder.components.Chat
import com.example.synder.components.Message
import com.example.synder.models.Message
import com.example.synder.screen.profile.ProfileViewModel
import android.util.Log
import com.example.synder.models.ChatAndParticipant


@Composable
fun chatScreen(curRoute: String, navController: NavHostController, modifier: Modifier = Modifier,
               chatViewModel: ChatViewModel = hiltViewModel(), profileViewModel: ProfileViewModel = hiltViewModel() //skal ikke være profile viewmodel
) {
    val chat by chatViewModel.chat //bytt til chats etterhvert
    profileViewModel.getUserById(chat.id1)
    val user1 by profileViewModel.user
    profileViewModel.getUserById(chat.id2)
    val user2 by profileViewModel.user
    val testmessage = Message(
        name = "This is a test message",
        message = "senderUserId", // Replace with the sender's user ID
        date = System.currentTimeMillis().toString(), // You can set the timestamp as needed
        sentbyuser = false
    )
    val chat1AndParticipant = ChatAndParticipant(
        id = chat.id, // Set the ID from the chat
        chat = chat,
        user1 = user1,
        user2 = user2,
        latestmessage = testmessage // Replace with the actual Message object
    )
    val userChats = listOf(
        Chat(chat1AndParticipant.user1.name, chat1AndParticipant.latestmessage.message, "4:00PM", chat1AndParticipant.user1.profileImageUrl),
        Chat("Emma 28", "Emma: Hei der! Hvordan har dagen din vært?", "Sendt 10:15" ),
        Chat("Sophie 32", "Sophie: Hei, hva skjer? :)", "Sendt 11:30" ),
        Chat("Olivia 22", "Olivia: Wow, fremgangen din innen trening er fantastisk!", "Sendt 13:45" ),
        Chat("Nora 25", "Nora: Hei! Hvordan har du det?", "Sendt 15:20" ),
        Chat("Mia 29", "Mia: Håper du har det bra! :)", "Sendt 16:45" ),
        Chat("Emma 28", "Du: Vil du ha bli med ut på date :)", "Sendt 10:15" ),
        Chat("Sophie 32", "Sophie: Hei jeg liker bicepen din >-<", "Sendt 11:30" ),
        Chat("Olivia 22", "Olivia: Hei kjekken ;)", "Sendt 13:45" ),
        Chat("Nora 25", "Nora: Har jeg sett deg før? Er du han gutten jeg pratet med på byen sist lørdag:)", "Sendt 15:20" ),
        Chat("Mia 29", "Mia: Hei har du hatt en fin dag:)", "Sendt 16:45" )
    )
    val bug = userChats[0]
    Log.d("Første bruker", "$bug")
    //Column
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)) {
        item {
            PageStart(title = "Chats")
        }


        /*
        item() {
            Chat(bug, curRoute, navController)
        }*/
        items(userChats) { it ->
            Chat(it, curRoute, navController)
        }

        item {
            PageEnd(textcontent = "Ingen flere Chats!")
        }
    }
}
@Composable
fun matchScreen(curRoute: String, navController: NavHostController, modifier: Modifier = Modifier) {
    val matches = listOf(
        Chat("Christine 21", "Matchet 4 dager siden", "" ),
        Chat("Monica 45", "Matchet 4 dager siden", "" ),
        Chat("Dudan 19", "Matchet 2 uker siden", "" ),
        Chat("Polkan 23", "Matchet 4 år siden", "" ),
    )
    //Column
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        item {
            PageStart(title = "Syndere")
        }

        items(matches) { it ->
            Chat(it, curRoute, navController, true)
        }

        item {
            PageEnd(textcontent = "Ingen flere Syndere!")
        }
    }
}


@Composable
fun conversationWindow(modifier: Modifier = Modifier) {
    val messages = listOf(
        Message("Christine", "Hello", "4:00PM", false),
        Message("Du", "Hvordan går det?", "4:01PM", true),
        Message("Christine", "Ja", "4:00PM", false),
        Message("Du", "Ser du er interessert i å ha en samtale ass", " 4:01PM", true),
        Message("Christine", "Ja", "4:00PM", false),
        Message("Du", ":|", "4:01PM", true),
        Message("Christine", "Ok", "4:00PM", false),
        Message("Du", "...", "4:01PM", true),
        Message("Christine", "hade", "4:00PM", false),
        )
    //Column
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        items(messages) { it ->
            Message(it)
        }

        item {
            PageEnd(textcontent = "Siste melding4:00PM")
        }
    }
}

@Composable
fun PageStart (title: String) {
    Column (horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = 10.dp, start = 20.dp, end = 20.dp, bottom = 5.dp)) {
        Text(text = "Nylige ${title}", fontSize = 32.sp, modifier = Modifier.padding(bottom = 5.dp))
        Divider(
            color = Color.Black,
            thickness = 2.dp,
            modifier = Modifier
                .fillMaxWidth() // 80% total width
        )
    }
}
@Composable
fun PageEnd (textcontent: String) {
    Column (horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(20.dp)) {
        Divider(
            color = Color.Black,
            thickness = 2.dp,
            modifier = Modifier
                .fillMaxWidth() // 80% total width
        )
        Text(
            text = textcontent,
            fontSize = 17.sp,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
                .padding(top = 10.dp)
        )
    }
}