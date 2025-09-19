package com.example.testapp.ui.pages.detail

sealed interface DetailPageIntent {
    data class SetText(val text: String) : DetailPageIntent
    data class SetChecked(val checked: Boolean) : DetailPageIntent
    data object Save : DetailPageIntent
    data object Cancel : DetailPageIntent
    data object LogOut : DetailPageIntent {

    }
}