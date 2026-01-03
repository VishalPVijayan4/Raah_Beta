// data/repository/ActivityRepositoryImpl.kt
package com.buildndeploy.raah.data.repository

import com.buildndeploy.raah.core.util.Constants
import com.buildndeploy.raah.core.util.Resource
import com.buildndeploy.raah.data.mapper.toDomain
import com.buildndeploy.raah.data.mapper.toDto
import com.buildndeploy.raah.data.remote.dto.ActivityDto
import com.buildndeploy.raah.domain.model.Activity
import com.buildndeploy.raah.domain.repository.ActivityRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ActivityRepositoryImpl @Inject constructor(
    private val database: FirebaseDatabase
) : ActivityRepository {

    private val activitiesRef = database.getReference(Constants.ACTIVITIES_NODE)

    override suspend fun saveActivity(activity: Activity): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading())
            val activityDto = activity.toDto()
            activitiesRef
                .child(activity.userId)
                .child(activity.activityId)
                .setValue(activityDto)
                .await()
            emit(Resource.Success(true))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to save activity"))
        }
    }

    override suspend fun getActivities(userId: String): Flow<Resource<List<Activity>>> =
        callbackFlow {
            trySend(Resource.Loading())

            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val activities = snapshot.children.mapNotNull {
                        it.getValue(ActivityDto::class.java)?.toDomain()
                    }
                    trySend(Resource.Success(activities))
                }

                override fun onCancelled(error: DatabaseError) {
                    trySend(Resource.Error(error.message))
                }
            }

            activitiesRef.child(userId).addValueEventListener(listener)

            awaitClose {
                activitiesRef.child(userId).removeEventListener(listener)
            }
        }

    override suspend fun getActivityById(
        userId: String,
        activityId: String
    ): Flow<Resource<Activity>> = flow {
        try {
            emit(Resource.Loading())
            val snapshot = activitiesRef
                .child(userId)
                .child(activityId)
                .get()
                .await()

            val activityDto = snapshot.getValue(ActivityDto::class.java)
                ?: throw Exception("Activity not found")

            emit(Resource.Success(activityDto.toDomain()))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to fetch activity"))
        }
    }

    override suspend fun deleteActivity(
        userId: String,
        activityId: String
    ): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading())
            activitiesRef
                .child(userId)
                .child(activityId)
                .removeValue()
                .await()
            emit(Resource.Success(true))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to delete activity"))
        }
    }
}
