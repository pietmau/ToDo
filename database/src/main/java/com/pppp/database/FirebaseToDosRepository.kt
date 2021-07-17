package com.pppp.database

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.pppp.entities.ToDo
import com.pppp.usecases.ListsRepository.Companion.USERS
import com.pppp.usecases.ToDosRepository
import com.pppp.usecases.ToDosRepository.Companion.COMPLETED
import com.pppp.usecases.ToDosRepository.Companion.CREATED
import com.pppp.usecases.ToDosRepository.Companion.DUE
import com.pppp.usecases.ToDosRepository.Companion.EMPTY_STRING
import com.pppp.usecases.ToDosRepository.Companion.LISTS
import com.pppp.usecases.ToDosRepository.Companion.LIST_ID
import com.pppp.usecases.ToDosRepository.Companion.STARRED
import com.pppp.usecases.ToDosRepository.Companion.TITLE
import com.pppp.usecases.ToDosRepository.Companion.TODOS
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseToDosRepository(private val db: FirebaseFirestore) : ToDosRepository {

    @ExperimentalCoroutinesApi
    override fun getList(userId: String, listId: String): Flow<List<ToDo>> =
        callbackFlow {
            val registration = listReference(userId, listId)
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        close(error)
                    } else {
                        trySend(value.toToDoList(listId))
                    }
                }
            awaitClose {
                registration.remove()
            }
        }

    override suspend fun addToDo(params: ToDosRepository.Params.Add): String =
        suspendCoroutine { continuation ->
            listReference(userId = params.userId, listId = params.listId).add(
                ToDo(
                    title = params.title,
                    created = System.currentTimeMillis(),
                    due = params.due,
                    listId = params.listId
                )
            )
        }

    override suspend fun edit(params: ToDosRepository.Params.Edit): String =
        suspendCoroutine { continuation ->
            listReference(params.userId, params.listId).document(params.itemId)
                .update(params.values).addOnSuccessListener {
                    continuation.resume(params.itemId)
                }
        }

    @ExperimentalCoroutinesApi
    override suspend fun getToDo(params: ToDosRepository.Params.GetSingle): Flow<List<ToDo>> =
        callbackFlow {
            listReference(params.userId, params.listId).document(params.itemId).get()
                .addOnSuccessListener { value ->
                    trySend(listOf(value.toToDo(params.listId)))
                }
            awaitClose {
            }
        }

    private fun listReference(userId: String, listId: String): CollectionReference =
        db.collection(USERS).document(userId).collection(LISTS).document(listId).collection(TODOS)

    private fun QuerySnapshot?.toToDoList(listId: String): List<ToDo> =
        this?.documents?.map { it.toToDo(listId) }?.sortedWith(ToDoComparator) ?: emptyList()

    private fun DocumentSnapshot.toToDo(listId: String) =
        ToDo(
            id = id,
            title = getString(TITLE) ?: EMPTY_STRING,
            starred = getBoolean(STARRED) ?: false,
            created = getLong(CREATED),
            completed = getBoolean(COMPLETED),
            due = getLong(DUE),
            listId = listId
        )
}
