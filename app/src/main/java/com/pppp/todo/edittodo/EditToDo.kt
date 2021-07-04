package com.pppp.todo.edittodo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pppp.todo.R
import com.pppp.todo.main.viewmodel.MainViewEvent
import com.pppp.todo.main.viewmodel.MainViewEvent.OnCancel
import com.pppp.uielements.BottomSheet
import com.pppp.uielements.ClosableErrorTextField
import com.pppp.todo.edittodo.EditTodoViewEvent.Init
import com.pppp.uielements.fooLog

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun EditBottomSheet(
    item: String? = null,
    modalBottomSheetState: ModalBottomSheetState = rememberModalBottomSheetState(
        ModalBottomSheetValue.Hidden
    ),
    onEvent: (MainViewEvent) -> Unit = {}
) {
    val editViewModel = viewModel<EditViewModel>()
    LaunchedEffect(item) {
        editViewModel.invoke(Init(item))
    }
    val state by editViewModel.states.collectAsState()
    LaunchedEffect(state.isVisible) {
        if (state.isVisible) {
            modalBottomSheetState.show()
        } else {
            modalBottomSheetState.hide()
        }
    }

    BottomSheet(
        onBackPressed = {
            fooLog("onCancel")
        },
        content = {
            Content(
                state = state,
                onCancel = {
                    fooLog("onCancel")
                }
            )
        },
        modalBottomSheetState = modalBottomSheetState
    )
}

@ExperimentalComposeUiApi
@Composable
fun Content(
    state: EditTodoViewState = EditTodoViewState(),
    onDone: () -> Unit = {},
    onCancel: () -> Unit = {}
) {
    Column(
        modifier = Modifier.padding(
            start = 16.dp,
            end = 16.dp,
            top = 16.dp,
            bottom = 8.dp
        )
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                ClosableErrorTextField(
                    modifier = Modifier.weight(1F),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    onDone = { onDone() },
                    value = state.text,
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                modifier = Modifier.padding(end = 8.dp, top = 8.dp),
                onClick = {
                    onCancel()
                }) {
                Text(text = stringResource(id = R.string.cancel))
            }
            Button(
                modifier = Modifier.padding(top = 8.dp),
                onClick = {}) {
                Text(text = stringResource(id = R.string.save))
            }
        }
    }
}

@Composable
@Preview
@ExperimentalComposeUiApi
private fun ContentPtreview(onEvent: (MainViewEvent) -> Unit = {}) {
    Content()
}




