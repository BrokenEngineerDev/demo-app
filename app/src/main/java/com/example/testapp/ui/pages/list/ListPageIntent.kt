package com.example.testapp.ui.pages.list

sealed interface ListPageIntent {
    data object LogOut : ListPageIntent

    data class SetItemChecked(val id: Int, val checked: Boolean) : ListPageIntent
    data class SetAll(val checked: Boolean) : ListPageIntent
    data class SaveScroll(val index: Int, val offset: Int) : ListPageIntent
    data class OnItemClicked(val id: Int) : ListPageIntent
}