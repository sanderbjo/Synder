package com.example.synder.screen.sign_up

import android.net.Uri
import androidx.annotation.StringRes

data class SignUpUiState(
    val email: String = "",
    val password: String = "",
    val repeatPassword: String = "",
    @StringRes val errorMessage: Int = 0,
    val name: String = "",
    val age: Float = 0f,
    val bio: String = "",
    val profileImageUri: Uri? = null,
    val kjonn: String  = "",
    val serEtter: String = "",

    val kjonnDropdownEnabled: Boolean = false,
    val serEtterDropdownEnabled: Boolean = false
    )
