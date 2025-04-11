package com.al_enterprise.helper

import android.content.Context
import android.content.SharedPreferences

class TokenProvider(private val context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE)

    // Get the stored token
    fun getToken(): String? {
        return sharedPreferences.getString("API_TOKEN", null)
    }

    // Save the token
    fun setToken(token: String) {
        sharedPreferences.edit().putString("API_TOKEN", token).apply()
    }

    // Remove the token (e.g., logout)
    fun removeToken() {
        sharedPreferences.edit().remove("API_TOKEN").apply()
    }
}
