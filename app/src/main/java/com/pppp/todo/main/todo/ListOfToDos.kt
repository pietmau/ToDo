package com.pppp.todo.main.todo

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pppp.todo.main.ToDoViewEvent
import com.pppp.todo.main.TodoMainViewModel

@Composable
fun ListOfToDos(
    mainViewModel: TodoMainViewModel,
    onItemChecked: (String, Boolean) -> Unit = { _, _ -> }
) {
    LazyColumn(
        Modifier
            .fillMaxHeight()
            .padding(0.dp, 0.dp, 0.dp, 56.dp)
    ) {
        itemsIndexed(
            items = mainViewModel.todos,
            key = { index, item -> item.id }
        ) { index, item ->
            ToDoItem(
                toDo = item,
                onItemChecked = onItemChecked
            )
        }
    }
}
