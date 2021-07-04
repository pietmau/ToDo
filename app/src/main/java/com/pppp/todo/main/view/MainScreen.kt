package com.pppp.todo.main.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pppp.todo.Navigation
import com.pppp.todo.edittodo.EditBottomSheet
import com.pppp.todo.exaustive
import com.pppp.todo.main.viewmodel.MainViewEvent
import com.pppp.todo.main.viewmodel.MainViewEvent.OnAddToDoClicked
import com.pppp.todo.main.viewmodel.MainViewModel
import com.pppp.todo.main.viewmodel.OneOffEvent
import com.pppp.todo.main.viewmodel.OneOffEvent.CloseAddToDoModal
import com.pppp.todo.main.viewmodel.OneOffEvent.OpenAddToDoModal
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import com.pppp.todo.main.viewmodel.MainViewState as TodoMainViewModel

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val viewModel: MainViewModel = viewModel()
    val state by viewModel.states.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val addToDoBottomSheetState: ModalBottomSheetState = rememberModalBottomSheetState(
        ModalBottomSheetValue.Hidden
    )
    val editToDoBottomSheetState: ModalBottomSheetState = rememberModalBottomSheetState(
        ModalBottomSheetValue.Hidden
    )
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            viewModel.oneOffEvents.collect {
                when (it) {
                    is OpenAddToDoModal -> addToDoBottomSheetState.show()
                    is CloseAddToDoModal -> addToDoBottomSheetState.hide()
                    is OneOffEvent.OpenEditModal -> TODO()
                }.exaustive
            }
        }
    }
    NavHost(navController, Navigation.NONE) {
        composable(Navigation.NONE) { /* NoOp */ }
    }
    MainScreenImpl(state) {
        viewModel.invoke(it)
    }
    AddBottomSheet(addToDoBottomSheetState, {
        coroutineScope.launch {
            addToDoBottomSheetState.hide()
        }
    }) {
        viewModel.invoke(it)
    }
    EditBottomSheet(editToDoBottomSheetState) {
        viewModel.invoke(it)
    }
}

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
private fun MainScreenImpl(mainViewModel: TodoMainViewModel, onEvent: (MainViewEvent) -> Unit) {
    Box {
        Scaffold(
            floatingActionButton = {
                Fab {
                    onEvent(OnAddToDoClicked)
                }
            },
            isFloatingActionButtonDocked = true,
            bottomBar = {
                BottomAppBar {}
            }
        ) {
            Content(mainViewModel, onEvent)
        }
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
    state: TodoMainViewModel,
    onEvent: (MainViewEvent) -> Unit = { _ -> }
) {
    when (state.isLoading) {
        true -> ListOfToDos(state, onEvent)
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
