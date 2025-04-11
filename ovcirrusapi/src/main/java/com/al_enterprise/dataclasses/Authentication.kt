package com.al_enterprise.dataclasses

data class LoginCredentials(
    val email: String,
    val password: String,
    val appId: String,
    val appSecret: String
)

data class AuthToken(
    val access_token : String,
    val expires_in: Long,
    val token_type: String
)