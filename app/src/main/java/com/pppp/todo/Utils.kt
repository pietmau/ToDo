package com.pppp.todo

import com.pppp.entities.ToDo
import com.pppp.todo.main.viewmodel.ToDoViewModel
import com.pppp.uielements.toMillis
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

fun ToDo.toDoViewModel() = ToDoViewModel(
        listId = listId,
        id = id!!,
        title = title,
        starred = starred,
        due = due
)

fun Long.calculateDelay() = this - LocalDateTime.now().toMillis()

