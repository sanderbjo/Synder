package com.example.synder.models.FromFirebase;
import com.google.firebase.firestore.DocumentId

data class ChatsFromFirebase (
        @DocumentId val id: String = "",
        val userId1: String = "",
        val userId2: String = "",
        val latestmessage: String = "",
        val latestsender: String = "",
        val messages: List<MessagesFromFirebase> = emptyList()
)
