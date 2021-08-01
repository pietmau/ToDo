package com.pppp.todo.main.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomAppBar
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pppp.todo.GenericViewModelWithOneOffEvents
import com.pppp.todo.edittodo.EditItem
import com.pppp.todo.exaustive
import com.pppp.todo.main.viewmodel.AddToDo.Showing
import com.pppp.todo.main.viewmodel.AddToDo.Hidden
import com.pppp.todo.main.viewmodel.MainViewEvent
import com.pppp.todo.main.viewmodel.MainViewEvent.OnAddToDoClicked
import com.pppp.todo.main.viewmodel.MainViewEvent.OnEditToDoClicked
import com.pppp.todo.main.viewmodel.MainViewModel
import com.pppp.todo.main.viewmodel.MainViewState
import com.pppp.todo.main.viewmodel.OneOffEvent
import com.pppp.uielements.Loading
import kotlinx.coroutines.launch


interface MainScreen {

    companion object {
        @ExperimentalComposeUiApi
        @ExperimentalMaterialApi
        @Composable
        fun Content(
            listId: String = "",
            viewModel: GenericViewModelWithOneOffEvents<MainViewState, MainViewEvent, OneOffEvent> =
                viewModel<MainViewModel>(),
            onNavigationIconClicked: suspend () -> Unit = {}
        ) {
            LaunchedEffect(listId) {
                viewModel(MainViewEvent.GetList(listId))
            }
            val state by viewModel.states.collectAsState()
            val addToDoBottomSheetState = rememberModalBottomSheetState(
                ModalBottomSheetValue.Hidden
            )
            LaunchedEffect(state.addToDo) {
                launch {
                    when (state.addToDo) {
                        Hidden -> addToDoBottomSheetState.hide()
                        Showing -> addToDoBottomSheetState.show()
                    }.exaustive
                }
            }
            Scaffold(
                floatingActionButton = {
                    Fab(onClick = {
                        viewModel(OnAddToDoClicked)
                    })
                },
                isFloatingActionButtonDocked = true,
                bottomBar = {
                    BottomAppBar {}
                },
                topBar = {
                    TopAppBar {
                        val scope = rememberCoroutineScope()
                        IconButton(
                            onClick = {
                                scope.launch {
                                    onNavigationIconClicked()
                                }
                            },
                            content = {
                                Icon(
                                    imageVector = Icons.Filled.Menu,
                                    contentDescription = null
                                )
                            })
                    }
                },
                content = {
                    Content(
                        state = state,
                        onToDoChecked = { listId, id, checked ->
                            viewModel(MainViewEvent.OnToDoCompleted(listId, id, checked))
                        },
                        onEditToDoClicked = { listId, itemId ->
                            viewModel(OnEditToDoClicked(listId = listId, itemId = itemId))
                        }
                    )
                }
            )
            AddItem.Content(state.addToDo == Showing) {
                viewModel(it)
            }
            EditItem.Content(state.itemBeingEdited) {
                viewModel(it)
            }
        }

        @Composable
        fun Fab(onClick: () -> Unit) {
            FloatingActionButton(
                modifier = Modifier.size(48.dp),
                onClick = onClick,
                content = { Icon(imageVector = Icons.Outlined.Add, contentDescription = null) }
            )
        }

        @Composable
        private fun Content(
            state: MainViewState,
            onToDoChecked: (listId: String, itemId: String, checked: Boolean) -> Unit = { _, _, _ -> },
            onEditToDoClicked: (listId: String, itemId: String) -> Unit = { _, _ -> }
        ) {
            when (state.isLoading) {
                true -> ListOfToDos(state, onToDoChecked, onEditToDoClicked)
                else -> Loading.Content()
            }.exaustive
        }
    }
}