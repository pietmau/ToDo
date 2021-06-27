package com.pppp.todo.main.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pppp.todo.main.ToDoViewEvent
import com.pppp.todo.main.ToDoViewEvent.OnEditToDoClicked
import com.pppp.todo.main.ToDoViewEvent.OnToDoCompleted
import com.pppp.todo.main.ToDoViewModel
import com.pppp.todo.toDueDateText

@Composable
fun ToDoItem(
    toDo: ToDoViewModel = ToDoViewModel(id = "", title = "Title"),
    onEvent: (ToDoViewEvent) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .padding(
                PaddingValues(
                    start = 4.dp,
                    end = 4.dp,
                    top = 4.dp,
                    bottom = 0.dp
                )
            )
            .clickable { onEvent(OnEditToDoClicked(toDo.id)) },
        elevation = 4.dp,
        shape = MaterialTheme.shapes.medium.copy(
            topStart = CornerSize(4.dp)
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(8.dp)
                .height(48.dp)
                .fillMaxWidth(),
        ) {
            ToDoCheckBox(toDo, onEvent)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = toDo.title,
                modifier = Modifier.weight(1F, true)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Due(toDo)
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@Composable
fun Due(toDo: ToDoViewModel) {
    toDo.due ?: return
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            modifier = Modifier.size(16.dp),
            imageVector = Icons.Outlined.DateRange,
            contentDescription = ""
        )
        Spacer(modifier = Modifier.width(2.dp))
        Text(
            text = "Due ${toDo.due.toDueDateText()}", //TODO
            style = MaterialTheme.typography.caption
        )
    }
}

@Composable
private fun Star(modifier: Modifier, toDo: ToDoViewModel) {
    var image by remember {
        mutableStateOf(toDo.getStarImage())
    }
    Icon(
        imageVector = image,
        contentDescription = null,
        modifier = modifier.clickable {
            image = getImage(image)
        }
    )
}

private fun getImage(image: ImageVector) =
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
private fun ToDoCheckBox(toDo: ToDoViewModel, onEvent: (ToDoViewEvent) -> Unit) {
    var isChecked by remember { mutableStateOf(false) }
    Checkbox(checked = isChecked, onCheckedChange = {
        isChecked = it
        onEvent(OnToDoCompleted(toDo.id, it))
    })
}

@Preview
@Composable
fun ToDoItemPreview() {
    ToDoItem(ToDoViewModel("", "Foo", true, due = 1624717282382)) { }
}