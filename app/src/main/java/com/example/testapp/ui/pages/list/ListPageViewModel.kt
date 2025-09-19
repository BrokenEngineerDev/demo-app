package com.example.testapp.ui.pages.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.data.Item
import com.example.testapp.di.ItemsRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface Tri {
    data object Unchecked : Tri
    data object Checked : Tri
    data object Indeterminate : Tri
}

class ListPageViewModel(
    private val itemsRepository: ItemsRepository
) : ViewModel() {
    private val _state = MutableStateFlow(ListState())
    val state: StateFlow<ListState> = _state.asStateFlow()

    private val _effect = Channel<ListPageEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        viewModelScope.launch {
            _state.update { it.copy(items = itemsRepository.getAllItems()) }
        }
    }

    fun process(intent: ListPageIntent) {
        when (intent) {
            is ListPageIntent.SetItemChecked -> setChecked(intent.id, intent.checked)
            is ListPageIntent.SetAll -> setAll(intent.checked)
            is ListPageIntent.SaveScroll -> saveScroll(intent.index, intent.offset)
            ListPageIntent.LogOut -> logout()
            is ListPageIntent.OnItemClicked -> onItemClicked(intent.id)
        }
    }

    private fun onItemClicked(id: Int) {
        viewModelScope.launch {
            _effect.send(ListPageEffect.GoToDetails(id))
        }
    }

    private fun saveScroll(index: Int, offset: Int) {
        viewModelScope.launch {
            _state.update { it.copy(savedIndex = index, savedOffset = offset) }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            _effect.send(ListPageEffect.LogOut)
        }
    }

    private fun setChecked(id: Int, checked: Boolean) {
        viewModelScope.launch {
            itemsRepository.updateItemById(id) { item: Item -> item.copy(checked = checked) }
            _state.update { it.copy(items = itemsRepository.getAllItems()) }
        }
    }


    private fun setAll(checked: Boolean) {
        viewModelScope.launch {
            itemsRepository.updateAllItems { it.copy(checked = checked) }
            _state.update { it.copy(items = itemsRepository.getAllItems()) }
        }
    }
}