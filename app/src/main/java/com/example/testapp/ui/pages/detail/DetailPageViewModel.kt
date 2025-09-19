package com.example.testapp.ui.pages.detail

import androidx.lifecycle.SavedStateHandle
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

class DetailPageViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val itemsRepository: ItemsRepository
) : ViewModel() {

    private val _state = MutableStateFlow<DetailPageState?>(null)
    val state: StateFlow<DetailPageState?> = _state.asStateFlow()


    private val _effect = Channel<DetailPageEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()


    init {
        viewModelScope.launch {
            val itemId = savedStateHandle.get<Int>("item_id") ?: return@launch

            val item = itemsRepository.getItemById(itemId)
            if (item == null) {
                _effect.send(DetailPageEffect.ShowSnackbar(message = "Item not found"))
                return@launch
            }

            _state.value = DetailPageState(
                id = item.id,
                originalText = item.text,
                originalChecked = item.checked,
                textValidation = false,
                text = item.text,
                checked = item.checked,
                canBeSaved = false
            )
        }
    }


    fun process(intent: DetailPageIntent) {
        when (intent) {
            is DetailPageIntent.SetText -> setText(intent.text)
            is DetailPageIntent.SetChecked -> setChecked(intent.checked)
            DetailPageIntent.Save -> saveIfValid()
            DetailPageIntent.Cancel -> viewModelScope.launch { _effect.send(DetailPageEffect.PopBack) }
            DetailPageIntent.LogOut -> viewModelScope.launch { _effect.send(DetailPageEffect.LogOut) }
        }
    }

    private fun setChecked(checked: Boolean) {
        viewModelScope.launch {
            _state.update {
                it?.copy(
                    checked = checked,
                    canBeSaved = checked != it.originalChecked
                )
            }
        }
    }

    private fun setText(text: String) {
        viewModelScope.launch {
            _state.update {
                it?.copy(
                    text = text,
                    canBeSaved = text != it.originalText
                )
            }
        }
    }


    private fun saveIfValid() {
        viewModelScope.launch {
            val currentState = _state.value ?: return@launch

            if (currentState.text.isBlank()) {
                _effect.send(DetailPageEffect.ShowSnackbar("Text can't be empty"))
                return@launch
            }

            if (currentState.text.length > 100) {
                _effect.send(DetailPageEffect.ShowSnackbar("Max text length is 100 characters"))
                return@launch
            }

            itemsRepository.updateItemById(currentState.id) { item: Item ->
                item.copy(text = currentState.text, checked = currentState.checked)
            }

            _effect.send(DetailPageEffect.PopBack)
        }
    }
}