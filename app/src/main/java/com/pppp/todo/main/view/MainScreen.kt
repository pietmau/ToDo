package com.pppp.todo.main.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomAppBar
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pppp.todo.drawer.Drawer
import com.pppp.todo.edittodo.EditBottomSheet
import com.pppp.todo.exaustive
import com.pppp.todo.main.viewmodel.AddToDo.Hidden
import com.pppp.todo.main.viewmodel.AddToDo.Showing
import com.pppp.todo.main.viewmodel.MainViewEvent.OnAddToDoClicked
import com.pppp.todo.main.viewmodel.MainViewEvent.OnEditToDoClicked
import com.pppp.todo.main.viewmodel.MainViewEvent.OnToDoCompleted
import com.pppp.todo.main.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun MainScreen() {
    val viewModel: MainViewModel = viewModel()
    val state by viewModel.states.collectAsState()
    val addToDoBottomSheetState = rememberModalBottomSheetState(
        ModalBottomSheetValue.Hidden
    )
    val editToDoBottomSheetState = rememberModalBottomSheetState(
        ModalBottomSheetValue.Hidden
    )
    LaunchedEffect(state.addToDo) {
        launch {
            when (state.addToDo) {
                Hidden -> addToDoBottomSheetState.hide()
                Showing -> addToDoBottomSheetState.show()
            }.exaustive
        }
    }
    MainScreenImpl(
        state = state,
        onEditToDoClicked = {
            viewModel(OnEditToDoClicked(it))
        },
        onAddToDoClicked = {
            viewModel(OnAddToDoClicked)
        },
        onToDoChecked = { id, checked ->
            viewModel(OnToDoCompleted(id, checked))
        }
    )
    AddBottomSheet(state.addToDo == Showing) {
        viewModel(it)
    }
    EditBottomSheet(state.itemBeingEdited, editToDoBottomSheetState) {
        viewModel(it)
    }
}

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
private fun MainScreenImpl(
    state: com.pppp.todo.main.viewmodel.MainViewState,
    onAddToDoClicked: () -> Unit,
    onToDoChecked: (String, Boolean) -> Unit = { _, _ -> },
    onEditToDoClicked: (String) -> Unit = {}
) {
    Scaffold(
        floatingActionButton = {
            Fab(onAddToDoClicked)
        },
        isFloatingActionButtonDocked = true,
        bottomBar = {
            BottomAppBar {}
        },
        drawerContent = { Drawer() }
    ) {
        Content(state, onToDoChecked, onEditToDoClicked)
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

@Composable
private fun Content(
    state: com.pppp.todo.main.viewmodel.MainViewState,
    onToDoChecked: (String, Boolean) -> Unit = { _, _ -> },
    onEditToDoClicked: (String) -> Unit = {}
) {
    when (state.isLoading) {
        true -> ListOfToDos(state, onToDoChecked, onEditToDoClicked)
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
