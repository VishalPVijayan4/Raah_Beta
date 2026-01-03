// presentation/auth/AuthIntent.kt
package com.buildndeploy.raah.presentation.auth

sealed interface AuthIntent {
    object InitiateGoogleSignIn : AuthIntent
    data class SignInWithGoogle(val idToken: String) : AuthIntent
    object SignOut : AuthIntent
    object ClearError : AuthIntent
}
