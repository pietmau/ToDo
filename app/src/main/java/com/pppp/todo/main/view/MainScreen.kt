package com.pppp.todo.main.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ModalBottomSheetValue.Hidden
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pppp.todo.exaustive
import com.pppp.todo.main.MainViewModel
import com.pppp.todo.main.ToDoViewEvent
import com.pppp.todo.main.ToDoViewEvent.OnAddToDoClicked
import com.pppp.todo.main.TodoMainViewModel as TodoMainViewModel1

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun MainScreen(viewModel: MainViewModel) {
    val state by viewModel.uiState.collectAsState()
    MainScreenImpl(state) {
        viewModel(it)
    }
}

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
private fun MainScreenImpl(state: TodoMainViewModel1, onEvent: (ToDoViewEvent) -> Unit) {
    var modalBottomSheetState: ModalBottomSheetState = rememberModalBottomSheetState(Hidden)
    LaunchedEffect(state.isAddTodoShowing) {
        if (state.isAddTodoShowing) {
            modalBottomSheetState.show()
        } else {
            modalBottomSheetState.hide()
        }
    }
    Box {
        Scaffold(
            floatingActionButton = {
                Fab {
                    onEvent(OnAddToDoClicked)
                }
            },
            isFloatingActionButtonDocked = true,
            bottomBar = {
                BottomAppBar {}
            }
        ) {
            Content(state, onEvent)
        }

        ModalBottomSheetLayout(
            sheetState = modalBottomSheetState,
            sheetContent = {
                AddToDo(onEvent)
            },
            sheetShape = RoundedCornerShape(4.dp),
            content = {}
        )
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
    state: TodoMainViewModel1,
    onEvent: (ToDoViewEvent) -> Unit = { _ -> }
) {
    when (state.isLoading) {
        true -> ListOfToDos(state, onEvent)
        else -> Loading()
    }.exaustive
}

@Composable
fun Loading() {
    Column(
        Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            Modifier.size(96.dp)
        )
    }
}
