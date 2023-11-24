package com.example.synder.components

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.synder.Screen
import com.example.synder.models.ChatAndParticipant
import com.example.synder.models.UserProfile
import com.google.firebase.storage.StorageReference
import com.example.synder.screen.ChatList.ChatViewModel

@Composable
fun ChatCard(
    onChatClick: (String, ChatAndParticipant) -> Unit,
     it: ChatAndParticipant = ChatAndParticipant(),
     storageRef: StorageReference,
     navController: NavHostController,
     chatViewModel: ChatViewModel = hiltViewModel()
) {
    val UserInChat = if (it.user1.id.equals(chatViewModel.userId)) it.user2 else it.user1
    var newMessage = !it.latestsender.equals(chatViewModel.userId) && !it.latestmessage.isEmpty()

// Bestemmer visningsnavnet basert på hvem som sendte meldingen.


    val DisplayName = when {
        it.user1.id.equals(chatViewModel.userId) -> "Du"
        it.user2.id.equals(chatViewModel.userId) -> "Du"
        else -> UserInChat.name // Hvis ingen av brukerne matcher currentUserId, vis den andre brukerens navn.
    }


    OutlinedCard(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(8.dp)
            )
            .clickable {
                val path = chatViewModel.updateCurrentChat(it)
                if (path) {
                    onChatClick(it.id, it)
                    navController.navigate(Screen.Chat.name) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                    }
                }
            }
    ) {
        Row (
            modifier = Modifier
                .padding(start = 15.dp, top = 2.dp, bottom = 2.dp)
                .fillMaxSize(), // Center text both vertically and horizontally
            verticalAlignment = Alignment.CenterVertically

        ) {
            //if (UserInChat.profileImageUrl != "") {
            //    ProfilePicture(url = UserInChat.profileImageUrl)
            //} else {
            //    Monogram(name = UserInChat.name)
            //}
                ProfilePicture(imgReferance = storageRef.child("images/${it.user1.id}.jpg"))
            Column(modifier = Modifier
                .padding(10.dp)
                .padding(bottom = 20.dp)) {
                TruncatedText(UserInChat.name,20, 20, true)
                Row {
                    /*TruncatedText(DisplayName,16, 15)*/
                    if (it.chat.latestsender.isEmpty()) {
                        TruncatedText(DisplayName + " ", 18, 10, newMessage)

                        Text(text = "startet en chat", fontSize = 18.sp)
                    } else {
                        var hvemLeste = if (it.chat.latestsender.equals(it.user1.id))
                            it.user2.name else it.user1.name

                        if (!it.chat.latestsender.equals(chatViewModel.userId)) {
                            hvemLeste = "deg"
                        }

                        if (it.chat.latestmessage.isEmpty()) {
                            Text(text = "Lest av ", fontSize = 18.sp)
                            TruncatedText(hvemLeste, 18, 20, true)
                        } else {
                            TruncatedText(DisplayName + ": ", 18, 10, newMessage)
                            TruncatedText(it.latestmessage,20, 20, newMessage)
                        }
                    }

                }
                if (newMessage) {
                    Card(
                        modifier = Modifier.padding(top = 10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFD3454E), // Bakgrunnsfarge
                            contentColor = Color.White, // Innholdsfarge
                        ),
                    ) {
                        Text(text = "Du har en ny melding!", fontSize = 14.sp, modifier = Modifier.padding(5.dp)) // Adjust text size as needed
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f)) // This spacer will push the OutlinedCard to the right

        }
    }
}

@Composable
fun MatchCard(
    getChatFromClick: (String, ChatAndParticipant) -> Unit,
    ifChatIsNull: () -> Unit,
    it: UserProfile,
    storageRef: StorageReference,
    hasChat: ChatAndParticipant?,
    navController: NavHostController,
    chatViewModel: ChatViewModel = hiltViewModel()
) {
    OutlinedCard(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(8.dp)
            )
            .clickable {
                if (hasChat != null) {
                    val path = chatViewModel.updateCurrentChat(hasChat)
                    if (path) {
                        getChatFromClick(hasChat.id, hasChat)
                        navController.navigate(Screen.Chat.name) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                        }
                    }
                } else {
                    ifChatIsNull()
                    Log.d("PRØVDE Å TRYKKE PÅ BRUKJER ", it.name + " " + it.age.toString())
                }
            }
    ) {
        Row (
            modifier = Modifier
                .padding(start = 15.dp, top = 2.dp, bottom = 2.dp)
                .fillMaxSize(), // Center text both vertically and horizontally
            verticalAlignment = Alignment.CenterVertically

        ) {
            ProfilePicture(imgReferance = storageRef.child("images/${it.id}.jpg"))
            

            Column(modifier = Modifier.padding(10.dp)) {
                Text(text = it.name, fontWeight = FontWeight.Bold, fontSize = 20.sp) // Use "sp" for text size
                Text(text = it.bio, fontSize = 16.sp) // Adjust text size as needed

                Card(
                    modifier = Modifier.padding(top = 10.dp),

                    colors = CardDefaults.cardColors(
                            containerColor = if (hasChat == null) Color(0xFF901D1D) else Color.Transparent,
                            contentColor = if (hasChat == null) Color.White else Color.Black
                ),
                ) {
                    if (hasChat == null) {
                        Text(text = "Start samtalen", fontSize = 14.sp, modifier = Modifier.padding(5.dp)) // Adjust text size as needed
                    } else {
                        Text(text = "Gå til chat", fontSize = 14.sp, modifier = Modifier.padding(5.dp)) // Adjust text size as needed
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f)) // This spacer will push the OutlinedCard to the right

            if (hasChat === null) {
                OutlinedCard(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Transparent, // Set background to transparent
                        contentColor = Color.White, // Set content color to white
                    ),
                    border = BorderStroke(1.dp, Color(0xFFFFC700)), // Set border color to #FFC700
                    modifier = Modifier.padding(end = 10.dp)

                ) {
                    Text(text = "Ny!", color = Color(0xFFFFC700), modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp)) // Set text color to #FFC700
                }
            }
        }
    }
}

@Composable
fun TruncatedText(latestMessage: String, fontSize: Int, maxLength: Int = 20, isbold: Boolean = false) {
    Text(
        text = if (latestMessage.length > maxLength) {
            "${latestMessage.take(maxLength)}..."
        } else if (latestMessage.length > maxLength && latestMessage.length < (maxLength + 5)) {
            latestMessage
        } else {
            latestMessage
        },
        fontSize = fontSize.sp,
        fontWeight =
        if (isbold) {
             FontWeight.Bold
        } else {
            FontWeight.Normal
        }
    )
}
