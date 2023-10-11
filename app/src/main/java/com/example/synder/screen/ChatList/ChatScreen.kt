package com.example.synder.screen.ChatList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.synder.Screen
import com.example.synder.models.Chat
import com.example.synder.components.Chat
import com.example.synder.components.Message
import com.example.synder.models.Message

/*
enum class ChatList {
    Chats,
    Matches,
    Chat
}

@Composable
fun Parent(modifier: Modifier = Modifier) {
    val navControllerChat = rememberNavController()
    val chatRoute by rememberUpdatedState(newValue = navControllerChat.currentBackStackEntryAsState().value?.destination?.route ?: ChatList.Chats.name)

    Scaffold(
        topBar = {
            // Add a top bar if needed
        },
        content = {
            // Content of your screen
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Your content here
                Text("Your screen content goes here")

                SegmentedButton()
            }
        }
    ) {innerPadding ->
        NavHost(navControllerChat = navControllerChat,
            startDestination = ChatList.Chats.name,
            modifier = Modifier.padding(innerPadding))
        {
            composable(ChatList.Chats.name){
                ProfileScreen()
            }
            composable(ChatList.Matches.name){
                SwipeScreen()
            }
            composable(ChatList.Chat.name){
                chatScreen()
            }
        }
    }
}
}
@Composable
fun SegmentedButton(
    items: List<String>,
    selected: String,
    onItemSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        items.forEach { item ->
            val isSelected = item == selected
            Button(
                icon = { Icon(Icons.Default.Person, contentDescription = null) },
                selected = chatRoute == Screen.Profile.name,
                onClick = {
                    if (chatRoute != Screen.Profile.name) {
                        navControllerChat.navigate(Screen.Profile.name){
                            popUpTo(navControllerChat.graph.findStartDestination().id){
                                saveState = true
                            }
                            launchSingleTop = true
                        }
                    }
                },
                label = { Text(text = ChatList.Chats.name)},
                alwaysShowLabel = true
            )
        }
    }
}
*/

@Composable
fun chatScreen(curRoute: String, navController: NavHostController, modifier: Modifier = Modifier) {
    val userChats = listOf(
        Chat("Christine 21", "Christine: Hello", "Sent 4:00PM", Icons.Default.AccountBox),
        Chat("Monica 45", "Monica: Heisann storegutten ;)", "Sent 4:00PM", Icons.Default.AccountBox),
        Chat("Dudan 19", "Dudan: Omg så store biceps!!!", "Sent 4:00PM", Icons.Default.AccountBox),
        Chat("Polkan 23", "Du: Jævla fitte", "Sent 4:00PM", Icons.Default.AccountBox),
        Chat("Polkan 23", "Du: Jævla fitte", "Sent 4:00PM", Icons.Default.AccountBox),
        Chat("Polkan 23", "Du: Jævla fitte", "Sent 4:00PM", Icons.Default.AccountBox),
        Chat("Polkan 23", "Du: Jævla fitte", "Sent 4:00PM", Icons.Default.AccountBox),
        Chat("Polkan 23", "Du: Jævla fitte", "Sent 4:00PM", Icons.Default.AccountBox),
        Chat("Polkan 23", "Du: Jævla fitte", "Sent 4:00PM", Icons.Default.AccountBox),
        Chat("Polkan 23", "Du: Jævla fitte", "Sent 4:00PM", Icons.Default.AccountBox),
        Chat("Polkan 23", "Du: Jævla fitte", "Sent 4:00PM", Icons.Default.AccountBox),
        Chat("Polkan 23", "Du: Jævla fitte", "Sent 4:00PM", Icons.Default.AccountBox),
    )
    //Column
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)) {
        item { Text(text = "All Chats", fontSize = 32.sp) }

        items(userChats) { it ->
            Chat(it, curRoute, navController)
        }

        item {
            Divider(
                color = Color.Black,
                thickness = 2.dp,
                modifier = Modifier
                    .fillMaxWidth(0.8f) // 80% total width
                    .wrapContentWidth(Alignment.CenterHorizontally)
                    .padding(start = 1.dp, end = 1.dp)
            )
        }

        item {
            Text(
                text = "Ingen Flere Chats!",
                fontSize = 15.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )
        }
    }
}
@Composable
fun matchScreen(curRoute: String, navController: NavHostController, modifier: Modifier = Modifier) {
    val matches = listOf(
        Chat("Christine 21", "Matchet 4 dager siden", "", Icons.Default.AccountBox),
        Chat("Monica 45", "Matchet 4 dager siden", "", Icons.Default.AccountBox),
        Chat("Dudan 19", "Matchet 2 uker siden", "", Icons.Default.AccountBox),
        Chat("Polkan 23", "Matchet 4 år siden", "", Icons.Default.AccountBox),
    )
    //Column
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)) {
        item { Text(text = "All Chats", fontSize = 32.sp) }

        items(matches) { it ->
            Chat(it, curRoute, navController)
        }

        item {
            Divider(
                color = Color.Black,
                thickness = 2.dp,
                modifier = Modifier
                    .fillMaxWidth(0.8f) // 80% total width
                    .wrapContentWidth(Alignment.CenterHorizontally)
                    .padding(start = 1.dp, end = 1.dp)
            )
        }

        item {
            Text(
                text = "Ingen flere Syndere!",
                fontSize = 15.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )
        }
    }
}


@Composable
fun conversationWindow(modifier: Modifier = Modifier) {
    val messages = listOf(
        Message("Christine", "Hello", "Sent 4:00PM", false),
        Message("Christine", "Heisann storegutten ;)", "Sent 4:00PM", false),
        Message("Du", "Sex?", "Sent 4:00PM", true),
        Message("Christine", "Selvfølgelig! Jeg er en oppriktig hore jeg!", "Sent 4:00PM", false),
        Message("Du", "Var det jeg visste, din absolute sjus >;(", "Sent 4:01PM", true),
        Message("Du", "Vi er ferdig. Hade!", "Sent 4:01PM", true),
        Message("Christine", "NEI PLSSS KAN GI BLWJ JEG SVERGER", "Sent 4:00PM", false),
        Message("Christine", "DOn LeaVE MEHEHEHEHEHE", "Sent 4:00PM", false),


        )
    //Column
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)) {

        items(messages) { it ->
            Message(it)
        }

        item {
            Divider(
                color = Color.Black,
                thickness = 2.dp,
                modifier = Modifier
                    .fillMaxWidth(0.8f) // 80% total width
                    .wrapContentWidth(Alignment.CenterHorizontally)
                    .padding(start = 1.dp, end = 1.dp)
            )
        }

        item {
            Text(
                text = "Ingen Flere Meldinger!",
                fontSize = 15.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )
        }
    }
}