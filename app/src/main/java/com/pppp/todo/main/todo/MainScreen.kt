package com.pppp.todo.main.todo

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pppp.todo.exaustive
import com.pppp.todo.main.MainViewModel
import com.pppp.todo.main.TodoMainViewModel

@ExperimentalMaterialApi
@Composable
fun MainScreen(mainViewModel: MainViewModel) {
    val state: TodoMainViewModel by mainViewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        floatingActionButton = { Fab {} }
    ) {
        BottomSheetLayout(state)
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
private fun BottomSheetLayout(state: TodoMainViewModel) {
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Expanded)
    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = { Text(text = "Foo") },
        content = {
            when (state.isLoading) {
                true -> ListOfToDos(state)
                else -> Loading()
            }.exaustive
        }
    )
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
