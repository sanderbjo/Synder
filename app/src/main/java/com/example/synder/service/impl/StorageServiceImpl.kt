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
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
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
        get() = firestore.collection(CHATS).dataObjects() ///FÅ MELDINGENE I EN CACHE; SLIK AT DE AUTOMATISK OPPDATERES MED FIREBASE


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
        // Use Firestore call to get documents and transform them into a list
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

    /*
    override fun getChatsFlowForUser(userId: String): Flow<List<ChatsFromFirebase>> {
        return callbackFlow {
            Log.d("CHAP digdigg 1.0 ID", userId)
            val chatIdsRef = firestore.collection(USERS).document(userId).collection("chats")
            val chatsRef = firestore.collection("chats") // Antar at det finnes en kolleksjon på toppnivå som heter "chats"

            // Abonnerer på endringer i brukerens chat-ID-er
            val subscription = chatIdsRef.addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e("CHAP Error", "Error fetching chat ids", e)
                    close(e)
                    return@addSnapshotListener
                }

                val chatIds = snapshot?.documents?.mapNotNull { it.id } ?: emptyList()
                Log.d("CHAP digdigg 1", "Chat IDs: $chatIds")

                // Hent de faktiske chat-dokumentene basert på ID-ene
                if (chatIds.isNotEmpty()) {
                    chatIds.forEach { chatId ->
                        chatsRef.document(chatId.toString()).get().addOnSuccessListener { chatSnapshot ->
                            val chat = chatSnapshot.toObject(ChatsFromFirebase::class.java)
                            if (chat != null) {
                                // Håndter mottak av chat-objektet, f.eks. ved å oppdatere en lokal liste og sende listen
                                // Denne delen av koden må være trådsikker og håndtere sammenslåing av data fra flere asynkrone kall
                            }
                        }
                    }
                } else {
                    // Håndter tilfelle der det ikke finnes noen chat-ID-er
                    this.trySend(emptyList()).isSuccess
                }
            }

            awaitClose { subscription.remove() }
        }
    }

    override fun getMatchesFlowForUser(userId: String): Flow<List<UserProfile>> {
        return callbackFlow {
            val subscription = firestore.collection("users").document(userId)
                .collection("matches") // Antar at det finnes en subcollection "matches"
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        close(e)
                        return@addSnapshotListener
                    }
                    val matchList = snapshot?.documents?.mapNotNull { it.toObject(UserProfile::class.java) }
                    this.trySend(matchList ?: emptyList()).isSuccess
                }

            awaitClose { subscription.remove() }
        }
    }*/


    override suspend fun readChat(chatId: String): Boolean { //viser at bruker har lest chatten
        return try {
            firestore.collection("chats").document(chatId)
                .update("latestmessage", "").await()
            true // Oppdateringen var vellykket
        } catch (e: Exception) {
            Log.e("readChat", "Failed to update latestmessage", e)
            false // Det oppsto en feil under oppdateringen
        }
    }


    override suspend fun sendMessage(chatId: String, message: MessagesFromFirebase): Boolean {
        return try {
            // Hent antall dokumenter i kolleksjonen asynkront og bruk størrelsen som indeks
            val documents = firestore.collection("chats").document(chatId).collection(MESSAGES).get().await()
            val index = documents.size()  // Antall eksisterende dokumenter blir din indeks

            // Oppdater meldingen med indeksen
            val updatedMessage = message.copy(index = index)

            // Legg til meldingen i 'messages' underlagret samling
            firestore.collection("chats").document(chatId)
                .collection(MESSAGES).add(updatedMessage).await()

            // Oppdater 'latestmessage' og 'latestsender' i 'chats' dokumentet
            firestore.collection("chats").document(chatId)
                .update(mapOf(
                    "latestmessage" to updatedMessage.text,
                    "latestsender" to updatedMessage.userId
                )).await()

            true
        } catch (e: Exception) {
            false // Håndter feilen
        }
    }

    override suspend fun createChat(newChat: ChatsFromFirebase): String {
        // Oppretter et nytt Firestore-dokument for chatten og lagrer chatten
        val chatRef = firestore.collection(CHATS).document()
        chatRef.set(newChat).await()

        // Legger til ID-en til det nye chat-dokumentet i 'matches'-listen for hver bruker
        val chatId = chatRef.id
        val userId1Ref = firestore.collection(USERS).document(newChat.userId1)
        val userId2Ref = firestore.collection(USERS).document(newChat.userId2)

        firestore.runBatch { batch ->
            // Oppdaterer 'matches' for begge brukerne i samme database batch for konsistens
            batch.update(userId1Ref, "chats", FieldValue.arrayUnion(chatId))
            batch.update(userId2Ref, "chats", FieldValue.arrayUnion(chatId))
        }.await()

        // Returnerer ID-en til det nyopprettede chat-dokumentet
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
        private const val MESSAGES = "messages" //ligger i en chat
        private const val CHATSINUSER = "chats" //ligger i en user
    }
}