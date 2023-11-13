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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import com.example.synder.models.ChatAndParticipant
import com.example.synder.models.UserProfile
import java.text.SimpleDateFormat
import java.util.Date


@Composable
fun chatScreen(isDarkTheme: Boolean, curRoute: String, navController: NavHostController,
               userInChat: ChatAndParticipant,
               chatViewModel: ChatViewModel = hiltViewModel(), modifier: Modifier = Modifier

) {
    val chatsList = chatViewModel.chatsCache.values.toList()

    val userChats = listOf(chatsList)


    Log.d("Liste med chats:", "$chatsList")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            //.background(Color.White)
    ) {
        item {
            PageStart(title = "Chats")
        }

        items(chatsList) { it ->
            Chat(it, curRoute, navController)
        }

        item {
            PageEnd(textcontent = "Ingen flere Chats!")
        }
    }

}
@Composable
fun matchScreen(isDarkTheme: Boolean, curRoute: String, navController: NavHostController,
                userInChat: ChatAndParticipant,
                chatViewModel: ChatViewModel = hiltViewModel(), modifier: Modifier = Modifier
) {

    val userList = chatViewModel.usersCache.values.toList()
    Log.d("Liste med USERS:", "$userList")
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
    ) {
        item {
            PageStart(title = "Syndere")
        }

        items(userList) { it ->
            Chat(it, curRoute, navController)
        }

        item {
            PageEnd(textcontent = "Ingen flere Syndere!")
        }
    }
}


@Composable
fun conversationWindow(userInChat: ChatAndParticipant,
                       modifier: Modifier = Modifier,
                       chatViewModel: ChatViewModel = hiltViewModel()) {
    // Mapp meldinger fra ChatAndParticipant til Message objekter og sjekk hvem som sendte dem
    val messages = userInChat.chat.messages.map { firebaseMessage ->
        // Finn brukerprofilen i cachen ved hjelp av userId fra meldingen
        val userProfile = chatViewModel.usersCache[firebaseMessage.userId]

        // Sjekk om den innloggede brukeren sendte meldingen
        val sentByUser = firebaseMessage.userId == chatViewModel.accountService.currentUserId
        // Opprett et Message objekt med informasjonen
        Message(
            id = firebaseMessage.id,
            userId = userProfile?.name ?: "Ukjent bruker",
            userProfile = userInChat.user1,
            text = firebaseMessage.text,
            sent = firebaseMessage.sent,
            sentByUser = sentByUser
        )
    }

    // LazyColumn for å vise listen over meldinger
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(messages) { message ->
            Message(message, if (message.userId === userInChat.user1.id) userInChat.user1.name else userInChat.user2.name) // Pass på å bruke de riktige parameterne for MessageCard
        }

        item {
            // Vis en footer med tidspunktet for den siste meldingen, eller en annen beskjed hvis det ikke er noen meldinger
            val textContent = if (messages.isNotEmpty()) {
                "Siste melding ${messages.last().sent}"
            } else {
                "Ingen meldinger enda"
            }
            PageEnd(textcontent = textContent)
        }
    }
}


@Composable
fun PageStart (title: String) {
    Column (horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = 10.dp, start = 20.dp, end = 20.dp, bottom = 5.dp)) {
        Text(text = "Nylige ${title}", fontSize = 32.sp, modifier = Modifier.padding(bottom = 5.dp),
            /*color = Color.Black*/)
        Divider(
            thickness = 2.dp,
            /*color = Color.Black,*/
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
            /*color = Color.Black,*/
            thickness = 2.dp,
            modifier = Modifier
                .fillMaxWidth() // 80% total width
        )
        Text(
            /*color = Color.Black,*/
            text = textcontent,
            fontSize = 17.sp,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
                .padding(top = 10.dp)
        )
    }
}