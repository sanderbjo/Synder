package com.example.synder.screen.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.synder.common.ext.isValidEmail
import com.example.synder.common.ext.isValidPassword
import com.example.synder.service.AccountService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val accountService: AccountService) : ViewModel() {

    var uiState = mutableStateOf(LoginUiState())
        private set

    private val email
        get() = uiState.value.email

    private val password
        get() = uiState.value.password

    fun onEmailChange(newValue: String) {
        uiState.value = uiState.value.copy(email = newValue)
    }

    fun onPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(password = newValue)
    }


    fun onLoginClick(loggedIn: () -> Unit) {
        if (!email.isValidEmail()) {
            uiState.value = uiState.value.copy(errorMessage = "Feil email")
            return
        }

        if (!password.isValidPassword()) {
            uiState.value = uiState.value.copy(errorMessage = "Feil passord")
            return
        }

        viewModelScope.launch {
            try {
                accountService.authenticate(email, password) { error ->
                    if (error == null)
                        loggedIn()
                }
            }
            catch(e: Exception) {
                uiState.value = uiState.value.copy(errorMessage = "Kunne ikke logge inn")
            }
        }
    }



}