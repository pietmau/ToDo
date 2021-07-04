package com.pppp.todo.main.view

import android.util.Log
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
import com.pppp.todo.exaustive
import com.pppp.todo.main.viewmodel.MainViewEvent
import com.pppp.todo.main.viewmodel.MainViewEvent.OnAddToDoClicked
import com.pppp.todo.main.viewmodel.MainViewModel
import com.pppp.todo.main.viewmodel.NavigationEvent
import com.pppp.uielements.fooLog
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import com.pppp.todo.main.viewmodel.MainViewState as TodoMainViewModel

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun MainScreen() {
    fooLog(text = "MainScreen")
    val navController = rememberNavController()
    val viewModel: MainViewModel = viewModel()
    val state by viewModel.states.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val modalBottomSheetState: ModalBottomSheetState = rememberModalBottomSheetState(
        ModalBottomSheetValue.Hidden
    )
    LaunchedEffect(Unit) {
        fooLog("LaunchedEffect")
        coroutineScope.launch {
            viewModel.navigationEvents.collect {
                fooLog("viewModel.navigationEvents.collect")
                when (it) {
                    NavigationEvent.Foo ->  modalBottomSheetState.show()
                }.exaustive
            }
        }
    }
    NavHost(navController = navController, startDestination = Navigation.NONE) {
        composable(Navigation.NONE) { /* NoOp */ }
    }
    MainScreenImpl(state) {
        viewModel.invoke(it)
    }
    AddBottomSheet(modalBottomSheetState = modalBottomSheetState, onBackPressed = {
        coroutineScope.launch {
            modalBottomSheetState.hide()
        }
    }) {
        viewModel.invoke(it)
    }
    //EditBottomSheet(mainViewModel.toDoBeingEdited, onEvent)
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
