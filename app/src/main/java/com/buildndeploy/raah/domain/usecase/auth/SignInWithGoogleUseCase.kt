// domain/usecase/auth/SignInWithGoogleUseCase.kt
package com.buildndeploy.raah.domain.usecase.auth

import com.buildndeploy.raah.core.util.Resource
import com.buildndeploy.raah.domain.model.User
import com.buildndeploy.raah.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(idToken: String): Flow<Resource<User>> {
        return repository.signInWithGoogle(idToken)
    }
}
