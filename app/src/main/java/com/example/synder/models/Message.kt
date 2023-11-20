package com.example.synder.models

import androidx.compose.ui.graphics.vector.ImageVector
import com.example.synder.models.FromFirebase.MessagesFromFirebase
import com.google.firebase.firestore.DocumentId

data class Message(
    val id: String = "",
    val userId: String,
    val userProfile: UserProfile = UserProfile(),
    val text: String,
    val sent: String,
    val sentByUser: Boolean
){
    fun convertToMessageFromFirebase(): MessagesFromFirebase {
        return MessagesFromFirebase(
            id = id, // Firestore genererer denne IDen, så du kan sannsynligvis utelate den her
            sent = sent,
            text = text,
            userId = userId
        )
    }
}