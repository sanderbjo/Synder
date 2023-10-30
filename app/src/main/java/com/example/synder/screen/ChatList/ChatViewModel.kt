package com.example.synder.screen.ChatList;

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.synder.models.Chat // Make sure to import the correct Chat model
import com.example.synder.models.ChatsFromFirebase
import com.example.synder.models.UserProfile
import com.example.synder.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
        private val storageService: StorageService
        ) : ViewModel() {
        val chat = mutableStateOf(ChatsFromFirebase())
        val user1 = mutableStateOf(UserProfile())
        val user2 = mutableStateOf(UserProfile())

        private val usersCache: MutableMap<String, UserProfile> = mutableMapOf()

        init {
                viewModelScope.launch {
                        storageService.users.collect { userList ->
                                userList.forEach { user ->
                                        usersCache[user.id] = user
                                        Log.d("Users: ", user.toString())
                                        Log.d("Users: ", usersCache.toString())
                                }
                        }
                }
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

        /*
        init {
                val chatId = "7gM3MG2HlhELDG3pTYqu" // Replace with your chat ID
                viewModelScope.launch {
                        chat.value = storageService.getChat(chatId) ?: ChatsFromFirebase()
                }
        }*/
}
