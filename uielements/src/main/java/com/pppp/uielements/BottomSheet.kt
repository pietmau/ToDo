package com.pppp.uielements

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.ModalBottomSheetValue.Hidden
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

interface BottomSheet {

    companion object {

        @ExperimentalComposeUiApi
        @ExperimentalMaterialApi
        @Composable
        fun Content(
            isExpanded: Boolean = false,
            onBackPressed: () -> Unit = {},
            content: @Composable() (ColumnScope.() -> Unit) = {}
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
                sheetContent = content,
                sheetShape = RoundedCornerShape(4.dp),
                content = {}
            )
            LaunchedEffect(modalBottomSheetState.currentValue) {
                if (modalBottomSheetState.currentValue == Hidden) {
                    onBackPressed()
                }
            }
            val coroutineScope = rememberCoroutineScope()
            BackHandler(modalBottomSheetState.currentValue == ModalBottomSheetValue.Expanded) {
                coroutineScope.launch {
                    onBackPressed()
                }
            }
        }
    }
}