package com.al_enterprise.helper

import android.content.Context
import android.content.SharedPreferences

class TokenProvider(private val context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE)

    // Get the stored token
    fun getToken(): String? {
        return sharedPreferences.getString("ovcirrusapi_API_TOKEN", null)
    }

    // Save the token
    fun setToken(token: String) {
        sharedPreferences.edit().putString("ovcirrusapi_API_TOKEN", token).apply()
    }

    // Remove the token (e.g., logout)
    fun removeToken() {
        sharedPreferences.edit().remove("ovcirrusapi_API_TOKEN").apply()
    }
}
