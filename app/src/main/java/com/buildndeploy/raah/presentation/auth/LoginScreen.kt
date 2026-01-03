package com.buildndeploy.raah.presentation.auth

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.ApiException

@Composable
fun LoginScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        Log.d("LoginScreen", "Activity result: ${result.resultCode}")
        if (result.resultCode == Activity.RESULT_OK) {
            try {
                val credential = Identity.getSignInClient(context)
                    .getSignInCredentialFromIntent(result.data)
                val idToken = credential.googleIdToken

                Log.d("LoginScreen", "Got credential - ID Token: ${idToken != null}")

                if (idToken != null) {
                    Log.d("LoginScreen", "Calling signInWithGoogle")
                    viewModel.handleIntent(AuthIntent.SignInWithGoogle(idToken))
                } else {
                    Log.e("LoginScreen", "ID Token is null!")
                }
            } catch (e: ApiException) {
                Log.e("LoginScreen", "ApiException: ${e.statusCode} - ${e.message}", e)
            } catch (e: Exception) {
                Log.e("LoginScreen", "Exception getting credential", e)
            }
        } else {
            Log.w("LoginScreen", "Result not OK: ${result.resultCode}")
        }
    }

    LaunchedEffect(state.oneTapSignInResult) {
        state.oneTapSignInResult?.let { result ->
            Log.d("LoginScreen", "Launching OneTap intent")
            val intentSenderRequest = IntentSenderRequest.Builder(
                result.pendingIntent.intentSender
            ).build()
            launcher.launch(intentSenderRequest)
        }
    }

    LaunchedEffect(state.isAuthenticated) {
        if (state.isAuthenticated) {
            Log.d("LoginScreen", "User authenticated - navigating to home")
            onLoginSuccess()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "Welcome to Raah",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Track your fitness journey",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (state.isLoading) {
                CircularProgressIndicator()
                Text(
                    text = "Signing in...",
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                Button(
                    onClick = {
                        Log.d("LoginScreen", "Sign in button clicked")
                        viewModel.handleIntent(AuthIntent.InitiateGoogleSignIn)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = !state.isLoading
                ) {
                    Text(
                        text = "Sign in with Google",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            state.error?.let { error ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Error",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = error,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }

                TextButton(
                    onClick = { viewModel.handleIntent(AuthIntent.ClearError) }
                ) {
                    Text("Dismiss")
                }
            }
        }
    }
}
