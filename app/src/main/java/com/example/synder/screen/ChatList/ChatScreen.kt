package com.example.synder.screen.ChatList

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.synder.components.Chat
import com.example.synder.components.Message
import com.example.synder.models.Message
import android.util.Log
import com.example.synder.models.ChatAndParticipant
import com.example.synder.models.FromFirebase.MessagesFromFirebase
import com.example.synder.models.UserProfile

@Composable
fun chatScreen(getChatFromClick: (String, ChatAndParticipant) -> Unit, isDarkTheme: Boolean, curRoute: String, navController: NavHostController,
               userInChat: ChatAndParticipant,
               chatViewModel: ChatViewModel = hiltViewModel(), modifier: Modifier = Modifier

) {
    val chatsList = chatViewModel.chatsCache.values.toList()

    val userChats = listOf(chatsList)


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            //.background(Color.White)
    ) {
        item {
            PageStart(title = "Chats")
        }

        items(chatsList) { it ->
            Log.d("TESTINDCH11: ", "$it")

            Chat(onChatClick = getChatFromClick, it, curRoute, navController)
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

    //Column
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {
            PageStart(title = "Syndere")
        }

        /*items(userList) { it ->
            Chat(it, curRoute, navController)
        }*/

        item {
            PageEnd(textcontent = "Ingen flere Syndere!")
        }
    }
}


@Composable
fun conversationWindow(
    chatId: String,
    chat: ChatAndParticipant, // Assuming ChatandParticipant has a parameterless constructor
    messages: List<MessagesFromFirebase>,
    modifier: Modifier = Modifier,
    chatViewModel: ChatViewModel = hiltViewModel()
) {
    //Log.d("TESTCHAT1", "$chat")
    Log.d("TESTCHAT12", "${chatId}")
    Log.d("TESTCHAT13", "${chat}")
    Log.d("TEST HVOR MANGE MELDINGER1", "${chat.chat.messages}")
    Log.d("TEST HVOR MANGE MELDINGER12", "${chat.chat.messages.size}")

    val displayMessages = messages.map { firebaseMessage ->
        val userProfile = chatViewModel.usersCache[firebaseMessage.userId]
        val sentByUser = firebaseMessage.userId == chatViewModel.userId

        Message(
            id = firebaseMessage.id,
            userId = userProfile?.name ?: "Ukjent bruker",
            userProfile = userProfile ?: UserProfile(),
            text = firebaseMessage.text,
            sent = firebaseMessage.sent,
            sentByUser = sentByUser
        )
    }
    Log.d("TEST HVOR MANGE MELDINGER", "${displayMessages.size}")
    Log.d("TEST HVOR MANGE MELDINGER", "${displayMessages}")

    LazyColumn(modifier = modifier.fillMaxSize()) {
        if (displayMessages.isNotEmpty()) {

            items(displayMessages) { message ->
                Message(it = message)
            }
        }

        item {
            val textContent = if (displayMessages.isNotEmpty()) {
                "Siste melding ${displayMessages.last().sent}"
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