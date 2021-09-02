package com.pppp.todo.drawer

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch


@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun Drawer(
    currentRoute: String = "",
    closeDrawer: () -> Unit = {},
    onRouteChanged: (String) -> Unit = {},
    viewModel: DrawerViewModel = viewModel(),
    addListClicked: suspend () -> Unit = {}
) {
    val state: State<ViewState> = viewModel.states.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (spacer, divider, lists, addList) = createRefs()
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
                    bottom.linkTo(addList.top)
                    height = Dimension.fillToConstraints
                },
            items = state.value.lists,
            closeDrawer = closeDrawer
        )
        AddListRow(
            modifier = Modifier
                .constrainAs(addList) {
                    bottom.linkTo(parent.bottom)
                },
            onClick = {
                coroutineScope.launch {
                    addListClicked()
                }
            }
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
                isSelected = false,
                action = {
                    closeDrawer()
                }
            )
        }
    }
}

@Composable
private fun AddListRow(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val colors = MaterialTheme.colors
    Column(modifier.padding(start = 8.dp, top = 12.dp, end = 8.dp, bottom = 12.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onClick()
                },
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.width(16.dp))
            Icon(
                modifier = Modifier
                    .size(24.dp),
                imageVector = Icons.Outlined.Add,
                contentDescription = null,
                tint = colors.primary
            )
            Text(
                text = "Add list",
                color = colors.primary
            )
        }
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

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp)
            .clickable { },
        shape = MaterialTheme.shapes.small,
        color = backgroundColor,
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 8.dp),

            ) {
            Spacer(Modifier.width(16.dp))
            Icon(
                modifier = Modifier
                    .size(36.dp),
                imageVector = Icons.Outlined.List,
                contentDescription = null
            )
            Text(
                text = label,
                modifier = Modifier.alignBy()
            )
        }
    }
}

@Composable
@Preview
fun previewButton() {
    DrawerButton(label = "aaaa", isSelected = true, action = { /*TODO*/ })
}

