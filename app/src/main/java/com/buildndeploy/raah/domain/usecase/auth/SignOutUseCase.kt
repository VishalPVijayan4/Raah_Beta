// domain/usecase/auth/SignOutUseCase.kt
package com.buildndeploy.raah.domain.usecase.auth

import com.buildndeploy.raah.core.util.Resource
import com.buildndeploy.raah.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): Flow<Resource<Boolean>> {
        return repository.signOut()
    }
}
