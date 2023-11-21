package com.example.synder.service.impl

import com.example.synder.models.Chat
import com.example.synder.models.ChatsFromFirebase
import com.example.synder.models.Coordinates
import com.example.synder.models.UserProfile
import com.example.synder.service.StorageService
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.dataObjects
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StorageServiceImpl
@Inject
constructor(private val firestore: FirebaseFirestore) : StorageService {
    override val users: Flow<List<UserProfile>>
        get() = firestore.collection(USERS).dataObjects()
    override val chats: Flow<List<ChatsFromFirebase>>
        get() = firestore.collection(CHATS).dataObjects()
    override suspend fun getUser(userId: String): UserProfile? =
        firestore.collection(USERS).document(userId).get().await().toObject()

    override suspend fun createUser(user: UserProfile): String =
        firestore.collection(USERS).add(user).await().id

    override suspend fun getChat(chatId: String): ChatsFromFirebase? =
        firestore.collection(CHATS).document(chatId).get().await().toObject()


    override suspend fun saveLikedUser(userId: String, likedUserId: String) {
        val userDoc = firestore.collection(USERS).document(userId)
        userDoc.update("likedUsers", FieldValue.arrayUnion(likedUserId)).await()
    }
    override suspend fun saveDislikedUser(userId: String, dislikedUserId: String) {
        val userDoc = firestore.collection(USERS).document(userId)
        userDoc.update("dislikedUsers", FieldValue.arrayUnion(dislikedUserId)).await()
    }

    override suspend fun updateMatches(userId: String, matchedUserId: String){
        val userDoc = firestore.collection(USERS).document(userId)
        userDoc.update("matches", FieldValue.arrayUnion(matchedUserId)).await()

        val matchedUserDoc = firestore.collection(USERS).document(matchedUserId)
        matchedUserDoc.update("matches", FieldValue.arrayUnion(userId)).await()
    }

    /*override suspend fun updateUserLocation(userid: String, latitude: Double, longitude: Double){
        val userDoc = firestore.collection(USERS).document(userid)
        userDoc.update("latitude", latitude, "longitude", longitude).await()
    }*/

    override suspend fun updateUserLocation(userid: String, coordinates: Coordinates) {
        val userdoc = firestore.collection(USERS).document(userid)
        userdoc.update("coordinates", coordinates).await()
    }



    companion object {
        private const val USERS = "users"
        private const val CHATS = "chats"
    }
}