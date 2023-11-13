package com.example.synder.screen.ChatList;

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.synder.Screen
import com.example.synder.models.Chat // Make sure to import the correct Chat model
import com.example.synder.models.ChatAndParticipant
import com.example.synder.models.ChatsFromFirebase
import com.example.synder.models.Message
import com.example.synder.models.UserProfile
import com.example.synder.service.AccountService
import com.example.synder.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChatViewModel @Inject constructor(
        private val storageService: StorageService,
        val accountService: AccountService
        ) : ViewModel() {
        val chat = mutableStateOf(ChatsFromFirebase())
        val user1 = mutableStateOf(UserProfile())
        val user2 = mutableStateOf(UserProfile())
        val chats = MutableStateFlow<List<ChatsFromFirebase>>(emptyList())

        var currentChatClicked = mutableStateOf<ChatAndParticipant?>(null)

        val usersCache: MutableMap<String, UserProfile> = mutableMapOf()
        val chatsCache: MutableMap<String, ChatAndParticipant> = mutableMapOf()

        init {
                viewModelScope.launch {
                        storageService.users.collect { userList ->
                                userList.forEach { user ->
                                        usersCache[user.id] = user
                                        Log.d("ID1: ", user.toString())
                                        Log.d("ID2: ", usersCache.toString())
                                }
                        }
                }
                viewModelScope.launch {
                        // Fetch the current user ID
                        val currentUserId = accountService.currentUserId

                        storageService.chats.collect { chatList ->
                                // Filter the chatList to only include chats where the current user is either user1 or user2
                                val relevantChats = chatList.filter { chat ->
                                        chat.userId1 == currentUserId || chat.userId2 == currentUserId
                                }

                                relevantChats.forEach { chat ->
                                        val chatWithParticipant = ChatAndParticipant(
                                                id = chat.id,
                                                chat = chat,
                                                user1 = usersCache[chat.userId1] ?: UserProfile(),
                                                user2 = usersCache[chat.userId2] ?: UserProfile(),
                                                latestmessage = chat.latestmessage.toString(),
                                        )

                                        // Update the cache with the relevant chats only
                                        chatsCache[chat.id] = chatWithParticipant
                                        Log.d("ID3: ", chatWithParticipant.toString())
                                }

                                // If you need to log the entire cache, do it outside the forEach loop
                                Log.d("ChatsCache: ", chatsCache.toString())
                        }
                }
        }

        suspend fun getChatList(): List<ChatAndParticipant> {
                val chatAndParticipantList = mutableListOf<ChatAndParticipant>()

                storageService.chats.collect { chatList ->
                        chatList.forEach { chat ->
                                val chot = ChatAndParticipant(
                                        id = chat.id,
                                        chat = chat,
                                        user1 = usersCache[chat.userId1] ?: UserProfile(),
                                        user2 = usersCache[chat.userId2] ?: UserProfile(),
                                        latestmessage = chat.latestmessage.toString()
                                )
                                chatAndParticipantList.add(chot)

                                // Legg til i cache om nødvendig
                                chatsCache[chat.id] = chot
                        }
                }

                return chatAndParticipantList
        }


        fun getUserById(userId: String) {
                //val userId = "7gM3MG2HlhELDG3pTYqu"
                viewModelScope.launch {
                        user1.value = storageService.getUser(userId) ?: UserProfile()
                }
        }
        fun getChatById(chatId: String) {
                val chatId = "YhsAJ6tRK4S4QDOcaZ2n"
                viewModelScope.launch {
                        chat.value = storageService.getChat(chatId) ?: ChatsFromFirebase()
                }
        }
        fun getChatAndUsersById(chatId: String) {
                viewModelScope.launch {
                        chat.value = storageService.getChat(chatId) ?: ChatsFromFirebase()
                        Log.d("Chat", "Chatid: " + chatId + " " + chat.toString())
                        user1.value = usersCache[chat.value.userId1] ?: UserProfile()
                        user2.value = usersCache[chat.value.userId2] ?: UserProfile()

                        Log.d("Bruker fra firebase", user1.value.toString())
                }
        }

        suspend fun getChatByIdAndCurrentUserId(userId: String) {
                // Hent currentUserId asynkront om nødvendig
                val currentUserId = accountService.currentUserId

                // Bruk en coroutine for å utføre søket asynkront
                coroutineScope {
                        // Finn og tilordne den første chatten som matcher betingelsene
                        currentChatClicked.value = chatsCache.values.firstOrNull { chatAndParticipant ->
                                (chatAndParticipant.user1.id == userId && chatAndParticipant.user2.id == currentUserId) ||
                                        (chatAndParticipant.user2.id == userId && chatAndParticipant.user1.id == currentUserId)
                        }
                }
        }

        // Denne funksjonen starter en coroutine og utfører de nødvendige operasjonene
        fun onChatClicked(chatId: String, navController: NavController, currentChatAndParticipant: ChatAndParticipant) {
                viewModelScope.launch {
                        getChatByIdAndCurrentUserId(chatId)
                        currentChatAndParticipant = currentChatClicked.value
                        navController.navigate(Screen.Chat.name) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                }
                                launchSingleTop = true
                        }
                }
        }

}
