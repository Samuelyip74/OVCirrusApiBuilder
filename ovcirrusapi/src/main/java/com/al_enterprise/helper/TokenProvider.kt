package com.al_enterprise.helper

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class TokenProvider(private val context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE)

    // Get the stored token
    fun getToken(): String? {
        return sharedPreferences.getString("CIRRUS_API_TOKEN", null)
    }

    fun getExpiresIn(): Long {
        return sharedPreferences.getString("CIRRUS_EXPIRES_IN", null)?.toLong() ?: 0
    }

    // Save the token
    fun setToken(token: String, expiresIn: Long?) {
        val expiryTime = System.currentTimeMillis() + expiresIn!!
        sharedPreferences.edit().putString("CIRRUS_API_TOKEN", token).apply()
        sharedPreferences.edit().putString("CIRRUS_EXPIRES_IN", expiryTime.toString()).apply()
    }

    // Remove the token (e.g., logout)
    fun removeToken() {
        sharedPreferences.edit().remove("CIRRUS_API_TOKEN").apply()
        sharedPreferences.edit().remove("CIRRUS_EXPIRES_IN").apply()
    }
}
