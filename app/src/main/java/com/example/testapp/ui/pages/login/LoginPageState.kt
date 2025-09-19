package com.example.testapp.ui.pages.login

data class LoginPageState(
    val username: String = "compose_user",
    val usernameError: String? = null,
    val password: String = "12345678",
    val passwordError: String? = null,
    val canSubmit: Boolean = false,
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val error: String? = null
)