package com.pppp.todo.drawer

import com.pppp.entities.ToDoList
import javax.inject.Inject

class DrawerMapper @Inject constructor() : @JvmSuppressWildcards (List<ToDoList>) -> ViewState {

    override fun invoke(list: List<ToDoList>): ViewState =
            ViewState(lists = list.map { it.toModel() })

    private fun ToDoList.toModel(): ViewState.ToDoList =
            ViewState.ToDoList(
                    id = id,
                    name = name,
                    created = created,
                    modified = modified,
                    archived = archived,
                    deleted = deleted,
                    priority = priority
            )
}

