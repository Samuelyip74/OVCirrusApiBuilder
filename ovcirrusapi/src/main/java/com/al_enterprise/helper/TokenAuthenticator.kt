package com.al_enterprise.helper

import kotlinx.coroutines.runBlocking

class TokenAuthenticator(
    private val email: String,
    private val password: String,
    private val appId: String,
    private val appSecret: String,
    private val baseUrl: String
)  {

    private lateinit var apiClient: ApiClient

    fun reauthenticate(): Boolean {
        return runBlocking {
            try {
                val isAuthenticated =
                    apiClient.authenticate(email, password, appId, appSecret, baseUrl)
                isAuthenticated
            } catch (e: Exception) {
                false
            }
        }
    }

}