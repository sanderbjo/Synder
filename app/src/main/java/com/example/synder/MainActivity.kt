package com.example.synder


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.synder.ui.chatScreen
import com.example.synder.ui.profileScreen
import com.example.synder.ui.swipeScreen
import com.example.synder.ui.theme.SynderTheme


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

enum class Screens {
    Profile,
    Settings,
    Home
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val curRoute by rememberUpdatedState(newValue = navController.currentBackStackEntryAsState().value?.destination?.route ?: Screens.Home.name)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text(text = "Synder") },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = Color.Red
                )
            )
        },
        bottomBar = {
            BottomAppBar {
                Row(Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceAround) {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            Icons.Filled.AccountCircle,
                            contentDescription = null
                        )
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            Icons.Filled.Home,
                            contentDescription = null
                        )
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            Icons.Filled.MailOutline,
                            contentDescription = null
                        )
                    }
                }
            }
        }

    ) {innerPadding ->
        NavHost(navController = navController,
            startDestination = "profile",
            modifier = modifier.fillMaxSize()) {
            composable("profile") {
                profileScreen(modifier.padding(innerPadding))
            }
            composable("settings"){
                swipeScreen(modifier.padding(innerPadding))
            }
            composable("home"){
                chatScreen(modifier.padding(innerPadding))
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