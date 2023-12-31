package com.example.synder.components

import SoundEffectPlayer
import android.content.Context
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.synder.screen.ChatList.ChatViewModel
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Chatbar(context: Context, chatViewModel: ChatViewModel = hiltViewModel()) {
    var textValue by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val soundEffectPlayer = SoundEffectPlayer(context)

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {
            soundEffectPlayer.playCloseKeyboard()
            keyboardController?.hide()
        },
            modifier = Modifier.padding(8.dp)
        ) {
            Icon(Icons.Default.KeyboardArrowDown, contentDescription = null)
        }
        TextField(
            value = textValue,
            onValueChange = { newTextValue -> textValue = newTextValue },
            placeholder = { Text("Skriv her") },
            modifier = Modifier.weight(1f)
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) {
                        soundEffectPlayer.playOpenKeyboard()
                    }
                }
        )
        IconButton(
            onClick = {
                if (textValue.trim().isNotEmpty()) {
                    chatViewModel.sendMessage(textValue.trim())
                    soundEffectPlayer.playSend()
                    textValue = ""
                }
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Icon(Icons.Default.Send, contentDescription = null)
        }
    }
}
