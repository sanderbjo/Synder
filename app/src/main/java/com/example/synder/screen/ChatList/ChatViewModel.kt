package com.example.synder.screen.ChatList;

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.synder.models.ChatAndParticipant
import com.example.synder.models.FromFirebase.ChatsFromFirebase
import com.example.synder.models.FromFirebase.MessagesFromFirebase
import com.example.synder.models.UserProfile
import com.example.synder.service.AccountService
import com.example.synder.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class ChatViewModel @Inject constructor(
        private val storageService: StorageService,
        val accountService: AccountService
        ) : ViewModel() {
        val chat = mutableStateOf(ChatsFromFirebase())

        val chats = MutableStateFlow<List<ChatsFromFirebase>>(emptyList())

        val userId = accountService.currentUserId

        val user1 = mutableStateOf(UserProfile())
        val user2 = mutableStateOf(UserProfile())
        private val currentChatIdClicked = mutableStateOf("")

        private val currentChatClicked = mutableStateOf(ChatAndParticipant())

        val usersCache: MutableMap<String, UserProfile> = mutableMapOf()
        val chatsCache: MutableMap<String, ChatAndParticipant> = mutableMapOf()
        val messagesCache: MutableMap<String, List<MessagesFromFirebase>> = mutableMapOf()
        private var messageCounter = 0

        //TEST FOR LIVE ACTION HENTING AV DATA
        private val _messages = MutableStateFlow<List<MessagesFromFirebase>>(emptyList())
        val messages: StateFlow<List<MessagesFromFirebase>> = _messages.asStateFlow()

        init {
                viewModelScope.launch {
                        storageService.users.collect { userList ->
                                userList.forEach { user ->
                                        usersCache[user.id] = user
                                        Log.d("TESTBR1: ", user.toString())
                                        Log.d("TESTBR2: ", usersCache.toString())
                                        if (accountService.currentUserId === user.id) {
                                                Log.d("TEST userloggedin: ", accountService.currentUserId)
                                                Log.d("TEST userloggedin: ", userId)
                                        }
                                }
                        }
                }
                viewModelScope.launch {
                        val currentUserId = accountService.currentUserId

                        storageService.chats.collect { chatList ->
                                val relevantChats = chatList.filter {
                                        it.userId1 == currentUserId || it.userId2 == currentUserId
                                }
                                Log.d("CHATS ALL RELEVANT", relevantChats.toString())

                                relevantChats.forEach { chat ->
                                        // Anta at storageService har en funksjon for å hente meldinger for en gitt chat
                                        val messages = storageService.getMessagesForChat(chat.id)
                                        val chatWithParticipant = ChatAndParticipant(
                                                id = chat.id,
                                                chat = chat,
                                                user1 = usersCache[chat.userId1] ?: UserProfile(),
                                                user2 = usersCache[chat.userId2] ?: UserProfile(),
                                                latestmessage = chat.latestmessage.toString(),
                                                latestsender = chat.latestsender.toString(),
                                        )
                                        messagesCache[chat.id] = messages
                                        chatsCache[chat.id] = chatWithParticipant
                                        Log.d("CHATS single has data?: ", chat.latestsender)
                                }
                                Log.d("ChatsCache: ", chatsCache.toString())
                        }
                }

        }

        fun getMatches(userId: String) { //skal kun gjelde for current bruker?
                viewModelScope.launch {

                }
        }

        fun readChat(chatId: String) {
                viewModelScope.launch {
                        storageService.readChat(chatId = chatId)
                }
        }

        fun updateMessageCounter(index: Int) {
                messageCounter = index
        }

        fun sendMessage(messageText: String) {
                viewModelScope.launch {

                        val userId = accountService.currentUserId
                        val currentTime = System.currentTimeMillis()
                        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                        val timeString = dateFormat.format(Date(currentTime))
                        Log.d("CURRTIME: ", timeString)

                        val index = messageCounter

                        val message = MessagesFromFirebase(
                                // Du trenger ikke å sette ID her, fordi Firebase vil automatisk generere den
                                sent = timeString.toString(),
                                text = messageText,
                                userId = userId,
                                index = index,
                        )

                        Log.d("CHAT1 id to send: ", currentChatIdClicked.value.toString())
                        Log.d("CHAT1 message to send: ", message.toString())
                        val success = storageService.sendMessage(currentChatIdClicked.value, message)

                        if(success) {
                                // Meldingen ble sendt suksessfullt
                                Log.d("CHAT SUKSESS:", success.toString())
                                messageCounter++
                        } else {
                                Log.d("CHAT FAIL:", success.toString())
                                // Håndter feil
                        }
                }
        }

        fun fetchMessages(chatId: String) {
                viewModelScope.launch {
                        storageService.getMessagesFlowForChat(chatId).collect { messageList ->
                                _messages.value = messageList
                        }
                }
        }


        suspend fun getMessages(chatId: String, getAsync: Boolean = false): List<MessagesFromFirebase> {
                // Returnerer cachede meldinger hvis de er lageret i listen fra før
                if (!getAsync) {
                        messagesCache[chatId]?.let { cachedMessages ->
                                return cachedMessages
                        }
                }

                // Henter meldinger asynkront hvis de ikke er cachet
                return try {
                        val messages = storageService.getMessagesForChat(chatId)
                        messagesCache[chatId] = messages
                        messages
                } catch (e: Exception) {
                        emptyList()
                }
        }

        fun getCurrentId(): String {
                Log.d("TEST HAS HORE1: ", "${currentChatIdClicked.value}")
                return currentChatIdClicked.value;
        }
        fun getCurrentChat(): ChatAndParticipant {
                Log.d("TEST HAS HORE2: ", "${currentChatClicked.value}")
                return currentChatClicked.value;
        }
        fun updateCurrentChat(newCurrentChat: ChatAndParticipant): Boolean {
                Log.d("TEST HAS CLICKED: ", "${currentChatClicked.value}")
                Log.d("TEST HAS CLICKED: ", "${currentChatIdClicked.value}")
                currentChatClicked.value = newCurrentChat
                currentChatIdClicked.value = newCurrentChat.id

                Log.d("TEST HAS CLICKED: ", "TRUE")
                Log.d("TEST HAS CLICKED: ", "${currentChatClicked.value}")
                Log.d("TEST HAS CLICKED: ", "${currentChatIdClicked.value}")
                return true;
        }

        fun upadeCurrentId (newCurrentId: String) {
                currentChatIdClicked.value = newCurrentId;
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
                //val chatId = "YhsAJ6tRK4S4QDOcaZ2n"
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
        fun getChatByIdAndCurrentUserId(userId: String): ChatAndParticipant? {
                val currentUserId = accountService.currentUserId

                // Returnerer første chat som matcher betingelsene, eller null hvis ingen matcher
                return chatsCache.values.firstOrNull { chatAndParticipant ->
                        // Sjekker om gitt userId matcher enten user1 eller user2, og om currentUserId matcher den andre brukeren
                        (chatAndParticipant.user1.id == userId && chatAndParticipant.user2.id == currentUserId) ||
                                (chatAndParticipant.user2.id == userId && chatAndParticipant.user1.id == currentUserId)
                }
        }
}
