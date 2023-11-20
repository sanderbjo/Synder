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

        init {
                viewModelScope.launch {
                        storageService.users.collect { userList ->
                                userList.forEach { user ->
                                        usersCache[user.id] = user
                                        Log.d("TESTBR1: ", user.toString())
                                        Log.d("TESTBR2: ", usersCache.toString())
                                }
                        }
                }
                viewModelScope.launch {
                        val currentUserId = accountService.currentUserId

                        storageService.chats.collect { chatList ->
                                val relevantChats = chatList.filter {
                                        it.userId1 == currentUserId || it.userId2 == currentUserId
                                }

                                relevantChats.forEach { chat ->
                                        // Anta at storageService har en funksjon for å hente meldinger for en gitt chat
                                        val messages = storageService.getMessagesForChat(chat.id)
                                        val chatWithParticipant = ChatAndParticipant(
                                                id = chat.id,
                                                chat = chat,
                                                user1 = usersCache[chat.userId1] ?: UserProfile(),
                                                user2 = usersCache[chat.userId2] ?: UserProfile(),
                                                latestmessage = chat.latestmessage.toString(),
                                        )

                                        chatsCache[chat.id] = chatWithParticipant
                                        Log.d("TESTCHC11: ", chatWithParticipant.toString())
                                        Log.d("TESTCHC12: ", chat.id)
                                        Log.d("TESTCHSPES5: ", messages.toString())
                                }
                                Log.d("ChatsCache: ", chatsCache.toString())
                        }
                }

                /*
                viewModelScope.launch {
                        storageService.messages.collect { messageList ->
                                chatsCache.forEach { (chatId, chat) ->
                                        val messages = storageService.getMessagesForChat(chatId)
                                        messagesCache[chatId] = messages
                                }
                        }
                }*/

        }

        fun sendMessage(messageText: String) {
                viewModelScope.launch {

                        val userId = accountService.currentUserId
                        val currentTime = System.currentTimeMillis()
                        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                        val timeString = dateFormat.format(Date(currentTime))
                        Log.d("CURRTIME: ", timeString)


                        val message = MessagesFromFirebase(
                                // Du trenger ikke å sette ID her, fordi Firebase vil automatisk generere den
                                sent = currentTime.toString(),
                                text = messageText,
                                userId = userId
                        )
                        Log.d("CHAT message to send: ", message.toString())
                        val success = storageService.sendMessage(currentChatIdClicked.value, message)

                        if(success) {
                                // Meldingen ble sendt suksessfullt
                                Log.d("CHAT SUKSESS:", success.toString())
                        } else {
                                Log.d("CHAT FAIL:", success.toString())
                                // Håndter feil
                        }
                        /**/
                }
        }


        suspend fun getMessages(chatId: String): List<MessagesFromFirebase> {
                // Returnerer cachede meldinger hvis tilgjengelig
                messagesCache[chatId]?.let { cachedMessages ->
                        return cachedMessages
                }

                // Henter meldinger asynkront hvis de ikke er cachet
                return try {
                        val messages = storageService.getMessagesForChat(chatId)
                        messagesCache[chatId] = messages // Oppdaterer cachen
                        messages // Returnerer de nye meldingene
                } catch (e: Exception) {
                        // Logg feilen eller håndter den som nødvendig
                        emptyList() // Returnerer en tom liste hvis det oppstår en feil
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
        fun getChatByIdAndCurrentUserId(userId: String): ChatAndParticipant? {
                val currentUserId = accountService.currentUserId

                // Returnerer første chat som matcher betingelsene, eller null hvis ingen matcher
                return chatsCache.values.firstOrNull { chatAndParticipant ->
                        // Sjekker om gitt userId matcher enten user1 eller user2, og om currentUserId matcher den andre brukeren
                        (chatAndParticipant.user1.id == userId && chatAndParticipant.user2.id == currentUserId) ||
                                (chatAndParticipant.user2.id == userId && chatAndParticipant.user1.id == currentUserId)
                }
        }

        /*suspend fun getChatByIdAndCurrentUserId(userId: String) {
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
        }*/



        // Denne funksjonen starter en coroutine og utfører de nødvendige operasjonene
        /*fun onChatClicked(chatId: String, navController: NavController, currentChatAndParticipant: ChatAndParticipant) {
                viewModelScope.launch {
                        getChatByIdAndCurrentUserId(chatId)
                        val chatAndParticipant = currentChatClicked.value
                        if (chatAndParticipant != null) {
                                currentChatAndParticipant = chatAndParticipant
                                navController.navigate(Screen.Chat.name) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                        }
                                        launchSingleTop = true
                                }
                        }
                }
        }*/


}
