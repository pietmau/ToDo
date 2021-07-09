package com.pppp.uielements

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun BottomSheet(
    modalBottomSheetState: ModalBottomSheetState,
    onBackPressed: () -> Unit = {},
    content: @Composable() (ColumnScope.() -> Unit) = {}
) {
    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetContent = content,
        sheetShape = RoundedCornerShape(4.dp),
        content = {}
    )
    LaunchedEffect(modalBottomSheetState.currentValue) {
        if (modalBottomSheetState.currentValue == ModalBottomSheetValue.Hidden) {
            onBackPressed()
        }
    }
    val coroutineScope = rememberCoroutineScope()
    BackHandler(false) {
        coroutineScope.launch {
            onBackPressed()
        }
    }
}