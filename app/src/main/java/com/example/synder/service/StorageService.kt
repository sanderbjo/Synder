package com.example.synder.service

import com.example.synder.models.ChatsFromFirebase
import com.example.synder.models.UserProfile
import kotlinx.coroutines.flow.Flow

interface StorageService {
    val users: Flow<List<UserProfile>>
    val chats: Flow<List<ChatsFromFirebase>>
    suspend fun getUser(userId: String): UserProfile?

    suspend fun createUser(user: UserProfile): String
    suspend fun getChat(chatId: String): ChatsFromFirebase?

    suspend fun saveLikedUser(userId: String, likedUserId: String)
    suspend fun saveDislikedUser(userId: String, dislikedUserId: String)

    suspend fun updateMatches(userId: String, matchedUserId: String)
    suspend fun updateUserLocation(userid: String, latitude: Double, longitude: Double)

}