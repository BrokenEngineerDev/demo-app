package com.example.testapp.ui.pages.login

sealed interface LoginPageIntent {
    data class SetUsername(val value: String) : LoginPageIntent
    data class SetPassword(val value: String) : LoginPageIntent
    data object Submit : LoginPageIntent
}