// presentation/home/HomeIntent.kt
package com.buildndeploy.raah.presentation.home

sealed interface HomeIntent {
    object LoadActivities : HomeIntent
    object SignOut : HomeIntent
}
