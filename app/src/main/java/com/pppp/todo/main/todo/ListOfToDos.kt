package com.pppp.todo.main.todo

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pppp.todo.main.TodoMainViewModel

@Composable
fun ListOfToDos(state: TodoMainViewModel) {
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
