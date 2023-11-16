package com.example.synder.service.impl

import com.example.synder.models.FromFirebase.ChatsFromFirebase
import com.example.synder.models.FromFirebase.MessagesFromFirebase
import com.example.synder.models.UserProfile
import com.example.synder.service.StorageService
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

    override suspend fun getMessagesForChat(chatId: String): List<MessagesFromFirebase> {
        val messages = mutableListOf<MessagesFromFirebase>()
        // Use Firestore call to get documents and transform them into a list
        val documents = firestore.collection("chats").document(chatId)
            .collection("messages").get().await()
        for (document in documents) {
            document.toObject(MessagesFromFirebase::class.java)?.let { message ->
                messages.add(message)
            }
        }
        return messages
    }


    override suspend fun getUser(userId: String): UserProfile? =
        firestore.collection(USERS).document(userId).get().await().toObject()

    override suspend fun createUser(user: UserProfile): String =
        firestore.collection(USERS).add(user).await().id



    override suspend fun getChat(chatId: String): ChatsFromFirebase? =
        firestore.collection(CHATS).document(chatId).get().await().toObject()
    companion object {
        private const val USERS = "users"
        private const val CHATS = "chats"
    }
}