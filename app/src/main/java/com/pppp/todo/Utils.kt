package com.pppp.todo

import java.text.SimpleDateFormat
import java.util.*

val Any?.exaustive
    get() = Unit

interface Consumer<T> {
    operator fun invoke(t: T)
}

fun Long.toDueDateText() =
    SimpleDateFormat("dd MMM", Locale.getDefault()).format(Date(this))