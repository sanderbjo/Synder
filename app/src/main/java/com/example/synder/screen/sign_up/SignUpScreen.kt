package com.example.synder.screen.sign_up

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.synder.R

//deler av koden er hentet/inspirert av kodeeksempel i forelesning/github
//men det er endret på for å passe inn i dette prosjektet og det er lagt til en del mer funksjonalitet/elementer
@Composable
fun SignUpScreen(
    loggedIn: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState

    val fieldModifier = Modifier
        .fillMaxWidth()
        .padding(16.dp, 8.dp)


    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.padding(top = 120.dp))

        Text(text = stringResource(R.string.registrering), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineLarge
        )
        EmailField(uiState.email, viewModel::onEmailChange, fieldModifier)
        PasswordField(uiState.password, viewModel::onPasswordChange, fieldModifier)
        RepeatPasswordField(uiState.repeatPassword, viewModel::onRepeatPasswordChange, fieldModifier)
        ImgField(uiState.profileImageUri,viewModel::onProfileImageUrisChange, fieldModifier)
        NameField(uiState.name, viewModel::onNameChange, fieldModifier )
        AgeField(uiState.age, viewModel::onAgeChange, fieldModifier )
        BioField(uiState.bio, viewModel::onBioChange, fieldModifier )
        KjonnField(uiState.kjonn, uiState.kjonnDropdownEnabled ,viewModel::onKjonnEnabledChange,viewModel::onKjonnChange , fieldModifier )
        SerEtterField(uiState.serEtter, uiState.serEtterDropdownEnabled,viewModel::onSerEtterEnabledChange, viewModel::onSerEtterChange, fieldModifier )

        Spacer(modifier = Modifier.padding(bottom = 80.dp))

        //feilmelding om det er en feil i sign up
        if (uiState.errorMessage != 0)
            Text(
                text = stringResource(id = uiState.errorMessage),
                Modifier.padding(16.dp, 8.dp),
                color = MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.Bold
            )

        Row {
            Button(
                onClick = { viewModel.onSignUpClick(loggedIn) },
                modifier = Modifier
                    .padding(16.dp, 8.dp),
            ) {
                Text(text = stringResource(R.string.lag_bruker), fontSize = 16.sp)
            }
        }
        //lagt til en liten spacer som gjør at når man har tastaturet åpent så kan man swipe ned for å se knappene
        Spacer(modifier = Modifier.padding(bottom = 260.dp))

    }
}

    @Composable
    fun ImgField(value: Uri?, onNewValue: (Uri?) -> Unit, modifier: Modifier) {
        //var selectedPhoto by remember { mutableStateOf<Uri?>(null) }

        val photoPicker = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = {onNewValue(it)})
        //ActivityResultLauncher()

        Column(modifier.border(width = 1.dp, color = Color.Gray),
                horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = stringResource(R.string.profilbilde_star))
            if (value == null){
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = stringResource(R.string.profilbilde),
                    modifier = Modifier
                        .size(160.dp)
                        .clip(CircleShape)
                        .border(width = 1.dp, color = Color.Gray, shape = CircleShape),
                    contentScale = ContentScale.Crop,

                )
            }else {
                AsyncImage(
                    model = value,
                    contentDescription = stringResource(R.string.placeholder_profilbilde) ,
                    modifier = Modifier
                        .size(160.dp)
                        .clip(CircleShape)
                        .border(width = 1.dp, color = Color.Gray, shape = CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
            Button(onClick = {
                photoPicker.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            },
                Modifier.padding(bottom = 8.dp))
            {
                Text(text = stringResource(R.string.legg_til_profilbilde))
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
            placeholder = { Text(text = stringResource(R.string.email_star)) },
            leadingIcon = { Icon(imageVector = Icons.Default.Email,
                contentDescription = stringResource(R.string.email )
            ) }
        )
    }

    @Composable
    fun PasswordField(value: String, onNewValue: (String) -> Unit, modifier: Modifier = Modifier) {
        PasswordField(value, stringResource(R.string.passord_star), onNewValue, modifier)
    }

    @Composable
    fun RepeatPasswordField(
        value: String,
        onNewValue: (String) -> Unit,
        modifier: Modifier = Modifier
    ) {
        PasswordField(value, stringResource(R.string.gjenta_passord), onNewValue, modifier)
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
            leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = stringResource(R.string.lock)) },
            trailingIcon = {
                IconButton(onClick = { isVisible = !isVisible }) {
                    Icon(painter = icon, contentDescription = stringResource(R.string.visibility))
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
            placeholder = { Text(text = stringResource(R.string.fornavn)) },
            leadingIcon = { Icon(imageVector = Icons.Default.Person, contentDescription = stringResource(
                R.string.name
            )
            ) }
        )
    }

@Composable
fun AgeField(value: Float, onNewValue: (Float) -> Unit, modifier: Modifier = Modifier) {
    Spacer(modifier = Modifier.padding(bottom = 16.dp))
    Text(
        text = stringResource(R.string.alder)
    )
    Slider(
        value = value,
        valueRange = 0f..100f,
        onValueChange = {onNewValue(it)},
        modifier = modifier.padding(20.dp, 0.dp)
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

}

@Composable
fun BioField(value: String, onNewValue: (String) -> Unit, modifier: Modifier = Modifier) {
    OutlinedTextField(
        singleLine = true,
        modifier = modifier,
        value = value,
        onValueChange = { onNewValue(it) },
        placeholder = { Text(text = stringResource(R.string.bio)) },
        leadingIcon = { Icon(imageVector = Icons.Default.Person, contentDescription = stringResource(R.string.bio)) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KjonnField(value: String, enabled: Boolean ,onEnabledChange: (Boolean) -> Unit, onNewValue: (String) -> Unit , modifier: Modifier = Modifier) {

    //for enkelhets skyld bare mann og kvinne
    val kjonn = listOf(stringResource(R.string.mann), stringResource(R.string.kvinne))

    ExposedDropdownMenuBox(
        expanded = enabled,
        onExpandedChange = { newValue ->
            onEnabledChange(!enabled)
        },
        modifier = modifier
    ) {
        OutlinedTextField(
            singleLine = true,
            value = value,
            onValueChange = {},
            readOnly = true,
            leadingIcon = { Icon(imageVector = Icons.Default.Person, contentDescription = stringResource(
                R.string.person_icon
            )
            ) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = enabled)
                    },
            placeholder = { Text(text = stringResource(R.string.kj_nn))},
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()

        )
        ExposedDropdownMenu(
            expanded = enabled,
            onDismissRequest = { onEnabledChange(!enabled) },
        ) {
            kjonn.forEach{
                DropdownMenuItem(
                    text = { Text(text = it) },
                    onClick = {
                        onNewValue(it)
                        onEnabledChange(!enabled)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SerEtterField(value: String, enabled: Boolean ,onEnabledChange: (Boolean) -> Unit, onNewValue: (String) -> Unit, modifier: Modifier = Modifier) {

    //for enkelhets skyld bare mann og kvinne
    val kjonn = listOf(stringResource(R.string.mann), stringResource(R.string.kvinne))

    ExposedDropdownMenuBox(
        expanded = enabled,
        onExpandedChange = { newValue ->
            onEnabledChange(!enabled)
        },
        modifier = modifier
    ) {
        OutlinedTextField(
            singleLine = true,
            value = value,
            onValueChange = {},
            readOnly = true,
            leadingIcon = { Icon(imageVector = Icons.Default.Person, contentDescription = stringResource(
                R.string.person_icon
            )) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = enabled)
            },
            placeholder = { Text(text = stringResource(R.string.hvem_kj_nn_ser_du_etter)) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()

        )
        ExposedDropdownMenu(
            expanded = enabled,
            onDismissRequest = { onEnabledChange(!enabled) },
        ) {
            kjonn.forEach{
                DropdownMenuItem(
                    text = { Text(text = it) },
                    onClick = {
                        onNewValue(it)
                        onEnabledChange(!enabled)
                    }
                )
            }
        }
    }
}