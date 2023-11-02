package com.example.synder.screen.login

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.synder.R

@Composable
fun LoginScreen(
    loggedIn: () -> Unit,
    onSignupClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState
    val fieldModifier = Modifier
        .fillMaxWidth()
        .padding(16.dp, 4.dp)


    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (uiState.errorMessage != "") {
            Text(
                text = uiState.errorMessage,
                Modifier.padding(vertical = 8.dp)
            )
        }
            Text(text = "Vi har lagd en anonym bruker som kan brukes for testing",
                modifier = Modifier.padding(horizontal = 16.dp),
                textAlign = TextAlign.Center)
            Text(text = "email = anonym@bruker.no")
            Text(text = "passord = Anonym123",
                modifier = modifier.padding(bottom = 24.dp))
            EmailField(uiState.email, viewModel::onEmailChange, fieldModifier)
            PasswordField(uiState.password, viewModel::onPasswordChange, fieldModifier)


            Row {
                Button(
                    onClick = { viewModel.onLoginClick(loggedIn) },
                    modifier = Modifier
                        .padding(16.dp, 8.dp),
                ) {
                    Text(text = "login", fontSize = 16.sp)
                }

            }
            Text(text = "Har du ikke bruker? Sign up nå!")
            Button(onClick = onSignupClick) {
                Text(text = "Sign up")
            }
    }
}


@Composable
fun EmailField(value: String, onNewValue: (String) -> Unit, modifier: Modifier = Modifier) {
    OutlinedTextField(
        singleLine = true,
        modifier = modifier,
        value = value,
        onValueChange = { onNewValue(it) },
        placeholder = { Text("email") },
        leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "Email") }
    )
}

@Composable
fun PasswordField(value: String, onNewValue: (String) -> Unit, modifier: Modifier = Modifier) {
    PasswordField(value, R.string.password, onNewValue, modifier)
}


    @Composable
    private fun PasswordField(
        value: String,
        @StringRes placeholder: Int,
        onNewValue: (String) -> Unit,
        modifier: Modifier = Modifier
    ) {
        var isVisible by remember { mutableStateOf(false) }

        val icon =
            if (isVisible) painterResource(R.drawable.baseline_visibility_24)
            else painterResource(R.drawable.baseline_visibility_off_24)

        val visualTransformation =
            if (isVisible) VisualTransformation.None else PasswordVisualTransformation()

        OutlinedTextField(
            modifier = modifier,
            value = value,
            onValueChange = { onNewValue(it) },
            placeholder = { Text(text = stringResource(placeholder)) },
            leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "Lock") },
            trailingIcon = {
                IconButton(onClick = { isVisible = !isVisible }) {
                    Icon(painter = icon, contentDescription = "Visibility")
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = visualTransformation
        )
    }

