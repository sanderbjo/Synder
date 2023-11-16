package com.example.synder.models.FromFirebase
import com.google.firebase.firestore.DocumentId

data class MessagesFromFirebase (
    @DocumentId val id: String = "",
    var sent: String = "",
    var text: String = "",
    var userId: String = ""
)