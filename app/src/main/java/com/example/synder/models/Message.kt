package com.example.synder.models

import androidx.compose.ui.graphics.vector.ImageVector
import com.google.firebase.firestore.DocumentId

data class Message(
    val id: String = "",
    val userId: String,
    val userProfile: UserProfile = UserProfile(),
    val text: String,
    val sent: String,
    val sentByUser: Boolean
)