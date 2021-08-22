package com.pppp.uielements

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.ModalBottomSheetValue.Hidden
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch


@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun BottomSheet(
    isExpanded: Boolean = false,
    onDismissed: () -> Unit = {},
    onConfirmClicked: () -> Unit = {},
    onCancelClicked: () -> Unit = {},
    sheetContent: @Composable () -> Unit,
    optionalDialogControls: @Composable () -> Unit = {},
    title: String? = null
) {
    val modalBottomSheetState: ModalBottomSheetState =
        rememberModalBottomSheetState(initialValue = Hidden)
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(isExpanded) {
        if (isExpanded) {
            modalBottomSheetState.show()
        } else {
            modalBottomSheetState.hide()
            keyboardController?.hide()
        }
    }
    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetContent = {
            Column(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 8.dp
                ),
                content = {
                    title?.let {
                        Text(
                            text = it,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                    SheetContent(
                        onConfirmClicked = onConfirmClicked,
                        onCancelClicked = onCancelClicked,
                        content = sheetContent,
                        optionalDialogControls = optionalDialogControls
                    )
                }
            )
        },
        sheetShape = RoundedCornerShape(4.dp),
        content = {}
    )
    LaunchedEffect(modalBottomSheetState.currentValue) {
        if (modalBottomSheetState.currentValue == Hidden) {
            onDismissed()
        }
    }
    val coroutineScope = rememberCoroutineScope()
    BackHandler(modalBottomSheetState.currentValue == ModalBottomSheetValue.Expanded) {
        coroutineScope.launch {
            onDismissed()
        }
    }
}

@ExperimentalComposeUiApi
@Composable
private fun SheetContent(
    onConfirmClicked: () -> Unit = {},
    onCancelClicked: () -> Unit = {},
    content: @Composable () -> Unit = {},
    optionalDialogControls: @Composable () -> Unit = {}
) {
    Column {
        content()
        DialogControls(
            onConfirmClicked = onConfirmClicked,
            onCancelClicked = onCancelClicked,
            optionalControls = optionalDialogControls
        )
    }
}
