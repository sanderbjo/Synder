package com.example.synder.screen.sign_up

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.synder.common.ext.isValidEmail
import com.example.synder.common.ext.isValidPassword
import com.example.synder.service.AccountService
import com.example.synder.service.ImgStorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor( private val accountService: AccountService, private val imgStorageService: ImgStorageService) : ViewModel() {

    var uiState = mutableStateOf(SignUpUiState())
        private set
    private val email
        get() = uiState.value.email
    private val password
        get() = uiState.value.password
    private val name
        get() = uiState.value.name
    private val age
        get() = uiState.value.age
    private val bio
        get() = uiState.value.bio
    private val kjonn
        get() = uiState.value.kjonn
    private val serEtter
        get() = uiState.value.serEtter
    private val profileImageUri
        get() = uiState.value.profileImageUri


    fun onEmailChange(newValue: String) {
        uiState.value = uiState.value.copy(email = newValue)
    }

    fun onPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(password = newValue)
    }

    fun onRepeatPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(repeatPassword = newValue)
    }

    fun onNameChange(newValue: String) {
        uiState.value = uiState.value.copy(name = newValue)
    }

    fun onAgeChange(newValue: Float) {
        uiState.value = uiState.value.copy(age = newValue)
    }

    fun onBioChange(newValue: String) {
        uiState.value = uiState.value.copy(bio = newValue)
    }

    fun onKjonnChange(newValue: String) {
        uiState.value = uiState.value.copy(kjonn = newValue)
    }

    fun onKjonnEnabledChange(newValue: Boolean) {
        uiState.value = uiState.value.copy(kjonnDropdownEnabled = newValue)
    }

    fun onSerEtterChange(newValue: String) {
        uiState.value = uiState.value.copy(serEtter = newValue)
    }

    fun onSerEtterEnabledChange(newValue: Boolean) {
        uiState.value = uiState.value.copy(serEtterDropdownEnabled = newValue)
    }

    fun onProfileImageUrisChange(newValue: Uri?) {
        uiState.value = uiState.value.copy(profileImageUri = newValue)
    }

    fun onSignUpClick(loggedIn: () -> Unit) {
        if (!email.isValidEmail()) {
            uiState.value = uiState.value.copy(errorMessage = "email_error")
            return
        }

        else if (!password.isValidPassword()) {
            uiState.value = uiState.value.copy(errorMessage = "password_error")
            return
        }

        else if (password != uiState.value.repeatPassword) {
            uiState.value = uiState.value.copy(errorMessage = "password_match_error")
            return
        }

        viewModelScope.launch {
            try {

                accountService.linkAccount(email, password, name, age, bio, profileImageUri , kjonn, serEtter) { error ->
                    if (error == null)
                        loggedIn()
                }
                imgStorageService.addFile(profileImageUri,accountService.currentUserId)


                //accountService.currentUser
                //imgStorageService.getImg(accountService.currentUserId)

            }
            catch(e: Exception) {
                uiState.value = uiState.value.copy(errorMessage = "could_not_create_account")
            }
        }
    }
}