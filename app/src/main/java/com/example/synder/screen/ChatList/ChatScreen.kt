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
import com.example.synder.components.ChatCard
import com.example.synder.components.Message
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.synder.Screen
import com.example.synder.components.MatchCard
import com.example.synder.models.ChatAndParticipant
import com.example.synder.models.UserProfile

@Composable
fun chatScreen(getChatFromClick: (String, ChatAndParticipant) -> Unit,
               isDarkTheme: Boolean,
               navController: NavHostController,
               userInChat: ChatAndParticipant,
               chatViewModel: ChatViewModel = hiltViewModel(),
               modifier: Modifier = Modifier
) {
    val chatsList = chatViewModel.chatsCache.values.toList()
    val storageRef = chatViewModel.storageRef

    LazyColumn( modifier = Modifier.fillMaxSize() ) {
        item {
            PageStart(title = "Chats")
        }
        items(chatsList) { it ->
            ChatCard(onChatClick = getChatFromClick, it, storageRef , navController)
        }

        item {
            PageEnd(textcontent = "Her var det tomt. Start en chat først!")
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
    val matchList = chatViewModel.matchesCache.values.toList()
    val chatsList = chatViewModel.chatsCache.values.toList()

    val storageRef = chatViewModel.storageRef
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {
            PageStart(title = "Syndere")
        }

        items(matchList) { match ->
            var isNull by remember { mutableStateOf(false) }
            val currentUserChats = chatViewModel.currentUser.value.chats ?: emptyList()
            val matchChats = match.chats ?: emptyList()

            val commonChats = currentUserChats.intersect(matchChats)

            var chatForMatch by remember { mutableStateOf<ChatAndParticipant?>(null) }

            val theID = commonChats.firstOrNull()
            if (theID != null) {
                chatForMatch = chatsList.firstOrNull { it.id == theID }
            }

            MatchCard(
                getChatFromClick = getChatFromClick,
                isDarkTheme = isDarkTheme,
                ifChatIsNull = { isNull = true },
                it = match,
                storageRef,
                hasChat = chatForMatch,
                navController = navController
            )

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
    var showLast = false
    val messages by chatViewModel.messages.collectAsState()
    val sortedMessages = messages.sortedBy { it.index }

    val storageRef = chatViewModel.storageRef

    LaunchedEffect(chatId) {
        chatViewModel.fetchMessages(chatId)
        if (chat.latestsender.isNotEmpty() && !chat.latestsender.equals(chatViewModel.userId)) {
            if (chat.latestmessage.isNotEmpty()) {
                chatViewModel.readChat(chat.id)
                showLast = true
            }
        }
    }

    LazyColumn(modifier = modifier.fillMaxSize()) {
        if (messages.isNotEmpty()) {

            items(sortedMessages) { firebaseMessage ->
                val sentByUser = firebaseMessage.userId == chatViewModel.userId

                val userProf = when (firebaseMessage.userId) {
                    chat.user1.id -> chat.user1
                    chat.user2.id -> chat.user2
                    else -> UserProfile(name = "Ukjent")
                }
                Message(
                    it = firebaseMessage,
                    storageRef,
                    userProfile = userProf,
                    sentByUser = sentByUser
                )
            }

            if (showLast) {
                item {
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
        } else {
            item {
                Text(text = "Her var det tomt. Vær den første til å sende en melding!")
            }
        }
    }
}

@Composable
fun PageStart (title: String) {
    Column (horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = 10.dp, start = 20.dp, end = 20.dp, bottom = 5.dp)) {
        Text(text = "Nylige ${title}", fontSize = 32.sp, modifier = Modifier.padding(bottom = 5.dp),
            )
        Divider(
            thickness = 2.dp,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}
@Composable
fun PageEnd (textcontent: String) {
    Column (horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(20.dp)) {
        Divider(
            thickness = 2.dp,
            modifier = Modifier
                .fillMaxWidth()
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
