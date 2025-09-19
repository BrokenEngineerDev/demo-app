package com.example.testapp.ui.pages.detail

sealed interface DetailPageEffect {
    data object PopBack : DetailPageEffect
    data object LogOut : DetailPageEffect

    data class ShowSnackbar(val message: String) : DetailPageEffect
}