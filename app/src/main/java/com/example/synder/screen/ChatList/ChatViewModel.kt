package com.example.synder.screen.ChatList;

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.synder.models.Chat // Make sure to import the correct Chat model
import com.example.synder.models.ChatsFromFirebase
import com.example.synder.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
        private val storageService: StorageService
        ) : ViewModel() {
        val chat = mutableStateOf(ChatsFromFirebase())
        init {
                val chatId = "7gM3MG2HlhELDG3pTYqu" // Replace with your chat ID
                viewModelScope.launch {
                        chat.value = storageService.getChat(chatId) ?: ChatsFromFirebase()
                }
        }
}
