package com.pppp.todo.main.view

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pppp.todo.main.viewmodel.MainViewEvent
import com.pppp.todo.main.viewmodel.MainViewState

@Composable
fun ListOfToDos(
    mainViewState: MainViewState,
    onEvent: (MainViewEvent) -> Unit = { _ -> }
) {
    LazyColumn(
        Modifier
            .fillMaxHeight()
            .padding(0.dp, 0.dp, 0.dp, 56.dp)
    ) {
        itemsIndexed(
            items = mainViewState.todos,
            key = { index, item -> item.id }
        ) { index, item ->
            ToDoItem(
                toDo = item,
                onEvent = onEvent
            )
        }
    }
}
