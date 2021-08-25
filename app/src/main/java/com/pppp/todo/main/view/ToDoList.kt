package com.pppp.todo.main.view

import androidx.compose.foundation.layout.size
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pppp.todo.GenericViewModelWithOneOffEvents
import com.pppp.todo.exaustive
import com.pppp.todo.main.viewmodel.MainViewEvent
import com.pppp.todo.main.viewmodel.MainViewEvent.OnToDoCompleted
import com.pppp.todo.main.viewmodel.MainViewEvent.GetList
import com.pppp.todo.main.viewmodel.MainViewModel
import com.pppp.todo.main.viewmodel.MainViewState
import com.pppp.todo.main.viewmodel.OneOffEvent
import com.pppp.uielements.Loading

@Composable
fun Fab(onClick: () -> Unit) {
    FloatingActionButton(
        modifier = Modifier.size(48.dp),
        onClick = onClick,
        content = { Icon(imageVector = Icons.Outlined.Add, contentDescription = null) }
    )
}

@Composable
internal fun MainContent(
    listId: String = "",
    viewModel: GenericViewModelWithOneOffEvents<MainViewState, MainViewEvent, OneOffEvent> =
        viewModel<MainViewModel>(),
    onEditToDoClicked: (listId: String, itemId: String) -> Unit = { _, _ -> }
) {
    LaunchedEffect(listId) {
        viewModel(GetList(listId))
    }
    val state by viewModel.states.collectAsState()
    val onToDoChecked: (String, String, Boolean) -> Unit = { listId, itemId, checked ->
        viewModel(
            OnToDoCompleted(
                listId = listId,
                itemId = itemId,
                completed = checked
            )
        )
    }
    when (state.isLoading) {
        true -> ToDoList(state, onEditToDoClicked, onToDoChecked)
        else -> Loading()
    }.exaustive
}
