package com.pppp.todo.main

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Surface
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import com.pppp.todo.drawer.Drawer
import com.pppp.todo.exaustive
import com.pppp.todo.main.view.MainScreen
import com.pppp.todo.ui.theme.ToDoTheme
import dagger.hilt.android.AndroidEntryPoint
import com.pppp.todo.main.MainActivityViewModel.ViewState
import com.pppp.todo.main.MainActivityViewModel.ViewState.Content
import com.pppp.todo.main.MainActivityViewModel.ViewState.None
import com.pppp.uielements.Loading

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @ExperimentalComposeUiApi
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        val viewModel: MainActivityViewModel by viewModels()

        setContent {
            val state by viewModel.states.collectAsState()
            ToDoTheme {
                Surface(color = MaterialTheme.colors.background) {
                    val scaffoldState = rememberScaffoldState()
                    Scaffold(
                        scaffoldState = scaffoldState,
                        content = { Content(state, scaffoldState) },
                        drawerContent = { Drawer.Content() }
                    )
                }
            }
        }
    }

    companion object {
        const val PENDING_INTENT_REQUEST_CODE = 2

        @ExperimentalMaterialApi
        @ExperimentalComposeUiApi
        @Composable
        private fun Content(state: ViewState, scaffoldState: ScaffoldState) {
            when (state) {
                is Content -> MainScreen.Content(
                    listId = state.listId,
                    onNavigationIconClicked = scaffoldState::toggle
                )
                is None -> Loading.Content()
            }.exaustive

        }
    }
}

private suspend fun ScaffoldState.toggle() {
    if (drawerState.isOpen) {
        drawerState.close()
    } else {
        drawerState.open()
    }
}