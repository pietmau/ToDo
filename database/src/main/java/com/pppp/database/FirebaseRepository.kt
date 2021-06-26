package com.pppp.database

import com.google.firebase.firestore.*
import com.pppp.database.FirebaseRepository.Companion.COMPLETED
import com.pppp.database.FirebaseRepository.Companion.CREATED
import com.pppp.database.FirebaseRepository.Companion.DUE
import com.pppp.database.FirebaseRepository.Companion.EMPTY_STRING
import com.pppp.database.FirebaseRepository.Companion.STARRED
import com.pppp.database.FirebaseRepository.Companion.TITLE
import com.pppp.entities.ToDo
import com.pppp.usecases.Repository
import com.pppp.usecases.addtodo.AddToDoUseCase
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

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

    override suspend fun addToDo(params: AddToDoUseCase.Params) {
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
        }
    }

    override fun edit(id: String, values: Map<String, Any?>) {
        db.collection(TODOS).document(id).update(values)
    }

    companion object {
        const val ID = "id"
        const val TODOS = "todos"
        const val TITLE = "title"
        const val STARRED = "starred"
        const val CREATED = "created"
        const val COMPLETED = "completed"
        const val DUE = "due"
        const val EMPTY_STRING = ""
    }
}

private fun QuerySnapshot?.toToDoList(): List<ToDo> =
    this?.documents?.map { it.toToDo() }?.sortedBy { it.due } ?: emptyList()

private fun DocumentSnapshot.toToDo() = ToDo(
    id = this.id,
    title = this.get(TITLE, String::class.java) ?: EMPTY_STRING,
    starred = this.getBoolean(STARRED) ?: false,
    created = this.get(CREATED, Long::class.java),
    completed = this.getBoolean(COMPLETED),
    due = this.get(DUE, Long::class.java),
)
