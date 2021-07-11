package com.pppp.database

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.pppp.entities.ToDo
import com.pppp.entities.ToDoList
import com.pppp.usecases.ListsRepository
import com.pppp.usecases.ListsRepository.Companion.ARCHIVED
import com.pppp.usecases.ListsRepository.Companion.CREATED
import com.pppp.usecases.ListsRepository.Companion.DELETED
import com.pppp.usecases.ListsRepository.Companion.LISTS
import com.pppp.usecases.ListsRepository.Companion.LIST_ID
import com.pppp.usecases.ListsRepository.Companion.MODIFIED
import com.pppp.usecases.ListsRepository.Companion.PRIORITY
import com.pppp.usecases.ListsRepository.Companion.USERS
import com.pppp.usecases.ToDosRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseListsRepository(
    private val db: FirebaseFirestore,
    private val userId: String,
) : ListsRepository {

    override suspend fun getLists(): Flow<List<ToDoList>> =
        callbackFlow {
            val registration = db.collection(USERS).document(userId).collection(LISTS)
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        close(error)
                    } else {
                        trySend(value.toLists())
                    }
                }
            awaitClose {
                registration.remove()
            }
        }

    override suspend fun addList(toDoList: ToDoList) {
        db.collection(USERS).document(userId).get().addOnSuccessListener {

        }.addOnFailureListener {
            addUser()
        }
    }

    private fun addUser(): String {
        TODO("Not yet implemented")
    }

    override suspend fun editList(toDoList: ToDoList): String {
        TODO("Not yet implemented")
    }

}

private fun QuerySnapshot?.toLists(): List<ToDoList> =
    this?.documents?.map { it.toList() }?.sortedBy { it.priority } ?: emptyList()

private fun DocumentSnapshot.toList(): ToDoList =
    ToDoList(
        id = id,
        listId = requireNotNull(this.getString(LIST_ID)),
        created = getLong(CREATED),
        modified = getLong(MODIFIED),
        archived = getBoolean(ARCHIVED) ?: false,
        deleted = getBoolean(DELETED) ?: false,
        priority = getLong(PRIORITY) ?: 0
    )
