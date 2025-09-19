@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.testapp.ui.pages.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.testapp.ui.widgets.CustomTextField
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetailPageComposable(
    navHostController: NavHostController,
    viewModel: DetailPageViewModel = koinViewModel<DetailPageViewModel>()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }


    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is DetailPageEffect.PopBack -> navHostController.popBackStack()
                is DetailPageEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(effect.message)
                }

                DetailPageEffect.LogOut -> {
                    navHostController.navigate("login_page") {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }

    Scaffold(topBar = {
        TopAppBar(title = { Text("Details") }, actions = {
            IconButton(onClick = {
                viewModel.process(DetailPageIntent.LogOut)
            }) {
                Icon(Icons.Default.ExitToApp, contentDescription = "Logout")
            }
        })
    }, snackbarHost = {
        SnackbarHost(snackbarHostState)
    }) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.CenterVertically)
        ) {
            when (val currentState = state) {
                null -> CircularProgressIndicator()
                else -> {

                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(
                            8.dp,
                            alignment = Alignment.CenterVertically
                        )
                    ) {
                        CustomTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = currentState.text,
                            onValueChange = { viewModel.process(DetailPageIntent.SetText(it)) },
                            label = "Item text",
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(
                                8.dp,
                                alignment = Alignment.Start
                            )
                        ) {
                            Checkbox(
                                checked = currentState.checked,
                                onCheckedChange = { viewModel.process(DetailPageIntent.SetChecked(it)) }
                            )

                            Text(
                                "Checked",
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Start
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                    ) {
                        TextButton(
                            onClick = { viewModel.process(DetailPageIntent.Cancel) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Cancel")
                        }

                        Spacer(Modifier.width(8.dp))

                        Button(
                            onClick = { viewModel.process(DetailPageIntent.Save) },
                            enabled = currentState.canBeSaved,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Send, contentDescription = "Save")
                            Spacer(Modifier.width(6.dp))
                            Text("Save")
                        }

                    }
                }
            }
        }
    }
}