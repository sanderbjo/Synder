package com.example.synder.service

import com.example.synder.models.FromFirebase.ChatsFromFirebase
import com.example.synder.models.FromFirebase.MessagesFromFirebase
import com.example.synder.models.UserProfile
import kotlinx.coroutines.flow.Flow

interface StorageService {
    val users: Flow<List<UserProfile>>
    val chats: Flow<List<ChatsFromFirebase>>
    val messages: Flow<List<List<MessagesFromFirebase>>>

    suspend fun readChat(chatId: String): Boolean
    suspend fun getUser(userId: String): UserProfile?
    suspend fun getNumberOfMessages(chatId: String): Int
    suspend fun createUser(user: UserProfile): String
    suspend fun getMessagesForChat(chatId: String): List<MessagesFromFirebase>
    suspend fun getChat(chatId: String): ChatsFromFirebase?
    suspend fun sendMessage(chatId: String, message: MessagesFromFirebase): Boolean
    fun getMessagesFlowForChat(chatId: String): Flow<List<MessagesFromFirebase>>
}