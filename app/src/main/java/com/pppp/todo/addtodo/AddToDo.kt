package com.pppp.todo.main.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetValue.Hidden
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pppp.todo.R
import com.pppp.todo.addtodo.AddTodoViewModel
import com.pppp.todo.addtodo.Event
import com.pppp.todo.addtodo.OneOffEvent.AddToDo
import com.pppp.todo.addtodo.OneOffEvent.OnBackPressed
import com.pppp.todo.addtodo.ViewState
import com.pppp.todo.exaustive
import com.pppp.todo.main.viewmodel.MainViewEvent
import com.pppp.todo.toDueDateText
import com.pppp.todo.toLocalDateTime
import com.pppp.todo.toMillis
import com.pppp.uielements.BottomSheet
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.datetimepicker
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun AddBottomSheet(
    isVisible: Boolean = false,
    onEvent: (MainViewEvent) -> Unit = {},
) {
    val modalBottomSheetState = rememberModalBottomSheetState(initialValue = Hidden)
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(isVisible) {
        launch {
            when (isVisible) {
                true -> modalBottomSheetState.show()
                false -> {
                    modalBottomSheetState.hide()
                    keyboardController?.hide()
                }
            }.exaustive
        }
    }

    val viewmodel: AddTodoViewModel = viewModel()

    LaunchedEffect(onEvent) {
        viewmodel.oneOffEvents.collect {
            when (it) {
                is AddToDo -> onEvent(
                    MainViewEvent.OnToDoAdded(
                        title = it.title,
                        due = it.due
                    )
                )
                is OnBackPressed -> onEvent(MainViewEvent.OnCancel)
            }.exaustive
        }
    }
    BottomSheet(
        modalBottomSheetState = modalBottomSheetState,
        onBackPressed = {
           viewmodel(Event.OnBackPressed)
        },
    )
    {
        AddToDo(viewmodel.states.collectAsState()) {
            viewmodel(it)
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
            TextField(
                modifier = Modifier
                    .weight(1F)
                    .onKeyEvent {
                        if (it.key.keyCode == Key.Back.keyCode) {
                            onEvent(Event.OnBackPressed)
                        }
                        false
                    },
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
        }
        Row {
            Calendar(state.value.due) {
                onEvent(Event.OnTimeDataPicked(it))
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                modifier = Modifier.padding(end = 8.dp, top = 8.dp),
                onClick = {
                    onEvent(Event.OnBackPressed)
                }) {
                Text(text = stringResource(id = R.string.cancel))
            }
            Button(
                modifier = Modifier.padding(top = 8.dp),
                onClick = {
                    onEvent(Event.DoneClicked)
                }) {
                Text(text = stringResource(id = R.string.save))
            }
        }
    }
}

@Composable
internal fun Calendar(
    due: Long? = null,
    onTimeDataPicked: (Long) -> Unit = {}
) {
    val dialog = remember { MaterialDialog() }
    dialog.build {
        datetimepicker(
            initialDateTime = due.toLocalDateTime()
        ) {
            onTimeDataPicked(it.toMillis())
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
                text = due.toDueDateText() ?: "Due",
                style = MaterialTheme.typography.caption
            )
        }
    }
}

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Preview
@Composable
fun previewAddToDo() {
    AddToDo {}
}