package com.pppp.todo.main.todo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ModalBottomSheetValue.Hidden
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pppp.todo.exaustive
import com.pppp.todo.main.MainViewModel
import com.pppp.todo.main.ToDoViewEvent.OnToDoAdded
import com.pppp.todo.main.ToDoViewEvent.OnToDoCompleted
import com.pppp.todo.main.TodoMainViewModel
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun MainScreen(mainViewModel: MainViewModel) {
    val state: TodoMainViewModel by mainViewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val modalBottomSheetState = rememberModalBottomSheetState(Hidden)
    Box {
        Scaffold(
            floatingActionButton = {
                Fab {
                    coroutineScope.launch {
                        if (modalBottomSheetState.isVisible) {
                            modalBottomSheetState.hide()
                        } else {
                            modalBottomSheetState.show()
                        }
                    }
                }
            },
            isFloatingActionButtonDocked = true,
            bottomBar = {
                BottomAppBar {}
            }
        ) {
            Content(state) { id, checked ->
                mainViewModel(OnToDoCompleted(id, checked))
            }
        }
        BottomSheet(modalBottomSheetState) {
            coroutineScope.launch {
                if (modalBottomSheetState.isVisible) {
                    modalBottomSheetState.hide()
                } else {
                    modalBottomSheetState.show()
                }
            }
            coroutineScope.launch {
                mainViewModel(OnToDoAdded(it))
            }
        }
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

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
private fun BottomSheet(
    modalBottomSheetState: ModalBottomSheetState,
    onToDoAdded: (String) -> Unit = {}
) {
    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetContent = {
            AddToDo(onToDoAdded)
        },
        sheetShape = RoundedCornerShape(4.dp),
        content = {}
    )
}

@Composable
private fun Content(
    state: TodoMainViewModel,
    onItemChecked: (String, Boolean) -> Unit = { _, _ -> }
) {
    when (state.isLoading) {
        true -> ListOfToDos(state, onItemChecked)
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
