package com.example.testapp.ui.pages.list

sealed interface ListPageEffect {
    data object LogOut : ListPageEffect
    data class GoToDetails(val itemId: Int) : ListPageEffect
}