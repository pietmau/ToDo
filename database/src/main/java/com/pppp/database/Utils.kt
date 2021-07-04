package com.pppp.database

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.pppp.entities.ToDo

fun QuerySnapshot?.toToDoList(): List<ToDo> =
    this?.documents?.map { it.toToDo() }?.sortedWith(ToDoComparator) ?: emptyList()

fun DocumentSnapshot.toToDo() = ToDo(
    id = this.id,
    title = this.get(FirebaseRepository.TITLE, String::class.java)
        ?: FirebaseRepository.EMPTY_STRING,
    starred = this.getBoolean(FirebaseRepository.STARRED) ?: false,
    created = this.get(FirebaseRepository.CREATED, Long::class.java),
    completed = this.getBoolean(FirebaseRepository.COMPLETED),
    due = this.get(FirebaseRepository.DUE, Long::class.java),
)

private object ToDoComparator : Comparator<ToDo> {
    override fun compare(o1: ToDo, o2: ToDo): Int {
        if (o1.due == null && o2.due != null) {
            return 1
        }
        if (o1.due != null && o2.due == null) {
            return -1
        }
        if (o1.due == null && o2.due == null) {
            return 1
        }
        return o1.due!!.compareTo(o2.due!!)
    }
}