package com.example.synder.models

data class ChatAndParticipant(
    val id: String = "",
    val chat: ChatsFromFirebase = ChatsFromFirebase(),
    val userId1: UserProfile = UserProfile(),
    val userId2: UserProfile = UserProfile(),
    val latestmessage: Message
)
