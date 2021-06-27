package com.pppp.database

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.pppp.entities.ToDo

fun QuerySnapshot?.toToDoList(): List<ToDo> =
    this?.documents?.map { it.toToDo() }?.sortedBy { it.due } ?: emptyList()

fun DocumentSnapshot.toToDo() = ToDo(
    id = this.id,
    title = this.get(FirebaseRepository.TITLE, String::class.java)
        ?: FirebaseRepository.EMPTY_STRING,
    starred = this.getBoolean(FirebaseRepository.STARRED) ?: false,
    created = this.get(FirebaseRepository.CREATED, Long::class.java),
    completed = this.getBoolean(FirebaseRepository.COMPLETED),
    due = this.get(FirebaseRepository.DUE, Long::class.java),
)
