package com.example.testapp.ui.pages.login

sealed interface LoginPageEffect {
    object NavigateToList : LoginPageEffect
    data class ShowSnackbar(val message: String) : LoginPageEffect
}
