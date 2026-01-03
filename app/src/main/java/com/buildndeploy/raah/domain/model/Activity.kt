// domain/model/Activity.kt
package com.buildndeploy.raah.domain.model

data class Activity(
    val activityId: String,
    val userId: String,
    val type: String,
    val startTime: Long,
    val endTime: Long,
    val distance: Double,
    val route: List<RoutePoint>
)
