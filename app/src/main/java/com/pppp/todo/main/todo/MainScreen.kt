package com.pppp.todo.main.todo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pppp.entities.ToDo
import com.pppp.todo.main.MainViewModel
import com.pppp.todo.main.TodoMainViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import com.pppp.todo.main.ToDoViewModel


@Composable
fun MainScreen(viewModel: MainViewModel) {
    val state: TodoMainViewModel by viewModel.uiState.collectAsState()
    Scaffold {
        ToDoScreen(state)
    }
}

@Composable
private fun ToDoScreen(state: TodoMainViewModel) {
    when (state) {
        is TodoMainViewModel.Data -> Data(state)
        is TodoMainViewModel.Loading -> Loading()
    }.let { }
}

@Composable
fun Data(state: TodoMainViewModel.Data) {
    LazyColumn {
        items(state.todos) {
            ToDoItem(toDo = it) { id, selected -> }
        }
    }
}

@Composable
fun Loading() {
    CircularProgressIndicator()
}

@Preview
@Composable
fun LoadingPreview() {
    Loading()
}


