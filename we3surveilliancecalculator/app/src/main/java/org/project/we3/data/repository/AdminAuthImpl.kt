package org.project.we3.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import org.project.we3.data.model.User

class AdminAuthImpl(private val firestore: FirebaseFirestore) : AdminAuth {
    override suspend fun checkIsAdmin(user: User): Boolean {
        return try {
            val querySnapshot = firestore.collection("admin")
                .whereEqualTo("email", user.email)
                .get()
                .await()

            val adminDocument = querySnapshot.documents.firstOrNull()
            if (adminDocument != null) {
                val storedPassword = adminDocument.getString("password") ?: ""
                return user.password.hashCode().toString() == storedPassword
            }
            false
        } catch (_: Exception) {
            false
        }
    }
}
