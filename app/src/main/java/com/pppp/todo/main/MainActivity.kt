package com.pppp.todo.main

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import com.pppp.todo.drawer.Drawer
import com.pppp.todo.main.MainActivityViewModel.Event.OnNewListClicked
import com.pppp.todo.main.MainActivityViewModel.ViewState
import com.pppp.todo.main.view.AppBar
import com.pppp.todo.main.view.MainScreen
import com.pppp.todo.ui.theme.ToDoTheme
import com.pppp.uielements.BottomSheet
import com.pppp.uielements.Loading
import com.pppp.uielements.toggleDrawer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @ExperimentalComposeUiApi
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        val viewModel: MainActivityViewModel by viewModels()
        setContent {
            val state = viewModel.states.collectAsState().value
            ToDoTheme {
                Surface(color = MaterialTheme.colors.background) {
                    val scaffoldState = rememberScaffoldState()
                    Scaffold(
                        scaffoldState = scaffoldState,
                        content = {
                            content(state)
                        },
                        drawerContent = {
                            Drawer(
                                addListClicked = {
                                    scaffoldState.toggleDrawer()
                                    viewModel(OnNewListClicked)
                                }
                            )
                        },
                        topBar = {
                            AppBar {
                                scaffoldState.toggleDrawer()
                            }
                        }
                    )
                }
            }
        }
    }

    @ExperimentalMaterialApi
    @ExperimentalComposeUiApi
    @Composable
    private fun content(state: ViewState) {
        when {
            state.isLoading -> Loading()
            state.listId == null -> Loading()
            else -> MainScreen(state.listId)
        }
        BottomSheet(
            isExpanded = state.addNewListIsShowing,
            sheetContent = { Text("Foo") }
        )
    }

    companion object {
        const val PENDING_INTENT_REQUEST_CODE = 2

    }
}
