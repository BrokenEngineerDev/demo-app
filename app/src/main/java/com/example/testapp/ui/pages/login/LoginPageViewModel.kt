package com.example.testapp.ui.pages.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginPageViewModel() : ViewModel() {


    private val _state = MutableStateFlow(LoginPageState())
    val state: StateFlow<LoginPageState> = _state.asStateFlow()


    private val _effect = Channel<LoginPageEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()


    init {
        viewModelScope.launch { validateFields() }
    }


    fun process(intent: LoginPageIntent) {
        when (intent) {
            is LoginPageIntent.SetUsername -> setUsername(intent.value)
            is LoginPageIntent.SetPassword -> setPassword(intent.value)
            LoginPageIntent.Submit -> submit()
        }
    }

    private fun setUsername(username: String) {
        viewModelScope.launch {
            _state.update { it.copy(username = username, usernameError = null) }
            validateFields()
        }
    }

    private fun setPassword(password: String) {
        viewModelScope.launch {
            _state.update { it.copy(password = password, passwordError = null) }
            validateFields()
        }
    }

    private suspend fun validateFields() {
        val usernameIsValid = validateUsername()
        val passwordIsValid = validatePasswords()

        _state.update { it.copy(canSubmit = usernameIsValid && passwordIsValid) }
    }

    private suspend fun validateUsername(): Boolean {
        val username = _state.value.username
        if (username.length < 6) {
            _state.update { it.copy(usernameError = "Username is too short") }
            return false
        }

        if (username.length > 16) {
            _state.update { it.copy(usernameError = "Username is too long") }
            return false
        }

        if (!username.all { it.isLetter() || it == '_' }) {
            _state.update { it.copy(usernameError = "Username has disallowed symbols") }
            return false
        }

        _state.update { it.copy(usernameError = null) }
        return true
    }

    private suspend fun validatePasswords(): Boolean {
        val password = _state.value.password
        if (password.length < 8) {
            _state.update { it.copy(passwordError = "Password is too short") }
            return false
        }

        if (password.length > 12) {
            _state.update { it.copy(passwordError = "Password is too long") }
            return false
        }

        if (!password.all { it.isDigit() || it == '_' }) {
            _state.update { it.copy(passwordError = "Password has disallowed symbols") }
            return false
        }
        _state.update { it.copy(passwordError = null) }
        return true
    }


    private fun submit() {
        viewModelScope.launch {
            val currentState = _state.value
            val username: String = currentState.username
            val password: String = currentState.password

            _state.update { it.copy(isLoading = true) }
            delay(3000)

            _state.update { it.copy(isLoading = false) }
            if (username != PREDEFINED_USER) {
                _effect.send(LoginPageEffect.ShowSnackbar(message = "Unknown user"))
                return@launch
            }

            if (password != PREDEFINED_PASSWORD) {
                _effect.send(LoginPageEffect.ShowSnackbar(message = "Invalid password"))
                return@launch
            }

            _effect.send(LoginPageEffect.NavigateToList)
        }
    }

    companion object {
        const val PREDEFINED_USER = "compose_user"
        const val PREDEFINED_PASSWORD = "12345678"
    }
}