package com.example.synder


import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.synder.components.ChatTopNavigation
import com.example.synder.components.Chatbar
import com.example.synder.components.SegmentedButton
import com.example.synder.screen.profile.ProfileScreen
import com.example.synder.screen.ChatList.chatScreen
import com.example.synder.screen.swipe.SwipeScreen
import com.example.synder.screen.ChatList.conversationWindow
import com.example.synder.screen.ChatList.matchScreen
import com.example.synder.screen.profile.ProfileViewModel
import com.example.synder.ui.theme.SynderTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SynderTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation()
                }
            }
        }
    }
}

enum class Screen {
    Profile,
    Settings,
    Swipe,
    Chats,
    Matches,
    Chat
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation() {
    var isVisible by remember { mutableStateOf(false) }

    val navController = rememberNavController()
    val curRoute by rememberUpdatedState(newValue = navController.currentBackStackEntryAsState().value?.destination?.route ?: Screen.Swipe.name)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Synder",
                            fontFamily = FontFamily(Font(R.font.pacifico_regular)),
                            fontSize = 24.sp,
                            color = Color.Red   //må endres til theme farge
                        )
                    }
                }
            )
        },

        bottomBar = {
            if (isVisible == true) {
                BottomNavigation (
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = MaterialTheme.colorScheme.primaryContainer
                ){
                    //Profil screen
                    BottomNavigationItem(
                        icon = { Icon(Icons.Default.Person, contentDescription = null)},
                        selected = curRoute == Screen.Profile.name,
                        onClick = {
                            if (curRoute != Screen.Profile.name) {
                                navController.navigate(Screen.Profile.name){
                                    popUpTo(navController.graph.findStartDestination().id){
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                }
                            }
                        },
                        label = { Text(text = Screen.Profile.name)},
                        alwaysShowLabel = true
                    )

                    // Swipe screen
                    BottomNavigationItem(
                        icon = { Icon(Icons.Default.Home, contentDescription = null) },
                        selected = curRoute == Screen.Swipe.name,
                        onClick = {
                            if (curRoute != Screen.Swipe.name) {
                                navController.navigate(Screen.Swipe.name){
                                    popUpTo(navController.graph.findStartDestination().id){
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                }
                            }
                        },
                        label = { Text(text = Screen.Swipe.name)},
                        alwaysShowLabel = true
                    )

                    // Chat screen
                    BottomNavigationItem(
                        icon = { Icon(Icons.Default.Email, contentDescription = null)},
                        selected = curRoute == Screen.Chats.name,
                        onClick = {
                            if (curRoute != Screen.Chats.name) {
                                navController.navigate(Screen.Chats.name){
                                    popUpTo(navController.graph.findStartDestination().id){
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                }
                            }
                        },
                        label = { Text(text = Screen.Chats.name)},
                        alwaysShowLabel = true
                    )
                }
            } else {
                Chatbar()
            }

        }

    ) {innerPadding ->
        NavHost(navController = navController,
            startDestination = Screen.Swipe.name,
            modifier = Modifier.padding(innerPadding))
        {
            composable(Screen.Profile.name){
                ProfileScreen()
                isVisible = true;
            }
            composable(Screen.Swipe.name){
                SwipeScreen()
                isVisible = true;
            }
            composable(Screen.Chats.name){
                Column {
                    isVisible = true;
                    SegmentedButton(curRoute = curRoute, navController = navController, 1)
                    chatScreen(curRoute, navController)
                }
            }
            composable(Screen.Matches.name){
                Column {
                    isVisible = true;
                    SegmentedButton(curRoute = curRoute, navController = navController, 2)
                    matchScreen(curRoute, navController)
                }
            }
            composable(Screen.Chat.name){
                Column {
                    isVisible = false;
                    ChatTopNavigation(curRoute = curRoute, navController = navController)
                    conversationWindow()
                }
            }

            when (curRoute) {
                Screen.Profile.name, Screen.Swipe.name, Screen.Chats.name -> {
                    isVisible = true
                }
                else -> {
                    isVisible = false
                }
            }

        }
    }
}


@Preview(showBackground = true)
@Composable
fun SynderPreview() {
    SynderTheme {
        Navigation()
    }
}