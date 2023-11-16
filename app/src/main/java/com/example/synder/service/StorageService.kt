package com.example.synder.service

import com.example.synder.models.FromFirebase.ChatsFromFirebase
import com.example.synder.models.FromFirebase.MessagesFromFirebase
import com.example.synder.models.UserProfile
import kotlinx.coroutines.flow.Flow

interface StorageService {
    val users: Flow<List<UserProfile>>
    val chats: Flow<List<ChatsFromFirebase>>
    suspend fun getUser(userId: String): UserProfile?

    suspend fun createUser(user: UserProfile): String
    suspend fun getMessagesForChat(chatId: String): List<MessagesFromFirebase>
    suspend fun getChat(chatId: String): ChatsFromFirebase?
}