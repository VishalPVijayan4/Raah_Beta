// domain/model/User.kt
package com.buildndeploy.raah.domain.model

data class User(
    val uid: String,
    val email: String,
    val displayName: String,
    val photoUrl: String?
)
