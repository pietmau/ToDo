package com.pppp.todo.main.todo

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pppp.todo.exaustive
import com.pppp.todo.main.MainViewModel
import com.pppp.todo.main.TodoMainViewModel
import kotlinx.coroutines.launch
import androidx.compose.material.ModalBottomSheetValue.Hidden
import kotlinx.coroutines.CoroutineScope

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
                    toggleBottomSheet(coroutineScope, modalBottomSheetState)
                }
            },
            isFloatingActionButtonDocked = true,
            bottomBar = {
                BottomAppBar {}
            }
        ) {
            Content(state)
        }
        BottomSheetLayout(modalBottomSheetState)
    }
}

@ExperimentalMaterialApi
private fun toggleBottomSheet(
    coroutineScope: CoroutineScope,
    modalBottomSheetState: ModalBottomSheetState
) {
    coroutineScope.launch {
        if (modalBottomSheetState.isVisible) {
            modalBottomSheetState.hide()
        } else {
            modalBottomSheetState.show()
        }
    }
}

@Composable
fun AddToDo() {
    Text(text = "dddd")
}

@Composable
fun Fab(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        content = { Icon(imageVector = Icons.Outlined.Add, contentDescription = null) }
    )
}

@ExperimentalMaterialApi
@Composable
private fun BottomSheetLayout(
    modalBottomSheetState: ModalBottomSheetState
) {

    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetContent = {
            AddToDo()
        },
        content = {}
    )
}

@Composable
private fun Content(state: TodoMainViewModel) {
    when (state.isLoading) {
        true -> ListOfToDos(state)
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
