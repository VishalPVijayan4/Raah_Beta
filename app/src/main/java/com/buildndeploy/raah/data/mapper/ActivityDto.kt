// data/mapper/ActivityMapper.kt
package com.buildndeploy.raah.data.mapper

import com.buildndeploy.raah.data.remote.dto.ActivityDto
import com.buildndeploy.raah.data.remote.dto.RoutePointDto
import com.buildndeploy.raah.domain.model.Activity
import com.buildndeploy.raah.domain.model.RoutePoint

fun ActivityDto.toDomain(): Activity {
    return Activity(
        activityId = activityId,
        userId = userId,
        type = type,
        startTime = startTime,
        endTime = endTime,
        distance = distance,
        route = route.map { it.toDomain() }
    )
}

fun Activity.toDto(): ActivityDto {
    return ActivityDto(
        activityId = activityId,
        userId = userId,
        type = type,
        startTime = startTime,
        endTime = endTime,
        distance = distance,
        route = route.map { it.toDto() }
    )
}

fun RoutePointDto.toDomain(): RoutePoint {
    return RoutePoint(
        lat = lat,
        lng = lng,
        timestamp = t
    )
}

fun RoutePoint.toDto(): RoutePointDto {
    return RoutePointDto(
        lat = lat,
        lng = lng,
        t = timestamp
    )
}
