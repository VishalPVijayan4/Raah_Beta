// domain/usecase/activity/GetActivitiesUseCase.kt
package com.buildndeploy.raah.domain.usecase.activity

import com.buildndeploy.raah.core.util.Resource
import com.buildndeploy.raah.domain.model.Activity
import com.buildndeploy.raah.domain.repository.ActivityRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetActivitiesUseCase @Inject constructor(
    private val repository: ActivityRepository
) {
    suspend operator fun invoke(userId: String): Flow<Resource<List<Activity>>> {
        return repository.getActivities(userId)
    }
}
