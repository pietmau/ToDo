package com.pppp.todo.main.todo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.pppp.entities.ToDo
import com.pppp.todo.main.ToDoViewModel

@Composable
fun ToDoItem(modifier: Modifier,toDo: ToDoViewModel, onItemChecked: ((String, Boolean) -> Unit)) {
    Surface(
        modifier = Modifier.padding(
            PaddingValues(
                start = 4.dp,
                end = 4.dp,
                top = 4.dp,
                bottom = 0.dp
            )
        ),
        elevation = 4.dp,
        shape = MaterialTheme.shapes.medium.copy(
            topStart = CornerSize(4.dp)
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
        ) {
            ToDoCheckBox(toDo, onItemChecked)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = toDo.title,
                modifier = Modifier.weight(1F, true)
            )
            Spacer(modifier = Modifier.width(8.dp))
            ToDoIcon(Modifier, toDo) {}
        }
    }
}

@Composable
private fun ToDoIcon(modifier: Modifier, toDo: ToDoViewModel, onItemChecked: (String) -> Unit) {
    var image by remember {
        mutableStateOf(toDo.getStarImage())
    }
    Icon(
        imageVector = image,
        contentDescription = null,
        modifier = modifier.clickable {
            onItemChecked(toDo.id)
            image = imageVector(image)
        }
    )
}

private fun imageVector(image: ImageVector) =
    if (image == Icons.Outlined.StarBorder) {
        Icons.Outlined.Star
    } else {
        Icons.Outlined.StarBorder
    }

private fun ToDoViewModel.getStarImage() =
    if (starred) {
        Icons.Outlined.Star
    } else {
        Icons.Outlined.StarBorder
    }

@Composable
private fun ToDoCheckBox(
    toDo: ToDoViewModel,
    onItemChecked: (String, Boolean) -> Unit
) {
    var isChecked by remember { mutableStateOf(false) }
    Checkbox(checked = isChecked, onCheckedChange = {
        isChecked = it
        onItemChecked(toDo.id, it)
    })
}

