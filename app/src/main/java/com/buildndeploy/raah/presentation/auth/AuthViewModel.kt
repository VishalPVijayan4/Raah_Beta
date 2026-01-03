package com.buildndeploy.raah.presentation.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buildndeploy.raah.core.util.Resource
import com.buildndeploy.raah.domain.repository.AuthRepository
import com.buildndeploy.raah.domain.usecase.auth.GetCurrentUserUseCase
import com.buildndeploy.raah.domain.usecase.auth.SignInWithGoogleUseCase
import com.buildndeploy.raah.domain.usecase.auth.SignOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    companion object {
        private const val TAG = "AuthViewModel"
    }

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()

    init {
        Log.d(TAG, "ViewModel initialized")
        checkAuthStatus()
    }

    fun handleIntent(intent: AuthIntent) {
        Log.d(TAG, "Intent received: $intent")
        when (intent) {
            is AuthIntent.InitiateGoogleSignIn -> initiateGoogleSignIn()
            is AuthIntent.SignInWithGoogle -> signInWithGoogle(intent.idToken)
            is AuthIntent.SignOut -> signOut()
            is AuthIntent.ClearError -> clearError()
        }
    }

    private fun checkAuthStatus() {
        val currentUser = getCurrentUserUseCase()
        Log.d(TAG, "Current user: ${currentUser?.email ?: "Not authenticated"}")
        _state.update {
            it.copy(
                user = currentUser,
                isAuthenticated = currentUser != null
            )
        }
    }

    private fun initiateGoogleSignIn() {
        Log.d(TAG, "=== Initiating Google Sign-In ===")
        viewModelScope.launch {
            try {
                authRepository.oneTapSignIn().collect { result ->
                    Log.d(TAG, "OneTap result: ${result::class.simpleName}")
                    when (result) {
                        is Resource.Loading -> {
                            Log.d(TAG, "OneTap: Loading...")
                            _state.update { it.copy(isLoading = true, error = null) }
                        }
                        is Resource.Success -> {
                            Log.d(TAG, "OneTap: Success! Result: ${result.data}")
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    oneTapSignInResult = result.data
                                )
                            }
                        }
                        is Resource.Error -> {
                            Log.e(TAG, "OneTap: Error - ${result.message}")
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    error = result.message
                                )
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception in initiateGoogleSignIn", e)
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Unknown error"
                    )
                }
            }
        }
    }

    private fun signInWithGoogle(idToken: String) {
        Log.d(TAG, "=== Signing in with Google token ===")
        Log.d(TAG, "Token (first 20 chars): ${idToken.take(20)}...")
        viewModelScope.launch {
            try {
                signInWithGoogleUseCase(idToken).collect { result ->
                    Log.d(TAG, "Sign-in result: ${result::class.simpleName}")
                    when (result) {
                        is Resource.Loading -> {
                            Log.d(TAG, "Sign-in: Authenticating...")
                            _state.update { it.copy(isLoading = true, error = null) }
                        }
                        is Resource.Success -> {
                            Log.d(TAG, "Sign-in: SUCCESS! User: ${result.data?.email}")
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    user = result.data,
                                    isAuthenticated = true,
                                    oneTapSignInResult = null
                                )
                            }
                        }
                        is Resource.Error -> {
                            Log.e(TAG, "Sign-in: ERROR - ${result.message}")
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    error = result.message
                                )
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception in signInWithGoogle", e)
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Unknown error"
                    )
                }
            }
        }
    }

    private fun signOut() {
        Log.d(TAG, "=== Signing out ===")
        viewModelScope.launch {
            signOutUseCase().collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        Log.d(TAG, "Sign-out: Success")
                        _state.update {
                            AuthState(isAuthenticated = false)
                        }
                    }
                    is Resource.Error -> {
                        Log.e(TAG, "Sign-out: Error - ${result.message}")
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = result.message
                            )
                        }
                    }
                }
            }
        }
    }

    private fun clearError() {
        Log.d(TAG, "Clearing error")
        _state.update { it.copy(error = null) }
    }
}
