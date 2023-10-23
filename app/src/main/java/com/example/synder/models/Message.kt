package com.example.synder.models

import androidx.compose.ui.graphics.vector.ImageVector

data class Message (
    val name: String,
    val message: String,
    val date: String,
    val sentbyuser: Boolean
)