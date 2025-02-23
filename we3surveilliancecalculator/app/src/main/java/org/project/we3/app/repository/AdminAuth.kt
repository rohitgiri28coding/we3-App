package org.project.we3.app.repository

import org.project.we3.app.db.User

interface AdminAuth {

    suspend fun checkIsAdmin(user: User): Boolean
}