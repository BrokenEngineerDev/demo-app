@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.testapp.ui.pages.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.testapp.ui.widgets.CustomTextField
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginPageComposable(
    navHostController: NavController,
    viewModel: LoginPageViewModel = koinViewModel<LoginPageViewModel>()
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                LoginPageEffect.NavigateToList -> {
                    navHostController.navigate("list_page") {
                        popUpTo("login_page") {
                            inclusive = true
                        }
                    }
                }

                is LoginPageEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(message = effect.message)
                }
            }
        }
    }

    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Login") }) },
        bottomBar = {
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {

            }
        }, snackbarHost = {
            SnackbarHost(snackbarHostState)
        }) { contentPadding: PaddingValues ->

        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center
            ) {

                CustomTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.username,
                    onValueChange = { viewModel.process(LoginPageIntent.SetUsername(it)) },
                    label = "Username",
                    placeholder = LoginPageViewModel.PREDEFINED_USER,
                    errorText = state.usernameError
                )

                Spacer(modifier = Modifier.height(8.dp))

                CustomTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.password,
                    onValueChange = { viewModel.process(LoginPageIntent.SetPassword(it)) },
                    label = "Password",
                    placeholder = LoginPageViewModel.PREDEFINED_PASSWORD,
                    errorText = state.passwordError,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(Modifier.height(8.dp))
                if (state.isLoading) LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            Button(
                onClick = { viewModel.process(LoginPageIntent.Submit) },
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                    .fillMaxWidth(),
                enabled = state.canSubmit,
                contentPadding = PaddingValues(16.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Login")
            }
        }
    }
}