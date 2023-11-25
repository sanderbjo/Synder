package com.example.synder


import android.os.Bundle
import android.os.UserHandle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.compose.SynderTheme
import com.example.compose.md_theme_light_onPrimary
import com.example.compose.md_theme_light_primary
import com.example.synder.components.ChatTopNavigation
import com.example.synder.components.Chatbar
import com.example.synder.components.SegmentedButton
import com.example.synder.models.ChatAndParticipant
import com.example.synder.models.FromFirebase.MessagesFromFirebase
import com.example.synder.models.UserProfile
import com.example.synder.screen.ChatList.ChatViewModel
import com.example.synder.screen.ChatList.chatScreen
import com.example.synder.screen.ChatList.conversationWindow
import com.example.synder.screen.ChatList.matchScreen
import com.example.synder.screen.login.LoginScreen
import com.example.synder.screen.profile.ProfileScreen
import com.example.synder.screen.settings.SettingsScreen
import com.example.synder.screen.sign_up.SignUpScreen
import com.example.synder.screen.swipe.SwipeScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
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
    Chat,
    Login,
    SignUp
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(chatViewModel: ChatViewModel = hiltViewModel()) {
    var isVisible by remember { mutableStateOf(false) }
    var showChatInput by remember { mutableStateOf(false) }
    var currentChatAndParticipant by remember { mutableStateOf(ChatAndParticipant()) }

    var id  by remember { mutableStateOf("") }
    var chat  by remember { mutableStateOf(ChatAndParticipant()) }

    var isDarkTheme by remember { mutableStateOf(false) }

    val navController = rememberNavController()
    val curRoute by rememberUpdatedState(
        newValue = navController.currentBackStackEntryAsState().value?.destination?.route
            ?: Screen.Swipe.name
    )

    fun toggleTheme() {
        isDarkTheme = !isDarkTheme
    }

    SynderTheme(useDarkTheme = isDarkTheme) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = md_theme_light_primary,
                        titleContentColor = md_theme_light_primary,
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
                                color = md_theme_light_onPrimary
                            )
                        }
                    }
                )
            },
            bottomBar = {
                if (isVisible == true) {
                    BottomNavigation(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = md_theme_light_primary
                    ) {
                        //Profil screen
                        BottomNavigationItem(
                            icon = {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = null,
                                    tint = md_theme_light_onPrimary
                                )
                            },
                            selected = curRoute == Screen.Profile.name,
                            onClick = {
                                if (curRoute != Screen.Profile.name) {
                                    navController.navigate(Screen.Profile.name) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                    }
                                }
                            },
                            label = {
                                Text(
                                    text = Screen.Profile.name,
                                    color = md_theme_light_onPrimary
                                )
                            },
                            alwaysShowLabel = true
                        )

                        // Swipe screen
                        BottomNavigationItem(
                            icon = {
                                Icon(
                                    Icons.Default.Home,
                                    contentDescription = null,
                                    tint = md_theme_light_onPrimary
                                )
                            },
                            selected = curRoute == Screen.Swipe.name,
                            onClick = {
                                if (curRoute != Screen.Swipe.name) {
                                    navController.navigate(Screen.Swipe.name) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                    }
                                }
                            },
                            label = {
                                Text(
                                    text = Screen.Swipe.name,
                                    color = md_theme_light_onPrimary
                                )
                            },
                            alwaysShowLabel = true
                        )

                        // Chat screen
                        BottomNavigationItem(
                            icon = {
                                Icon(
                                    Icons.Default.Email,
                                    contentDescription = null,
                                    tint = md_theme_light_onPrimary
                                )
                            },
                            selected = curRoute == Screen.Chats.name,
                            onClick = {
                                if (curRoute != Screen.Chats.name) {
                                    navController.navigate(Screen.Chats.name) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                    }
                                }
                            },
                            label = {
                                Text(
                                    text = Screen.Chats.name,
                                    color = md_theme_light_onPrimary
                                )
                            },
                            alwaysShowLabel = true
                        )
                    }
                } else if (showChatInput) {
                    Chatbar(context = LocalContext.current)

                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Login.name,
                modifier = Modifier.padding(innerPadding)
            )
            {
                composable(Screen.Profile.name) {
                    ProfileScreen(navController = navController)
                    isVisible = true
                }
                composable(Screen.Settings.name) {
                    SettingsScreen(isDarkTheme, { toggleTheme() }, context = LocalContext.current)
                    isVisible = true
                }
                composable(Screen.Swipe.name) {
                    SwipeScreen()
                    isVisible = true
                }
                composable(Screen.Chats.name) {
                    Column {
                        isVisible = true
                        SegmentedButton(
                            curRoute = curRoute,
                            navController = navController,
                            1,
                            isDarkTheme = isDarkTheme,
                        )
                        chatScreen(getChatFromClick = { chatId, chatData ->
                            id = chatId
                            chat = chatData
                        }
                            , isDarkTheme, navController, currentChatAndParticipant)
                    }
                }
                composable(Screen.Matches.name) {
                    Column {
                        isVisible = true
                        SegmentedButton(
                            curRoute = curRoute,
                            navController = navController,
                            2,
                            isDarkTheme = isDarkTheme,
                        )

                        matchScreen(getChatFromClick = { chatId, chatData ->
                            id = chatId
                            chat = chatData
                            if (id.equals("")) {

                            }
                        }, isDarkTheme, navController)
                    }
                }
                composable(Screen.Chat.name) {
                    Column {
                        // State for å kontrollere om conversationWindow skal vises
                        Log.d("TEST UPDATED? string", "${id}")
                        Log.d("TEST UPDATED? ChatAndParticipant", "${chat}")
                        val isLoaded = remember { mutableStateOf(false) }
                        isVisible = false
                        showChatInput = true
                        chatViewModel.upadeCurrentId(id)
                        val userToSend = chatViewModel.getOtherUserId(chat)

                        ChatTopNavigation( //Navigasjonen inne i et chat vindu
                            it = userToSend,
                            curRoute = curRoute,
                            navController = navController
                        )
                        conversationWindow(id, chat)
                        /*
                        if (showConversationWindow.value) {
                        } else {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator() //Innlastingds ikon
                            }
                        }*/
                    }
                }
                composable(Screen.Login.name){
                LoginScreen(loggedIn = { navController.navigate("profile") },
                            onSignupClick = {navController.navigate("signup")})
                isVisible = false;
                }
                composable(Screen.SignUp.name){
                    SignUpScreen(loggedIn = { navController.navigate("profile") })
                    isVisible = false;
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