package com.pppp.todo.main.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pppp.todo.addtodo.AddTodoViewModel
import com.pppp.todo.addtodo.Event
import com.pppp.todo.addtodo.OneOffEvent
import com.pppp.todo.addtodo.ViewState
import com.pppp.todo.exaustive
import com.pppp.todo.fooLog
import com.pppp.todo.main.viewmodel.MainViewEvent
import com.pppp.todo.main.viewmodel.MainViewEvent.OnToDoAdded
import com.pppp.uielements.BottomSheet
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun AddBottomSheet(
    modalBottomSheetState: ModalBottomSheetState,
    onBackPressed: () -> Unit = {},
    onEvent: (MainViewEvent) -> Unit = {},
) {
    val viewmodel: AddTodoViewModel = viewModel()
    val viewState = viewmodel.states.collectAsState()
    val scope = rememberCoroutineScope()
    LaunchedEffect(viewmodel) {
        scope.launch {
            viewmodel.navigationEvents.collect {
                when (it) {
                    is OneOffEvent.AddToDo -> {
                        onEvent(OnToDoAdded(title = it.title, due = it.due))
                        //modalBottomSheetState.hide()
                    }
                }.exaustive
            }
        }
    }
    BottomSheet(
        modalBottomSheetState = modalBottomSheetState,
        onBackPressed = {
            fooLog(text = "AddBottomSheet onBackPressed")
            scope.launch {
                fooLog(text = "modalBottomSheetState.hide()")
                onBackPressed()
                //modalBottomSheetState.hide()
            }
        },
    )
    {
        AddToDo(viewState) {
            viewmodel.invoke(it)
        }
    }
}

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun AddToDo(
    viewState: State<ViewState> = remember {
        mutableStateOf(ViewState())
    },
    onEvent: (Event) -> Unit = {}
) {
    Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)) {
        AddToDoInputControl(viewState, onEvent)
    }
}

@ExperimentalComposeUiApi
@Composable
fun AddToDoInputControl(
    state: State<ViewState> = remember {
        mutableStateOf(ViewState())
    },
    onEvent: (Event) -> Unit = {}
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            val keyboardController = LocalSoftwareKeyboardController.current
            TextField(
                modifier = Modifier.weight(1F),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { onEvent(Event.DoneClicked) }),
                value = state.value.title,
                onValueChange = {
                    onEvent(Event.OnTitleChanged(it))
                },
                isError = state.value.isError
            )
            DoneButton {
                onEvent(Event.DoneClicked)
            }
        }
        Row {
            //Calendar(state, onEvent) TODO
        }
    }
}

//@Composable
//private fun Calendar(
//    state: MutableState<AddTodoViewModel.ViewState> = remember {
//        mutableStateOf(AddTodoViewModel.ViewState())
//    },
//    onEvent: (AddTodoViewModel.Event) -> Unit = {}
//) {
//    val dialog = remember { MaterialDialog() }
//    dialog.build {
//        datetimepicker {
//            state.value = state.value.copy(
//                dueDate = it.toEpochMillis()
//            )
//        }
//    }
//    Row(
//        verticalAlignment = Alignment.CenterVertically,
//        modifier = Modifier
//            .clickable {
//                dialog.show()
//            }
//            .padding(all = 8.dp)
//    ) {
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//        ) {
//            Image(
//                imageVector = Icons.Outlined.DateRange,
//                contentDescription = "",
//            )
//            Text(
//                text = state.value.dueDate?.toDueDateText() ?: "Due",//TODO extract
//                style = MaterialTheme.typography.caption
//            )
//        }
//    }
//}

@Composable
private fun DoneButton(onDone: () -> Unit) {
    Surface(
        modifier = Modifier
            .padding(8.dp, 0.dp, 0.dp, 0.dp)
            .size(32.dp),
        shape = CircleShape,
        color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f)
    ) {
        Image(
            imageVector = Icons.Filled.ArrowUpward,
            contentDescription = "",
            modifier = Modifier.clickable {
                onDone()
            })
    }
}

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Preview
@Composable
fun previewAddToDo() {
    AddToDo {}
}