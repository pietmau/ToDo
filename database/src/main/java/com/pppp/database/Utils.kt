package com.pppp.database

import com.pppp.entities.ToDo

internal object ToDoComparator : Comparator<ToDo> {
    override fun compare(o1: ToDo, o2: ToDo): Int {
        if (o1.due == null && o2.due != null) {
            return 1
        }
        if (o1.due != null && o2.due == null) {
            return -1
        }
        if (o1.due == null && o2.due == null) {
            return 0
        }
        return o1.due!!.compareTo(o2.due!!)
    }
}