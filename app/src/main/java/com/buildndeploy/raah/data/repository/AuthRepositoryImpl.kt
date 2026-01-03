// data/repository/AuthRepositoryImpl.kt
package com.buildndeploy.raah.data.repository

import com.buildndeploy.raah.core.util.Resource
import com.buildndeploy.raah.domain.model.User
import com.buildndeploy.raah.domain.repository.AuthRepository
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val oneTapClient: SignInClient,
    @Named("signInRequest") private val signInRequest: BeginSignInRequest,
    @Named("signUpRequest") private val signUpRequest: BeginSignInRequest
) : AuthRepository {

    override suspend fun oneTapSignIn(): Flow<Resource<BeginSignInResult>> = flow {
        try {
            emit(Resource.Loading())
            val result = oneTapClient.beginSignIn(signInRequest).await()
            emit(Resource.Success(result))
        } catch (e: Exception) {
            try {
                val signUpResult = oneTapClient.beginSignIn(signUpRequest).await()
                emit(Resource.Success(signUpResult))
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "Unknown error occurred"))
            }
        }
    }

    override suspend fun signInWithGoogle(idToken: String): Flow<Resource<User>> = flow {
        try {
            emit(Resource.Loading())
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val authResult = auth.signInWithCredential(credential).await()
            val firebaseUser = authResult.user ?: throw Exception("User is null")

            val user = User(
                uid = firebaseUser.uid,
                email = firebaseUser.email ?: "",
                displayName = firebaseUser.displayName ?: "",
                photoUrl = firebaseUser.photoUrl?.toString()
            )
            emit(Resource.Success(user))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Authentication failed"))
        }
    }

    override suspend fun signOut(): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading())
            oneTapClient.signOut().await()
            auth.signOut()
            emit(Resource.Success(true))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Sign out failed"))
        }
    }

    override fun getCurrentUser(): User? {
        val firebaseUser = auth.currentUser ?: return null
        return User(
            uid = firebaseUser.uid,
            email = firebaseUser.email ?: "",
            displayName = firebaseUser.displayName ?: "",
            photoUrl = firebaseUser.photoUrl?.toString()
        )
    }

    override fun isUserAuthenticated(): Boolean = auth.currentUser != null
}
