package com.pppp.todo

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

val Any?.exaustive
    get() = Unit

interface Consumer<T> {
    operator fun invoke(t: T)
}

fun Long.toDueDateText() =
    SimpleDateFormat("dd MMM", Locale.getDefault()).format(Date(this))

fun LocalDateTime.toEpochMillis() = this.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

fun Long.toDueDateNotificationText() =
    SimpleDateFormat("EEE, dd MMMM HH:mm", Locale.getDefault()).format(Date(this))