// presentation/auth/AuthState.kt
package com.buildndeploy.raah.presentation.auth

import com.buildndeploy.raah.domain.model.User
import com.google.android.gms.auth.api.identity.BeginSignInResult

data class AuthState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val isAuthenticated: Boolean = false,
    val oneTapSignInResult: BeginSignInResult? = null,
    val error: String? = null
)
