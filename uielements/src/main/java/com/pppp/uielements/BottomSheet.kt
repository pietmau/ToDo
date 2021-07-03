package com.pppp.uielements

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun BottomSheet(
    shouldShow: Boolean = false,
    onBackPressed: () -> Unit = {},
    content: @Composable() (ColumnScope.() -> Unit) = {}
) {
    val modalBottomSheetState: ModalBottomSheetState = rememberModalBottomSheetState(
        ModalBottomSheetValue.Hidden
    )
    LaunchedEffect(shouldShow) {
        if (shouldShow) {
            modalBottomSheetState.show()
        } else {
            modalBottomSheetState.hide()
        }
    }
    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetContent = content,
        sheetShape = RoundedCornerShape(4.dp),
        content = {}
    )
    val coroutineScope = rememberCoroutineScope()
    BackHandler(modalBottomSheetState.isVisible) {
        coroutineScope.launch {
            onBackPressed()
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun BackHandler(isVisible: Boolean = false, onBack: () -> Unit = {}) {
    val currentOnBack by rememberUpdatedState(onBack)
    val backCallback = remember {
        object : OnBackPressedCallback(isVisible) {
            override fun handleOnBackPressed() {
                currentOnBack()
            }
        }
    }
    backCallback.isEnabled = isVisible
    val backDispatcher =
        requireNotNull(LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher)
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, backDispatcher, isVisible) {
        backDispatcher.addCallback(lifecycleOwner, backCallback)
        onDispose {
            backCallback.remove()
        }
    }
}