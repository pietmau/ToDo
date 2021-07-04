package com.pppp.todo

import com.pppp.entities.ToDo
import com.pppp.todo.main.viewmodel.ToDoViewModel
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

val Any?.exaustive
    get() = Unit

interface Consumer<T> {
    operator fun invoke(t: T): Unit?
}

fun Long.toDueDateText() =
    SimpleDateFormat("dd MMM", Locale.getDefault()).format(Date(this))

fun LocalDateTime.toMillis() = this.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

fun Long.toDueDateNotificationText() =
    SimpleDateFormat("EEE, dd MMMM HH:mm", Locale.getDefault()).format(Date(this))

fun ToDo.toDoViewModel() = ToDoViewModel(
    id = id!!,
    title = title,
    starred = starred,
    due = due
)

fun Long.calculateDelay() = this - LocalDateTime.now().toMillis()

