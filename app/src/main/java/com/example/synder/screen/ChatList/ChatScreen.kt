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
import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.synder.Screen
import com.example.synder.components.MatchCard
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.synder.models.ChatAndParticipant
import com.example.synder.models.FromFirebase.MessagesFromFirebase
import com.example.synder.models.UserProfile

@Composable
fun chatScreen(getChatFromClick: (String, ChatAndParticipant) -> Unit,
               isDarkTheme: Boolean,
               navController: NavHostController,
               userInChat: ChatAndParticipant,
               chatViewModel: ChatViewModel = hiltViewModel(), modifier: Modifier = Modifier

) {
    val chatsList = chatViewModel.chatsCache.values.toList()

    val userChats = listOf(chatsList)

    Log.d("CHATLIST: ", chatsList.toString())

    val storageRef = chatViewModel.storageRef

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            //.background(Color.White)
    ) {
        item {
            PageStart(title = "Chats")
        }

        items(chatsList) { it ->
            Chat(onChatClick = getChatFromClick, it, storageRef , navController)
        }

        item {
            PageEnd(textcontent = "Her var det tomt. Skaff en match!")
        }
    }

}
@Composable
fun matchScreen(
    getChatFromClick: (String, ChatAndParticipant) -> Unit,
    isDarkTheme: Boolean,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    chatViewModel: ChatViewModel = hiltViewModel()
) {
    /*val matches by chatViewModel.matches.collectAsState()

    Log.d("Matches: ", matches.toString())

    LaunchedEffect(chatViewModel.userId) {
        chatViewModel.fetchMatches(chatViewModel.userId)
    }
    Log.d("Matches 2:  ", chatViewModel.matchesCache.toString())*/
    val matchList = chatViewModel.matchesCache.values.toList()
    val chatMatchCache = chatViewModel.matchChatCache
    Log.d("P!:Kommer chatter?", chatMatchCache.toString())

    val userList = chatViewModel.usersCache.values.toList()
    Log.d("Liste med USERS:", "$userList")


    val storageRef = chatViewModel.storageRef
    //Column
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {
            PageStart(title = "Syndere")
        }

        items(matchList) { match ->
            // Finn matchens ID
            val matchId = match.id
            var isNull by remember { mutableStateOf(false) }

            val chatForMatch = chatMatchCache[matchId]

            MatchCard(
                getChatFromClick = getChatFromClick,
                ifChatIsNull = { isNull = true },
                it = match,
                storageRef,
                hasChat = chatForMatch,
                navController = navController
            )

            // Hvis shouldCreateChat er true, håndter oppretting av ny chat
            if (isNull) {
                LaunchedEffect(match.id) {
                    val result = chatViewModel.createNewChat(match.id)
                    result?.let { (chatId, chatAndParticipant) ->
                        getChatFromClick(chatId, chatAndParticipant)
                        navController.navigate(Screen.Chat.name) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                        }
                    }
                }
            }
        }


        item {
            PageEnd(textcontent = "Ingen flere Syndere!")
        }
    }
}


@Composable
fun conversationWindow(
    chatId: String,
    chat: ChatAndParticipant,
    modifier: Modifier = Modifier,
    chatViewModel: ChatViewModel = hiltViewModel()
) {
    if (chat.latestmessage.isNotEmpty()) {
        chatViewModel.readChat(chat.id)
    }
    val messages by chatViewModel.messages.collectAsState()
    chatViewModel.updateMessageCounter(messages.size - 1)
    val sortedMessages = messages.sortedBy { it.index }

    val storageRef = chatViewModel.storageRef
    // Bruk LaunchedEffect for å hente meldinger når Composable-funksjonen først blir vist
    LaunchedEffect(chatId) {
        chatViewModel.fetchMessages(chatId)
    }

    LazyColumn(modifier = modifier.fillMaxSize()) {
        if (messages.isNotEmpty()) {

            items(sortedMessages) { firebaseMessage ->
                val sentByUser = firebaseMessage.userId == chatViewModel.userId
                val userProf = when (firebaseMessage.userId) {
                    chat.user1.id -> chat.user1
                    chat.user2.id -> chat.user2
                    else -> UserProfile(name = "Ukjent") // Eller en annen default UserProfile hvis ID ikke matcher
                }
                Message(
                    it = firebaseMessage,
                    storageRef,
                    userProfile = userProf,
                    sentByUser = sentByUser
                )
            }
        }
        
        item {
            if (chat.latestmessage.isEmpty() && chat.latestsender !== chatViewModel.userId) {
                Text(text = " Lest", fontSize = 16.sp)
            }
        }

        item {
            val textContent = if (messages.isNotEmpty()) {
                "Siste melding ${messages.last().sent}"
            } else {
                "Her var det tomt. Start samtalen da vel!"
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
