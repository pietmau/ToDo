package com.pppp.todo.main.view

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@Composable
fun AppBar(
    onNavigationItemClick: suspend () -> Unit = {}
) {
    TopAppBar {
        val scope = rememberCoroutineScope()
        IconButton(
            onClick = {
                scope.launch {
                    onNavigationItemClick()
                }
            },
            content = {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = null
                )
            })
    }
}

