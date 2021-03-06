package com.pppp.todo

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

fun Long?.toLocalDateTime(): LocalDateTime =
    this?.let { LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneId.systemDefault()) }
        ?: LocalDateTime.now()

fun Long.toDueDateNotificationText() =
    SimpleDateFormat("EEE, dd MMMM HH:mm", Locale.getDefault()).format(Date(this))


