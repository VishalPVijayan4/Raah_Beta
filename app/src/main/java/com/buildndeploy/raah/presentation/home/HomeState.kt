// presentation/home/HomeState.kt
package com.buildndeploy.raah.presentation.home

import com.buildndeploy.raah.domain.model.Activity
import com.buildndeploy.raah.domain.model.User

data class HomeState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val activities: List<Activity> = emptyList(),
    val error: String? = null
)
