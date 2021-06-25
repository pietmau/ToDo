package com.pppp.database

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import com.pppp.database.FirebaseRepository.Companion.EMPTY_STRING
import com.pppp.database.FirebaseRepository.Companion.STARRED
import com.pppp.database.FirebaseRepository.Companion.TITLE
import com.pppp.entities.ToDo
import com.pppp.usecases.Repository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FirebaseRepository(
    private val db: FirebaseFirestore
) : Repository {

    override suspend fun getToDos(): Flow<List<ToDo>> =
        callbackFlow {
            val registration: ListenerRegistration =
                db.collection(TODOS).addSnapshotListener { value, error ->
                    if (error != null) {
                        cancel(
                            message = "error fetching collection data",
                            cause = error
                        )
                        return@addSnapshotListener
                    } else {
                        trySend(value.toToDoList())
                    }
                }
            awaitClose {
                registration.remove()
            }
        }

    override suspend fun addToDo(params: String) {
        db.collection(TODOS).add(ToDo(title = params))
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
