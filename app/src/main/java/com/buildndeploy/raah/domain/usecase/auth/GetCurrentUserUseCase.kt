// domain/usecase/auth/GetCurrentUserUseCase.kt
package com.buildndeploy.raah.domain.usecase.auth

import com.buildndeploy.raah.domain.model.User
import com.buildndeploy.raah.domain.repository.AuthRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(): User? = repository.getCurrentUser()
}
