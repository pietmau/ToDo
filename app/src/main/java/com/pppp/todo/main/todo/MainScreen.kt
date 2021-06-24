package com.pppp.todo.main.todo

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.BottomSheetValue.Collapsed
import androidx.compose.material.BottomSheetValue.Expanded
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
import com.pppp.todo.main.ToDoViewEvent
import com.pppp.todo.main.TodoMainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun MainScreen(mainViewModel: MainViewModel) {
    val state: TodoMainViewModel by mainViewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState(state),
        floatingActionButton = { Fab { onFabClickd(coroutineScope, mainViewModel) } },
        sheetContent = { AddToDo() }
    ) {
        ToDoScreen(state)
    }
}

private fun onFabClickd(
    coroutineScope: CoroutineScope,
    model: MainViewModel
) {
    coroutineScope.launch {
        model(ToDoViewEvent.OnFabClicked)
    }
}

@ExperimentalMaterialApi
@Composable
private fun bottomSheetScaffoldState(state: TodoMainViewModel) =
    rememberBottomSheetScaffoldState(
        bottomSheetState = if (state.addTodoViewModel == null) {
            BottomSheetState(Collapsed)
        } else {
            BottomSheetState(
                Expanded
            )
        }
    )

@Composable
fun AddToDo() {

}

@Composable
fun Fab(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        content = { Icon(imageVector = Icons.Outlined.Add, contentDescription = null) }
    )
}

@Composable
private fun ToDoScreen(state: TodoMainViewModel) {
    when (state.isLoading) {
        false -> ListOfToDos(state)
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
