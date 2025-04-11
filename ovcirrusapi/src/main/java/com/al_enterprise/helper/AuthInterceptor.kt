package com.al_enterprise.helper

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val tokenProvider: TokenProvider
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        // Get the token from TokenProvider and add it to the Authorization header
        val token = tokenProvider.getToken()
        token?.let {
            request = request.newBuilder()
                .addHeader("Authorization", "Bearer $it")  // Add the Authorization header with the token
                .build()
            Log.d("AuthInterceptor", "Token added to request: $token")
        }

        // Log to ensure the token is being added to the request
        if (token != null) {
            Log.d("AuthInterceptor", "Authorization header added: Bearer $token")
        } else {
            Log.d("AuthInterceptor", "No token found")
        }

        var response = chain.proceed(request)

        // If the response is unauthorized (HTTP 401), attempt to reauthenticate
        if (response.code == 401) {
            response.close()

            synchronized(this) {
                val reauthenticated = reauthenticate()  // Trigger re-authentication

                if (reauthenticated) {
                    // Retry the original request with the new token
                    val newToken = tokenProvider.getToken()
                    newToken?.let {
                        request = request.newBuilder()
                            .removeHeader("Authorization")  // Remove the old token
                            .addHeader("Authorization", "Bearer $it")  // Add the new token
                            .build()

                        // Retry the original request with the new token
                        response = chain.proceed(request)
                    }
                }
            }
        }

        return response
    }

    // Reauthenticate the user (e.g., by calling authenticate method again)
    private fun reauthenticate(): Boolean {
        // Trigger re-authentication logic here (for example, call authenticate() again)
        return true // Re-authentication logic goes here
    }
}


