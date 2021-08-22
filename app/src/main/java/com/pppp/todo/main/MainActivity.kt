package com.pppp.todo.main

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pppp.todo.R
import com.pppp.todo.drawer.Drawer
import com.pppp.todo.main.MainActivityViewModel.Event.OnNewListClicked
import com.pppp.todo.main.MainActivityViewModel.Event.OnNewListDismissed
import com.pppp.todo.main.view.AppBar
import com.pppp.todo.main.view.MainScreen
import com.pppp.todo.ui.theme.ToDoTheme
import com.pppp.uielements.BottomSheet
import com.pppp.uielements.ClosableInputText
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
        setContent {
            ToDoTheme {
                Surface(color = MaterialTheme.colors.background) {
                    val mainViewModel: MainActivityViewModel = viewModel()
                    val state = mainViewModel.states.collectAsState().value
                    val scaffoldState = rememberScaffoldState()
                    Scaffold(
                        scaffoldState = scaffoldState,
                        content = {
                            MainContent(state)
                            BottomSheet(
                                isExpanded = state.addNewListIsShowing,
                                onDismissed = { mainViewModel(OnNewListDismissed) },
                                onConfirmClicked = {},
                                onCancelClicked = { mainViewModel(OnNewListDismissed) },
                                sheetContent = { ClosableInputText() },
                                title = stringResource(id = R.string.create_new_list)
                            )
                        },
                        drawerContent = {
                            Drawer(
                                addListClicked = {
                                    scaffoldState.toggleDrawer()
                                    mainViewModel(OnNewListClicked)
                                }
                            )
                        },
                        topBar = {
                            AppBar {
                                //mainViewModel(OnDrawerOpened)
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
    private fun MainContent(state: MainActivityViewModel.ViewState) {
        when {
            state.isLoading -> Loading()
            state.listId == null -> Loading()
            else -> MainScreen(state.listId)
        }
    }

    companion object {
        const val PENDING_INTENT_REQUEST_CODE = 2
    }
}
