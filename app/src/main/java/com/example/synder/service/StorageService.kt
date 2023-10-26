package com.example.synder.service

import com.example.synder.models.ChatsFromFirebase
import com.example.synder.models.UserProfile
import kotlinx.coroutines.flow.Flow

interface StorageService {
    val users: Flow<List<UserProfile>>
    val chats: Flow<List<ChatsFromFirebase>>
    suspend fun getUser(userId: String): UserProfile?
    suspend fun saveUser(user: UserProfile): String
    suspend fun getChat(chatId: String): ChatsFromFirebase?
}