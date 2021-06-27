package com.pppp.todo.main.view

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pppp.todo.addtodo.AddTodoViewState
import com.pppp.todo.main.ToDoViewEvent
import com.pppp.todo.main.ToDoViewEvent.OnAddToDoClicked
import com.pppp.todo.main.ToDoViewEvent.OnToDoAdded
import com.pppp.todo.toDueDateText
import com.pppp.todo.toEpochMillis
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.datetimepicker
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun BottomSheet(
    modalBottomSheetState: ModalBottomSheetState,
    onEvent: (ToDoViewEvent) -> Unit
) {
    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetContent = {
            AddToDo(onEvent)
        },
        sheetShape = RoundedCornerShape(4.dp),
        content = {}
    )
    val coroutineScope = rememberCoroutineScope()
    BackHandler(modalBottomSheetState) {
        coroutineScope.launch {
           onEvent(OnAddToDoClicked)
        }
    }
}

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun AddToDo(
    onEvent: (ToDoViewEvent) -> Unit = {}
) {
    Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)) {
        AddToDoInputControl(onEvent)
    }
}

@ExperimentalComposeUiApi
@Composable
private fun AddToDoInputControl(onEvent: (ToDoViewEvent) -> Unit = {}) {
    val state = rememberSaveable { mutableStateOf(AddTodoViewState()) }

    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            val keyboardController = LocalSoftwareKeyboardController.current

            val onDone = {
                keyboardController?.hide()
                if (state.value.text.isEmpty()) {
                    state.value = state.value.copy(isError = true)
                } else {
                    onEvent(OnAddToDoClicked)
                    onEvent(OnToDoAdded(state.value.text, state.value.dueDate))
                    state.value = state.value.copy(text = "")
                }
            }
            TextField(
                modifier = Modifier.weight(1F),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { onDone() }),
                value = state.value.text,
                onValueChange = {
                    state.value = state.value.copy(text = it, isError = false)
                },
                isError = state.value.isError
            )
            DoneButton(onDone)
        }
        Row {
            Calendar(state)
        }
    }
}

@Composable
private fun Calendar(state: MutableState<AddTodoViewState>) {
    val dialog = remember { MaterialDialog() }
    dialog.build {
        datetimepicker {
            state.value = state.value.copy(
                dueDate = it.toEpochMillis()
            )
        }
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable {
                dialog.show()
            }
            .padding(all = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                imageVector = Icons.Outlined.DateRange,
                contentDescription = "",
            )
            Text(
                text = state.value.dueDate?.toDueDateText() ?: "Due",//TODO extract
                style = MaterialTheme.typography.caption
            )
        }
    }
}

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

@ExperimentalMaterialApi
@Composable
public fun BackHandler(modalBottomSheetState: ModalBottomSheetState, onBack: () -> Job) {
    val currentOnBack by rememberUpdatedState(onBack)
    val backCallback = remember {
        object : OnBackPressedCallback(modalBottomSheetState.isVisible) {
            override fun handleOnBackPressed() {
                currentOnBack()
            }
        }
    }
    backCallback.isEnabled = modalBottomSheetState.isVisible
    val backDispatcher =
        requireNotNull(LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher)
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, backDispatcher, modalBottomSheetState.isVisible) {
        backDispatcher.addCallback(lifecycleOwner, backCallback)
        onDispose {
            backCallback.remove()
        }
    }
}