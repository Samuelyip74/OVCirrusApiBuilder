package com.al_enterprise.dataclasses

data class User (
    val id: Int? = null,
    val role: String? = null,
    val user: UserProfile? = null,
)

data class UserProfile(
    val failedTry: Int? = null,
    val lockedUntilDate: Long? = null,
    val lastLoginDate: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val id: String? = null,
    var firstname: String? = null,
    var lastname: String? = null,
    var email: String? = null,
    val verified: Boolean? = null,
    val preferredLanguage: String? = null,
    var country: String? = null,
    val closestRegion: String? = null,
    val companyName: String? = null,
    val avatarLocation: String? = null,
    val address: String? = null,
    val city: String? = null,
    val zipCode: String? = null,
    val phoneNumber: String? = null,
    val isTwoFAEnabled: Boolean? = null,
    val faMethod: String? = null,
    val tempSecret: String? = null,
    val secret: String? = null,
    val enforcementPolicy: String? = null,
    val accessLevelRole: String? = null,
    //val msp: String // Ensure this matches the expected response
)