// domain/repository/AuthRepository.kt
package com.buildndeploy.raah.domain.repository

import com.buildndeploy.raah.core.util.Resource
import com.buildndeploy.raah.domain.model.User
import com.google.android.gms.auth.api.identity.BeginSignInResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun oneTapSignIn(): Flow<Resource<BeginSignInResult>>
    suspend fun signInWithGoogle(idToken: String): Flow<Resource<User>>
    suspend fun signOut(): Flow<Resource<Boolean>>
    fun getCurrentUser(): User?
    fun isUserAuthenticated(): Boolean
}
