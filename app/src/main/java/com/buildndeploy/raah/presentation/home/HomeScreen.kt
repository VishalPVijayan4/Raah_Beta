// presentation/home/HomeScreen.kt
package com.buildndeploy.raah.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onSignOut: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Raah - Activities") },
                actions = {
                    IconButton(onClick = {
                        viewModel.handleIntent(HomeIntent.SignOut)
                        onSignOut()
                    }) {
                        Icon(Icons.Default.ExitToApp, "Sign Out")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                state.activities.isEmpty() -> {
                    Text(
                        text = "No activities yet. Start tracking!",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.activities) { activity ->
                            ActivityItem(activity = activity)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ActivityItem(activity: com.buildndeploy.raah.domain.model.Activity) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = activity.type,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Distance: ${activity.distance} km",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Duration: ${(activity.endTime - activity.startTime) / 1000 / 60} mins",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
