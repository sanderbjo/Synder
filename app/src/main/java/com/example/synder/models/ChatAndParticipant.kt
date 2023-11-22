package com.example.synder.models

import com.example.synder.models.FromFirebase.ChatsFromFirebase

data class ChatAndParticipant(
    val id: String = "",
    val chat: ChatsFromFirebase = ChatsFromFirebase(),
    val user1: UserProfile = UserProfile(),
    val user2: UserProfile = UserProfile(),
    val latestmessage: String = "",     //Message
    val latestsender: String = "",
)
