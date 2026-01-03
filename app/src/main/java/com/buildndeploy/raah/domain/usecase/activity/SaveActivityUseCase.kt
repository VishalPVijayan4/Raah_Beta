// domain/usecase/activity/SaveActivityUseCase.kt
package com.buildndeploy.raah.domain.usecase.activity

import com.buildndeploy.raah.core.util.Resource
import com.buildndeploy.raah.domain.model.Activity
import com.buildndeploy.raah.domain.repository.ActivityRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SaveActivityUseCase @Inject constructor(
    private val repository: ActivityRepository
) {
    suspend operator fun invoke(activity: Activity): Flow<Resource<Boolean>> {
        return repository.saveActivity(activity)
    }
}
