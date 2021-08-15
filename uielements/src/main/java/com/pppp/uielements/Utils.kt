package com.pppp.uielements

import androidx.compose.material.ScaffoldState
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


suspend fun ScaffoldState.toggleDrawer() {
    if (drawerState.isOpen) {
        drawerState.close()
    } else {
        drawerState.open()
    }
}