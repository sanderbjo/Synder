package com.example.synder.service.impl

import com.example.synder.models.Coordinates
import android.util.Log
import com.example.synder.models.FromFirebase.ChatsFromFirebase
import com.example.synder.models.FromFirebase.MessagesFromFirebase
import com.example.synder.models.UserProfile
import com.example.synder.service.StorageService
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.dataObjects
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StorageServiceImpl
@Inject
constructor(private val firestore: FirebaseFirestore) : StorageService {
    override val users: Flow<List<UserProfile>>
        get() = firestore.collection(USERS).dataObjects()
    override val chats: Flow<List<ChatsFromFirebase>>
        get() = firestore.collection(CHATS).dataObjects()
    override val messages: Flow<List<List<MessagesFromFirebase>>>
        get() = firestore.collection(CHATS).dataObjects()


    override suspend fun getNumberOfMessages(chatId: String): Int {
        return try {
            val snapshot = firestore.collection("chats").document(chatId)
                .collection("messages").get().await()

            snapshot.size()
        } catch (e: Exception) {
            Log.e("StorageService", "Error getting number of messages", e)
            0
        }
    }

    override suspend fun getMessagesForChat(chatId: String): List<MessagesFromFirebase> {
        val messages = mutableListOf<MessagesFromFirebase>()
        val documents = firestore.collection("chats").document(chatId)
            .collection(MESSAGES).get().await()
        for (document in documents) {
            document.toObject(MessagesFromFirebase::class.java)?.let { message ->
                messages.add(message)
            }
        }
        return messages
    }

    override fun getMessagesFlowForChat(chatId: String): Flow<List<MessagesFromFirebase>> {
        return callbackFlow {
            val subscription = firestore.collection("chats").document(chatId)
                .collection(MESSAGES)
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        close(e)
                        return@addSnapshotListener
                    }
                    val messageList = snapshot?.documents?.mapNotNull { it.toObject(MessagesFromFirebase::class.java) }
                    this.trySend(messageList ?: emptyList()).isSuccess
                }

            awaitClose { subscription.remove() }
        }
    }


    override suspend fun readChat(chatId: String): Boolean {
        return try {
            firestore.collection("chats").document(chatId)
                .update("latestmessage", "").await()
            true
        } catch (e: Exception) {
            Log.e("readChat", "Failed to update latestmessage", e)
            false
        }
    }


    override suspend fun sendMessage(chatId: String, message: MessagesFromFirebase): Boolean {
        return try {
            val documents = firestore.collection("chats").document(chatId).collection(MESSAGES).get().await()
            val index = documents.size()

            val updatedMessage = message.copy(index = index)

            firestore.collection("chats").document(chatId)
                .collection(MESSAGES).add(updatedMessage).await()

            firestore.collection("chats").document(chatId)
                .update(mapOf(
                    "latestmessage" to updatedMessage.text,
                    "latestsender" to updatedMessage.userId
                )).await()

            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun createChat(newChat: ChatsFromFirebase): String {
        val chatRef = firestore.collection(CHATS).document()
        chatRef.set(newChat).await()

        val chatId = chatRef.id
        val userId1Ref = firestore.collection(USERS).document(newChat.userId1)
        val userId2Ref = firestore.collection(USERS).document(newChat.userId2)

        firestore.runBatch { batch ->
            batch.update(userId1Ref, "chats", FieldValue.arrayUnion(chatId))
            batch.update(userId2Ref, "chats", FieldValue.arrayUnion(chatId))
        }.await()

        return chatId
    }

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

    override suspend fun updateUserLocation(userid: String, coordinates: Coordinates) {
        val userdoc = firestore.collection(USERS).document(userid)
        userdoc.update("coordinates", coordinates).await()
    }



    companion object {
        private const val USERS = "users"
        private const val CHATS = "chats"
        private const val MESSAGES = "messages"
    }
}