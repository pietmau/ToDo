package com.pppp.todo

import com.pppp.entities.ToDo
import com.pppp.todo.main.viewmodel.ToDoViewModel
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

val Any?.exaustive
    get() = Unit

interface Consumer<T> {
    operator fun invoke(t: T): Unit?
}

fun Long?.toDueDateText(): String? =
    if (this != null && this > 0) {
        SimpleDateFormat("dd MMM", Locale.getDefault()).format(Date(this))
    } else {
        "Due"
    }

fun LocalDateTime.toMillis() = this.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

fun Long?.toLocalDateTime(): LocalDateTime =
    this?.let { LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneId.systemDefault()) } ?: LocalDateTime.now()

fun Long.toDueDateNotificationText() =
    SimpleDateFormat("EEE, dd MMMM HH:mm", Locale.getDefault()).format(Date(this))

fun ToDo.toDoViewModel() = ToDoViewModel(
    id = id!!,
    title = title,
    starred = starred,
    due = due
)

fun Long.calculateDelay() = this - LocalDateTime.now().toMillis()

