package com.pppp.todo.addtodo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddTodoViewState(
    val isError: Boolean = false,
    val text: String = "",
    val dueDate: Long? = null
) : Parcelable