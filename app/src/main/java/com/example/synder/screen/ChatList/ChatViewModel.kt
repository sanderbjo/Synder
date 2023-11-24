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
import com.example.synder.service.ImgStorageService
import com.example.synder.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class ChatViewModel @Inject constructor(
        private val storageService: StorageService,
        private val accountService: AccountService,
        private val imgStorageService: ImgStorageService
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
        val matchesCache: MutableMap<String, UserProfile> = mutableMapOf()

        val matchChatCache: MutableMap<String, ChatAndParticipant?> = mutableMapOf()

        val matchesFlow = MutableStateFlow<List<UserProfile>>(emptyList())
        fun updateMatchesFlow() {
                matchesFlow.value = matchesCache.values.toList()
        }

        var messageCounter = 0

        //TEST FOR LIVE ACTION HENTING AV DATA
        private val _messages = MutableStateFlow<List<MessagesFromFirebase>>(emptyList())
        val messages: StateFlow<List<MessagesFromFirebase>> = _messages.asStateFlow()

        private val _matches = MutableStateFlow<List<UserProfile>>(emptyList())
        val matches: StateFlow<List<UserProfile>> = _matches.asStateFlow()

        val storageRef = imgStorageService.storageRef

        init {
                viewModelScope.launch {
                        storageService.users.collect { userList ->
                                val currentUserId = accountService.currentUserId
                                val currentUserProfile = userList.find { it.id == currentUserId }

                                userList.forEach { user ->
                                        usersCache[user.id] = user

                                        // Sjekk om brukeren er en match for nåværende bruker
                                        if (currentUserProfile?.matches?.contains(user.id) == true) {
                                                matchesCache[user.id] = user

                                                // Sjekk om det finnes en chat mellom nåværende bruker og matchen
                                                val chatWithMatch = userHasChat(user.id)
                                                matchChatCache[user.id] = chatWithMatch
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

        fun userHasChat(chatUserId: String): ChatAndParticipant? {

                // Finn en chat hvor begge brukerne er involvert
                val foundChat = chatsCache.values.firstOrNull { chatAndParticipant ->
                        (chatAndParticipant.chat.userId1 == userId && chatAndParticipant.chat.userId2 == chatUserId) ||
                                (chatAndParticipant.chat.userId1 == chatUserId && chatAndParticipant.chat.userId2 == userId)
                }

                return foundChat
        }


        fun readChat(chatId: String) {
                viewModelScope.launch {
                        storageService.readChat(chatId = chatId)
                }
        }

        fun updateMessageCounter(index: Int) {
                messageCounter = index
        }
/*
        fun getMessageCounter(): Int {
                return 0
        }*/

        fun sendMessage(messageText: String) {
                viewModelScope.launch {

                        val userId = accountService.currentUserId
                        val currentTime = System.currentTimeMillis()
                        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                        val timeString = dateFormat.format(Date(currentTime))
                        Log.d("CURRTIME: ", timeString)

                        val message = MessagesFromFirebase(
                                sent = timeString.toString(),
                                text = messageText,
                                userId = userId,
                                index = 0, ///PLACEHOLDER, SKAL ENDRES I STORAGESERVICE
                        )

                        Log.d("CHAT1 id to send: ", currentChatIdClicked.value.toString())
                        Log.d("CHAT1 message to send: ", message.toString())
                        val success = storageService.sendMessage(currentChatIdClicked.value, message)

                        if(success) {
                                // Meldingen ble sendt suksessfullt
                                Log.d("CHAT SUKSESS:", success.toString())
                        } else {
                                Log.d("CHAT FAIL:", success.toString())
                                // Håndter feil
                        }
                }
        }

        fun fetchMessages(chatId: String) {
                viewModelScope.launch {
                        var index = 0
                        storageService.getMessagesFlowForChat(chatId).collect { messageList ->
                                _messages.value = messageList
                                index ++
                        }
                        updateMessageCounter(index)
                }
        }
        fun fetchMatches(userId: String) {
                viewModelScope.launch {
                        storageService.getMatchesFlowForUser(userId).collect { matchList ->
                                _matches.value = matchList
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

        /*
        * FOKUS PUKUS HJÆVA
        *
        *
        *
        *
        *
        *
        *
        *
        *
        *
        *
        *
        *
        *
        *
        *
        *
        *
        *
        *
        *
        *
        *
        *
        *
        *
        *
        *
        *
        *
        *
        *
        *
        *
        *
        *
        *
        *
        *
        *
        fun findOrCreateChatWithUser(chatUserId: String) {
                viewModelScope.launch {
                        val currentUserId = accountService.currentUserId
                        // Finn eksisterende chat hvor begge brukerne er en del av
                        val existingChat = chatsCache.values.firstOrNull { chat ->
                                (chat.user1.id == currentUserId && chat.user2.id == chatUserId) ||
                                        (chat.user2.id == currentUserId && chat.user1.id == chatUserId)
                        }

                        if (existingChat != null) {
                                // Chat finnes, kan hente meldinger eller gjøre andre nødvendige handlinger
                                updateCurrentChat(existingChat)
                        } else {
                                // Ingen eksisterende chat, opprett en ny
                                createNewChat(currentUserId, chatUserId)
                        }
                }
        }
        * */

        fun checkIfExists(chatUserId: String) {
                viewModelScope.launch {
                        val existingChat = chatsCache.values.firstOrNull { chat ->
                                (chat.user1.id == userId && chat.user2.id == chatUserId) ||
                                        (chat.user2.id == userId && chat.user1.id == chatUserId)
                        }

                        if (existingChat == null) {
                                // Ingen eksisterende chat, forsøk å opprette en ny
                                createNewChat(chatUserId)?.let { (newChatId, newChatAndParticipant) ->
                                        // Oppdaterer ViewModel-tilstanden med den nye chatten
                                        currentChatClicked.value = newChatAndParticipant
                                        currentChatIdClicked.value = newChatId
                                } ?: run {
                                        // Håndter tilfellet hvor det ikke var mulig å opprette en ny chat
                                        // For eksempel, oppdater en feilmelding i ViewModel eller trigger en event
                                }
                        } else {
                                // En eksisterende chat finnes, oppdater currentChat
                                currentChatClicked.value = existingChat
                                currentChatIdClicked.value = existingChat.id
                        }
                }
        }


        suspend fun createNewChat(userFromChat: String): Pair<String, ChatAndParticipant>? {
                val newChat = ChatsFromFirebase(
                        latestmessage = "",
                        latestsender = "",
                        userId1 = userId,
                        userId2 = userFromChat
                )

                return try {
                        // Lagrer den nye chatten i databasen og henter chatId
                        val chatId = storageService.createChat(newChat) // Returnerer nå ID-en til den nye chatten

                        // Oppretter et nytt ChatAndParticipant-objekt
                        val newChatAndParticipant = ChatAndParticipant(
                                id = chatId,
                                chat = newChat,
                                user1 = usersCache[newChat.userId1] ?: UserProfile(),
                                user2 = usersCache[newChat.userId2] ?: UserProfile(),
                                latestmessage = newChat.latestmessage
                        )

                        // Legger til den nye chatten i cache og oppdaterer currentChat
                        chatsCache[chatId] = newChatAndParticipant
                        currentChatClicked.value = newChatAndParticipant
                        currentChatIdClicked.value = chatId

                        // Returnerer Pair av chatId og ChatAndParticipant
                        Pair(chatId, newChatAndParticipant)
                } catch (e: Exception) {
                        // Logg eller håndter feilen
                        null
                }
        }


        fun updateCurrentChat(newCurrentChat: ChatAndParticipant): Boolean {
                currentChatClicked.value = newCurrentChat
                currentChatIdClicked.value = newCurrentChat.id
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
