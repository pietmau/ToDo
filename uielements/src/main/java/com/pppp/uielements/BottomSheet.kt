package com.pppp.uielements

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
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
    val coroutineScope = rememberCoroutineScope()
    fooLog("modalBottomSheetState " + modalBottomSheetState.currentValue)
    BackHandler(modalBottomSheetState.currentValue == ModalBottomSheetValue.Expanded) {
        coroutineScope.launch {
            fooLog( "BottomSheet onBackPressed")
            onBackPressed()
        }
    }
}