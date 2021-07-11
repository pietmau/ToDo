package com.pppp.todo.main.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pppp.todo.main.viewmodel.MainViewEvent
import com.pppp.todo.main.viewmodel.ToDoViewModel
import com.pppp.todo.toDueDateText

@Composable
fun ToDoItem(
    toDo: ToDoViewModel = ToDoViewModel(id = "", title = "Title"),
    onToDoChecked: (String, Boolean) -> Unit = { _, _ -> },
    onEditToDoClicked: (String) -> Unit = {}
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
            .clickable { onEditToDoClicked(toDo.id) },
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
            ToDoCheckBox(toDo, onToDoChecked)
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
private fun ToDoCheckBox(
    toDo: ToDoViewModel,
    onToDoChecked: (String, Boolean) -> Unit = { _, _ -> }
) {
    var isChecked by remember { mutableStateOf(false) }
    Checkbox(checked = isChecked, onCheckedChange = {
        isChecked = it
        onToDoChecked(toDo.id, it)
    })
}

@Preview
@Composable
fun ToDoItemPreview() {
    ToDoItem(ToDoViewModel("", "Foo", true, due = 1624717282382)) { }
}