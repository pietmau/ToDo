package com.pppp.database

import com.google.firebase.firestore.*
import com.pppp.entities.ToDo
import com.pppp.usecases.ToDosRepository
import com.pppp.usecases.ToDosRepository.Companion.COMPLETED
import com.pppp.usecases.ToDosRepository.Companion.CREATED
import com.pppp.usecases.ToDosRepository.Companion.DUE
import com.pppp.usecases.ToDosRepository.Companion.EMPTY_STRING
import com.pppp.usecases.ToDosRepository.Companion.ID
import com.pppp.usecases.ToDosRepository.Companion.STARRED
import com.pppp.usecases.ToDosRepository.Companion.TITLE
import com.pppp.usecases.ToDosRepository.Companion.TODOS
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseToDosRepository(private val db: FirebaseFirestore) : ToDosRepository {

    @ExperimentalCoroutinesApi
    override suspend fun getToDo(): Flow<List<ToDo>> =
        callbackFlow {
            val registration = db.collection(TODOS)
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        close(error)
                    } else {
                        trySend(value.toToDoList())
                    }
                }
            awaitClose {
                registration.remove()
            }
        }

    @ExperimentalCoroutinesApi
    override fun getToDo(id: String): Flow<List<ToDo>> =
        callbackFlow {
            db.collection(TODOS).document(id).get()
                .addOnSuccessListener { value: DocumentSnapshot ->
                    trySend(listOfNotNull(value.toToDo()))
                }
            awaitClose {
                /* NoOp */
            }
        }

    override suspend fun addToDo(params: ToDosRepository.Params.Add): String =
        suspendCoroutine { continuation ->
            db.collection(TODOS).add(
                ToDo(
                    title = params.title,
                    created = System.currentTimeMillis(),
                    due = params.due
                )
            ).addOnSuccessListener { document ->
                db.runTransaction { transaction ->
                    transaction.update(document, mapOf(ID to document.id))
                }
                continuation.resume(document.id)
            }
        }

    override suspend fun edit(id: String, values: Map<String, Any?>): String =
        suspendCoroutine { continuation ->
            db.collection(TODOS).document(id).update(values).addOnSuccessListener {
                continuation.resume(id)
            }
        }

    private fun QuerySnapshot?.toToDoList(): List<ToDo> =
        this?.documents?.map { it.toToDo() }?.sortedWith(ToDoComparator) ?: emptyList()

    private fun DocumentSnapshot.toToDo() = ToDo(
        id = id,
        title = getString(TITLE) ?: EMPTY_STRING,
        starred = getBoolean(STARRED) ?: false,
        created = getLong(CREATED),
        completed = getBoolean(COMPLETED),
        due = getLong(DUE),
    )
}
