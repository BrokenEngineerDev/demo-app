package com.example.testapp.ui.pages.detail


data class DetailPageState(
    val id: Int,
    val originalText: String,
    val originalChecked: Boolean,
    val text: String,
    val textValidation: Boolean,
    val checked: Boolean,
    val canBeSaved:Boolean
)