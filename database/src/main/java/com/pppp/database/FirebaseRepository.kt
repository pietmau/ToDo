package com.pppp.database

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.pppp.database.FirebaseRepository.Companion.EMPTY_STRING
import com.pppp.database.FirebaseRepository.Companion.STARRED
import com.pppp.database.FirebaseRepository.Companion.TITLE
import com.pppp.entities.ToDo
import com.pppp.usecases.Repository
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirebaseRepository(private val db: FirebaseFirestore) : Repository {

    override suspend fun getToDos(): List<ToDo> {
        return suspendCoroutine { continuation ->
            db.collection(TODOS).addSnapshotListener { value, error ->
                if (error != null) {
                    continuation.resumeWithException(error)
                } else {
                    continuation.resume(value.toToDoList())
                }
            }
        }
    }

    companion object {
        const val TODOS = "todos"
        const val TITLE = "title"
        const val STARRED = "starred"
        const val EMPTY_STRING = ""
    }
}

private fun QuerySnapshot?.toToDoList(): List<ToDo> =
    this?.documents?.map {
        ToDo(
            id = it.id,
            title = it.get(TITLE, String::class.java) ?: EMPTY_STRING,
            starred = it.getBoolean(STARRED) ?: false
        )
    } ?: emptyList()
