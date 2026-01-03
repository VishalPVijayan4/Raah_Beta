// data/remote/dto/ActivityDto.kt
package com.buildndeploy.raah.data.remote.dto

data class ActivityDto(
    val activityId: String = "",
    val userId: String = "",
    val type: String = "",
    val startTime: Long = 0L,
    val endTime: Long = 0L,
    val distance: Double = 0.0,
    val route: List<RoutePointDto> = emptyList()
)
