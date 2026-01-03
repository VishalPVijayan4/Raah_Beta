// MainActivity.kt (updated)
package com.buildndeploy.raah

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import com.buildndeploy.raah.core.navigation.NavGraph
import com.buildndeploy.raah.core.navigation.Screen
import com.buildndeploy.raah.domain.repository.AuthRepository
import com.buildndeploy.raah.ui.theme.RaahTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("MainActivity", "=== App Started ===")
        Log.d("MainActivity", "Auth repository injected: ${authRepository != null}")
        Log.d("MainActivity", "User authenticated: ${authRepository.isUserAuthenticated()}")

        enableEdgeToEdge()
        setContent {
            RaahTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    val startDestination = if (authRepository.isUserAuthenticated()) {
                        Log.d("MainActivity", "Starting at: Home")
                        Screen.Home.route
                    } else {
                        Log.d("MainActivity", "Starting at: Login")
                        Screen.Login.route
                    }

                    NavGraph(
                        navController = navController,
                        startDestination = startDestination
                    )
                }
            }
        }
    }
}

