package com.example.synder.screen.sign_up

import android.view.Display
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.synder.R
import com.example.synder.screen.sign_up.EmailField

@Composable
fun SignUpScreen(
    loggedIn: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = hiltViewModel()
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
        if (uiState.errorMessage != "")
            Text(
                text = uiState.errorMessage,
                Modifier.padding(vertical = 8.dp)
            )

        EmailField(uiState.email, viewModel::onEmailChange, fieldModifier)
        PasswordField(uiState.password, viewModel::onPasswordChange, fieldModifier)
        RepeatPasswordField(uiState.repeatPassword, viewModel::onRepeatPasswordChange, fieldModifier)
        NameField(uiState.name, viewModel::onNameChange, fieldModifier )
        AgeField(uiState.age, viewModel::onAgeChange, fieldModifier )
        BioField(uiState.bio, viewModel::onBioChange, fieldModifier )
        KjonnField(uiState.kjonn, viewModel::onKjonnChange, fieldModifier )
        SerEtterField(uiState.serEtter, viewModel::onSerEtterChange, fieldModifier )

        Spacer(modifier = Modifier.padding(bottom = 80.dp))
        Text(text = "Du må ha fylt ut alle feltene med * for å lage konto")
        Row {

            Button(
                onClick = { viewModel.onSignUpClick(loggedIn) },
                modifier = Modifier
                    .padding(16.dp, 8.dp),
            ) {
                Text(text = "create_account", fontSize = 16.sp)
            }
        }
        Spacer(modifier = Modifier.padding(bottom = 300.dp))

    }
}

    @Composable
    fun EmailField(value: String, onNewValue: (String) -> Unit, modifier: Modifier = Modifier) {
        OutlinedTextField(
            singleLine = true,
            modifier = modifier,
            value = value,
            onValueChange = { onNewValue(it) },
            placeholder = { Text(text = "email*") },
            leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "Email") }
        )
    }

    @Composable
    fun PasswordField(value: String, onNewValue: (String) -> Unit, modifier: Modifier = Modifier) {
        PasswordField(value, "password*", onNewValue, modifier)
    }

    @Composable
    fun RepeatPasswordField(
        value: String,
        onNewValue: (String) -> Unit,
        modifier: Modifier = Modifier
    ) {
        PasswordField(value, "repeat_password*", onNewValue, modifier)
    }

    @Composable
    private fun PasswordField(
        value: String,
        placeholder: String,
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
            placeholder = { Text(text = placeholder) },
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
@Composable
    fun NameField(value: String, onNewValue: (String) -> Unit, modifier: Modifier = Modifier) {
        OutlinedTextField(
            singleLine = true,
            modifier = modifier,
            value = value,
            onValueChange = { onNewValue(it) },
            placeholder = { Text(text = "fornavn*") },
            leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "Name") }
        )
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgeField(value: Float, onNewValue: (Float) -> Unit, modifier: Modifier = Modifier) {
    Text(
        text = "alder*")
    Slider(
        value = value,
        valueRange = 0f..100f,
        onValueChange = {onNewValue(it)},
        modifier = modifier
        )
    Text(
        text = value.toInt().toString() + " år",
        modifier = Modifier
            .background(
                color = Color.LightGray,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(8.dp)
    )
    Spacer(modifier = Modifier.padding(bottom = 16.dp))

/*OutlinedTextField(
        singleLine = true,
        modifier = modifier,
        value = value,
        onValueChange = { onNewValue(it) },
        placeholder = { Text(text = "age*") },
        leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "Name") }
    )*/


    /*val datePickerState = rememberDatePickerState(initialDisplayMode =  DisplayMode.Input)
    DatePicker(
        state = datePickerState, 
        modifier = Modifier.padding(16.dp),
        showModeToggle = false
        )*/
}

@Composable
fun BioField(value: String, onNewValue: (String) -> Unit, modifier: Modifier = Modifier) {
    OutlinedTextField(
        singleLine = true,
        modifier = modifier,
        value = value,
        onValueChange = { onNewValue(it) },
        placeholder = { Text(text = "bio") },
        leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "Name") }
    )
}

@Composable
fun KjonnField(value: String, onNewValue: (String) -> Unit, modifier: Modifier = Modifier) {
    OutlinedTextField(
        singleLine = true,
        modifier = modifier,
        value = value,
        onValueChange = { onNewValue(it) },
        placeholder = { Text(text = "kjønn*") },
        leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "Name") }
    )
}

@Composable
fun SerEtterField(value: String, onNewValue: (String) -> Unit, modifier: Modifier = Modifier) {
    OutlinedTextField(
        singleLine = true,
        modifier = modifier,
        value = value,
        onValueChange = { onNewValue(it) },
        placeholder = { Text(text = "hvem kjønn ser du etter?*") },
        leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "Name") }
    )
}