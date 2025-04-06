package org.project.we3.data.repository

import org.project.we3.data.model.User

interface AdminAuth {

    suspend fun checkIsAdmin(user: User): Boolean
}