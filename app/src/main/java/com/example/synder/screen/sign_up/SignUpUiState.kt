package com.example.synder.screen.sign_up

import android.util.Size

data class SignUpUiState(
    val email: String = "",
    val password: String = "",
    val repeatPassword: String = "",
    val errorMessage: String = "",
    val name: String = "",
    val age: Float = 0f,
    val bio: String = "",
    val profileImageUrl: String = "",
    val kjonn: String  = "",
    val serEtter: String = "",

    val kjonnDropdownEnabled: Boolean = false,
    val serEtterDropdownEnabled: Boolean = false
    )
