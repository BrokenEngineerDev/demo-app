package com.example.testapp.di

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.testapp.data.Item

class ItemsRepository {


    private val mutableList: SnapshotStateList<Item> = mutableStateListOf<Item>().apply {
        List(30) {
            this.add(
                Item(it, it.toString(), checked = false)
            )
        }
    }

    fun getAllItems(): List<Item> {
        return mutableList
    }

    fun getItemById(id: Int): Item? {
        return mutableList.firstOrNull { it.id == id }
    }

    fun updateItemById(id: Int, update: (Item) -> Item) {
        val index = mutableList.indexOfFirst { it.id == id }
        if (index != -1) {
            val oldItem = mutableList[index]
            mutableList[index] = update(oldItem)
        }
    }

    fun updateAllItems(update: (Item) -> Item) {
        for ((index, item) in mutableList.withIndex()) {
            mutableList[index] = update(item)
        }
    }
}