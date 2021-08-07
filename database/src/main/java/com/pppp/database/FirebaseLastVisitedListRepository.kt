package com.pppp.database

import com.google.firebase.firestore.FirebaseFirestore
import com.pppp.usecases.ListsRepository
import com.pppp.usecases.main.LastVisitedListRepository
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseLastVisitedListRepository(private val db: FirebaseFirestore) :
    LastVisitedListRepository {

    override suspend fun getLastVisitedList(userId: String) =
        suspendCoroutine { continuation: Continuation<String?> ->
            db.collection(ListsRepository.USERS).document(userId).get().addOnSuccessListener {
                val listId = try {
                    it[LAST_LIST] as? String
                } catch (exception: Exception) {
                    // TODO add log
                    null
                }
                continuation.resume(listId)
            }.addOnFailureListener {
                continuation.resume(null)
            }
        }

    override suspend fun addList(userId: String, listId: String): String =
        suspendCoroutine { continuation ->
            db.collection(ListsRepository.USERS).document(userId).update(LAST_LIST, listId)
                .addOnSuccessListener { continuation.resume(listId) }
        }

    private companion object {
        private const val LAST_LIST = "last_visited_list"
    }
}