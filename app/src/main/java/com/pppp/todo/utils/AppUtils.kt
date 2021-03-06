package com.pppp.todo.utils

import com.pppp.entities.ToDo
import com.pppp.todo.main.viewmodel.ToDoViewModel
import com.pppp.uielements.toMillis
import java.time.LocalDateTime

fun ToDo.toDoViewModel() = ToDoViewModel(
        listId = listId,
        id = id!!,
        title = title,
        starred = starred,
        due = due
)

fun Long.calculateDelay() = this - LocalDateTime.now().toMillis()

