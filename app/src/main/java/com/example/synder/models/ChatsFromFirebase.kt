package com.example.synder.models;
import com.google.firebase.firestore.DocumentId

data class ChatsFromFirebase (
        @DocumentId val id: String = "",
        val userId1: String = "",
        val userId2: String = "",
        val latestmessage: String = "",
        val messages: List<Message> = emptyList()
)
