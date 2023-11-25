package com.example.synder.screen.ChatList;

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

        val currentUser = mutableStateOf(UserProfile())

        val userId = accountService.currentUserId

        private val currentChatIdClicked = mutableStateOf("")

        private val currentChatClicked = mutableStateOf(ChatAndParticipant())

        val usersCache: MutableMap<String, UserProfile> = mutableMapOf()
        val chatsCache: MutableMap<String, ChatAndParticipant> = mutableMapOf()
        val messagesCache: MutableMap<String, List<MessagesFromFirebase>> = mutableMapOf()
        val matchesCache: MutableMap<String, UserProfile> = mutableMapOf()

        val matchChatCache: MutableMap<String, ChatAndParticipant?> = mutableMapOf()

        var messageCounter = 0

        private val _messages = MutableStateFlow<List<MessagesFromFirebase>>(emptyList())
        val messages: StateFlow<List<MessagesFromFirebase>> = _messages.asStateFlow()

        val storageRef = imgStorageService.storageRef

        init {
                viewModelScope.launch {
                        storageService.users.collect { userList ->
                                val currentUserId = accountService.currentUserId
                                val currentUserProfile = userList.find { it.id == currentUserId }
                                if (currentUserProfile != null) {
                                        currentUser.value = currentUserProfile
                                }
                                userList.forEach { user ->
                                        usersCache[user.id] = user

                                        if (currentUserProfile?.matches?.contains(user.id) == true) {
                                                matchesCache[user.id] = user

                                                val chatWithMatch = userHasChat(user.id)
                                                matchChatCache[user.id] = chatWithMatch
                                        }
                                }
                        }
                }
                viewModelScope.launch {
                        storageService.chats.collect { chatList ->
                                val userChatIds = usersCache[userId]?.chats ?: emptyList()

                                val relevantChats = chatList.filter { chat ->
                                        chat.id in userChatIds
                                }

                                relevantChats.forEach { chat ->
                                        val messages = storageService.getMessagesForChat(chat.id)
                                        val chatWithParticipant = ChatAndParticipant(
                                                id = chat.id,
                                                chat = chat,
                                                user1 = usersCache[chat.userId1] ?: UserProfile(),
                                                user2 = usersCache[chat.userId2] ?: UserProfile(),
                                                latestmessage = chat.latestmessage,
                                                latestsender = chat.latestsender,
                                        )
                                        messagesCache[chat.id] = messages
                                        chatsCache[chat.id] = chatWithParticipant
                                }
                        }
                }
        }

        fun getOtherUserId(chat: ChatAndParticipant): UserProfile {
                return if (chat.user1.id == userId) {
                        chat.user1
                } else {
                        chat.user2
                }
        }


        fun userHasChat(chatUserId: String): ChatAndParticipant? {

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

        fun sendMessage(messageText: String) {
                viewModelScope.launch {

                        val userId = accountService.currentUserId
                        val currentTime = System.currentTimeMillis()
                        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                        val timeString = dateFormat.format(Date(currentTime))
                        val message = MessagesFromFirebase(
                                sent = timeString.toString(),
                                text = messageText,
                                userId = userId,
                                index = 0,
                        )

                        val success = storageService.sendMessage(currentChatIdClicked.value, message)

                        if(success) {
                        } else {
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


        suspend fun createNewChat(userFromChat: String): Pair<String, ChatAndParticipant>? {
                val newChat = ChatsFromFirebase(
                        latestmessage = "",
                        latestsender = "",
                        userId1 = userId,
                        userId2 = userFromChat
                )

                return try {
                        val chatId = storageService.createChat(newChat)
                        val newChatAndParticipant = ChatAndParticipant(
                                id = chatId,
                                chat = newChat,
                                user1 = usersCache[newChat.userId1] ?: UserProfile(),
                                user2 = usersCache[newChat.userId2] ?: UserProfile(),
                                latestmessage = newChat.latestmessage
                        )

                        chatsCache[chatId] = newChatAndParticipant
                        currentChatClicked.value = newChatAndParticipant
                        currentChatIdClicked.value = chatId
                        Pair(chatId, newChatAndParticipant)
                } catch (e: Exception) {
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
}
