package com.pppp.todo.main.todo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pppp.todo.exaustive
import com.pppp.todo.main.MainViewModel
import com.pppp.todo.main.TodoMainViewModel

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val state: TodoMainViewModel by viewModel.uiState.collectAsState()
    Scaffold(
        floatingActionButton = { Fab() }
    ) {
        ToDoScreen(state)
    }
}

@Composable
fun Fab() {
    FloatingActionButton(
        onClick = {},
        content = { Icon(imageVector = Icons.Outlined.Add, contentDescription = null) }
    )
}

@Composable
private fun ToDoScreen(state: TodoMainViewModel) {
    when (state) {
        is TodoMainViewModel.Data -> ListOfToDos(state)
        is TodoMainViewModel.Loading -> Loading()
    }.exaustive
}

@Composable
fun ListOfToDos(state: TodoMainViewModel.Data) {
    LazyColumn(Modifier.fillMaxHeight()) {
        itemsIndexed(state.todos) { index, item ->
            val bottomPadding = if (index == state.todos.size - 1) 4.dp else 0.dp
            ToDoItem(
                modifier = Modifier.padding(
                    PaddingValues(
                        start = 4.dp,
                        end = 4.dp,
                        top = 4.dp,
                        bottom = bottomPadding
                    )
                ),
                toDo = item
            ) { id, selected -> }
        }
    }
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

@Preview
@Composable
fun LoadingPreview() {
    Loading()
}

@Preview
@Composable
fun Main() {
    Scaffold {
        ToDoScreen(TodoMainViewModel.Loading)
    }
}