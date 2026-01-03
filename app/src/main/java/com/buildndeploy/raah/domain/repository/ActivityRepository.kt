// domain/repository/ActivityRepository.kt
package com.buildndeploy.raah.domain.repository

import com.buildndeploy.raah.core.util.Resource
import com.buildndeploy.raah.domain.model.Activity
import kotlinx.coroutines.flow.Flow

interface ActivityRepository {
    suspend fun saveActivity(activity: Activity): Flow<Resource<Boolean>>
    suspend fun getActivities(userId: String): Flow<Resource<List<Activity>>>
    suspend fun getActivityById(userId: String, activityId: String): Flow<Resource<Activity>>
    suspend fun deleteActivity(userId: String, activityId: String): Flow<Resource<Boolean>>
}
