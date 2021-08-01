package com.pppp.uielements

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

fun Long?.toLocalDateTime(): LocalDateTime =
    this?.let { LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneId.systemDefault()) }
        ?: LocalDateTime.now()

fun LocalDateTime.toMillis() = this.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

fun Long?.toDueDateText(): String? =
    if (this != null && this > 0) {
        SimpleDateFormat("dd MMM", Locale.getDefault()).format(Date(this))
    } else {
        "Due"
    }