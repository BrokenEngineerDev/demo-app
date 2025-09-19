package com.example.testapp.ui.pages.list

import com.example.testapp.data.Item

data class ListState(
    val items: List<Item> = emptyList(),
    val savedIndex: Int = 0,
    val savedOffset: Int = 0
)