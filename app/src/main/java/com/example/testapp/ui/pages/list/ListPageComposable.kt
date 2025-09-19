@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.testapp.ui.pages.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.testapp.ui.widgets.ItemRowMVI
import kotlinx.coroutines.flow.distinctUntilChanged
import org.koin.androidx.compose.koinViewModel

@Composable
fun ListPageComposable(
    navHostController: NavHostController,
    viewModel: ListPageViewModel = koinViewModel<ListPageViewModel>()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val toggleableState: ToggleableState by remember(state) {
        derivedStateOf {
            when {
                state.items.all { it.checked } -> ToggleableState.On
                state.items.all { !it.checked } -> ToggleableState.Off
                else -> ToggleableState.Indeterminate
            }
        }
    }

    val toggleAction = remember(toggleableState) {
        {
            when (toggleableState) {
                ToggleableState.On -> viewModel.process(ListPageIntent.SetAll(false))
                else -> viewModel.process(ListPageIntent.SetAll(true))
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ListPageEffect.GoToDetails -> {
                    navHostController.navigate("detail_page/${effect.itemId}")
                }

                ListPageEffect.LogOut -> {
                    navHostController.navigate("login_page") {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }

    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = state.savedIndex,
        initialFirstVisibleItemScrollOffset = state.savedOffset
    )

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset }.distinctUntilChanged()
            .collect { (i, off) -> viewModel.process(ListPageIntent.SaveScroll(i, off)) }
    }


    Scaffold(
        topBar = {
            TopAppBar(title = { Text("List") }, actions = {
                IconButton(onClick = {
                    viewModel.process(ListPageIntent.LogOut)
                }) {
                    Icon(Icons.Default.ExitToApp, contentDescription = "Logout")
                }
            })
        },
    ) { padding ->


        LazyColumn(
            state = listState, modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            stickyHeader {
                Surface() {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TriStateCheckbox(
                            state = toggleableState, onClick = toggleAction
                        )

                        Spacer(
                            modifier = Modifier.width(8.dp)
                        )
                        Text("Check it all")
                    }
                }
            }

            items(items = state.items) { item ->
                ItemRowMVI(modifier = Modifier.fillMaxWidth(), item = item, onClick = {
                    viewModel.process(ListPageIntent.OnItemClicked(item.id))
                }, onCheckedChange = { checked ->
                    viewModel.process(ListPageIntent.SetItemChecked(item.id, checked))
                })
            }
        }
    }
}