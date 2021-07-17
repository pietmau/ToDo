package com.pppp.todo.drawer

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun Drawer(
    currentRoute: String = "",
    closeDrawer: () -> Unit = {},
) {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (spacer, divider, lists, addList) = createRefs()

        Spacer(
            Modifier
                .height(24.dp)
                .constrainAs(spacer) {
                    top.linkTo(parent.top)
                }
        )
        Divider(
            modifier = Modifier.constrainAs(divider) {
                top.linkTo(spacer.bottom)
            },
            color = MaterialTheme.colors.onSurface.copy(alpha = .2f)
        )
        Items(
            modifier = Modifier
                .constrainAs(lists) {
                    top.linkTo(divider.bottom)
                }
                .fillMaxHeight(),
            items = state.value.lists,
            closeDrawer = closeDrawer
        )
        AddList(
            modifier = Modifier
                .constrainAs(addList) {
                    bottom.linkTo(parent.bottom)
                },
            onClick = {}
        )
    }
}

@Composable
private fun Items(
    modifier: Modifier = Modifier,
    items: List<ViewState.ToDoList> = emptyList(),
    closeDrawer: () -> Unit = {},
) {
    LazyColumn(modifier) {
        items(items) {
            DrawerButton(
                label = it.name,
                isSelected = true,
                action = {
                    closeDrawer()
                }
            )
        }
    }
}

@Composable
private fun AddList(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Add list",
            style = MaterialTheme.typography.body2,
        )
        Image(
            modifier = Modifier
                .padding(8.dp)
                .size(32.dp),
            imageVector = Icons.Outlined.Add,
            contentDescription = null
        )
    }
}

@Composable
private fun DrawerButton(
    label: String,
    isSelected: Boolean,
    action: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colors
    val imageAlpha = if (isSelected) {
        1f
    } else {
        0.6f
    }
    val textIconColor = if (isSelected) {
        colors.primary
    } else {
        colors.onSurface.copy(alpha = 0.6f)
    }
    val backgroundColor = if (isSelected) {
        colors.primary.copy(alpha = 0.12f)
    } else {
        Color.Transparent
    }

    val surfaceModifier = modifier
        .padding(start = 8.dp, top = 8.dp, end = 8.dp)
        .fillMaxWidth()
    Surface(
        modifier = surfaceModifier,
        color = backgroundColor,
        shape = MaterialTheme.shapes.small
    ) {
        TextButton(
            onClick = action,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(Modifier.width(16.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.body2,
                    color = textIconColor
                )
            }
        }
    }
}

@Preview
@Composable
fun DrawerPreview() {
    val state =
        remember { mutableStateOf(ViewState(lists = listOf(ViewState.ToDoList(name = "Foo")))) }
    Drawer(state = state)
}