package com.al_enterprise.helper

import com.al_enterprise.dataclasses.AuthToken
import com.al_enterprise.dataclasses.LoginCredentials
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type


class ApiClient(
    private val baseUrl: String,
    private val email: String,
    private val password: String,
    private val appId: String,
    private val appSecret: String,
    private val tokenProvider: TokenProvider
) {

    private val tokenAuthenticator = TokenAuthenticator(
        email = email,
        password = password,
        appId = appId,
        appSecret = appSecret,
        baseUrl = baseUrl
    )


    // Create the HttpLoggingInterceptor
    private val loggingInterceptor = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    // Register the deserializer
    val gson = GsonBuilder()
        .setLenient()
        .registerTypeAdapter(Int::class.java, NullStringToZeroAdapter())
        .registerTypeAdapter(Integer::class.java, NullStringToZeroAdapter())
        .registerTypeAdapter(String::class.java, StringTypeAdapter()) // Register custom StringTypeAdapter
        .registerTypeAdapter(JsonObject::class.java, NullJsonObjectAdapter()) // handle JsonObject if needed
        .create()

    // Initialize Retrofit with OkHttpClient inside the constructor
    val retrofit: Retrofit by lazy {
        val client = OkHttpClient.Builder().apply {
            addInterceptor(AuthInterceptor(tokenProvider, tokenAuthenticator))  // Add the AuthInterceptor
            addInterceptor(loggingInterceptor)              // Add logging interceptor
        }.build()

        // Creating Retrofit instance with OkHttpClient
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    // Generic method to create ApiService instances
    inline fun <reified T> getApiService(): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    // Authenticate and get the token from the backend
    suspend fun authenticate(
        email: String,
        password: String,
        appId: String,
        appSecret: String,
        baseUrl: String
    ): Boolean {
        val loginCredentials = LoginCredentials(
            email = this.email ?: "",
            password = this.password ?: "",
            appId = this.appId ?: "",
            appSecret = this.appSecret ?: ""
        )

        val response: Response<AuthToken> = getApiService<ApiService>().authenticate(loginCredentials)

        if (response.isSuccessful) {
            // Store the token after successful authentication
            if(response.body()?.access_token != null && response.body()?.expires_in  != null){
                tokenProvider.setToken(response.body()?.access_token!!, response.body()?.expires_in)
            }
            return true
        }
        return false
    }


}

class NullStringToZeroAdapter : JsonDeserializer<Int?> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Int? {
        //Log.d("NullStringToZeroAdapter", "json: ${json?.asString}")
        if (json == null || json.asString.isEmpty()) {
            return null  // Return null for empty strings or null values
        }
        return try {
            json.asInt // Try to parse as an integer
        } catch (e: NumberFormatException) {
            //Log.d("NullStringToZeroAdapter", "Error while parsing number: ${e.message}")
            null
        }
    }
}

class StringTypeAdapter : JsonDeserializer<String?> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): String? {
        //Log.d("StringTypeAdapter", "json: ${json?.asString}")
        if (json == null || json.isJsonNull) {
            return null  // Return null for empty strings or null values
        }
        return try {
            json.asString // otherwise return the actual string
        } catch (e: UnsupportedOperationException) {
            //Log.d("NullStringToZeroAdapter", "Error while parsing number: ${e.message}")
            null
        }
    }

}

class NullJsonObjectAdapter : JsonDeserializer<JsonObject?> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): JsonObject? {
        // If the field is null or JsonNull, return null
        if (json == null || json.isJsonNull) {
            return null
        }
        // Otherwise, return the JsonObject
        return json.asJsonObject
    }
}

class NullStringToDoubleAdapter : JsonDeserializer<Double?> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Double? {
        return if (json == null || json.asString.isEmpty()) {
            null // Return null if the string is empty
        } else {
            json.asDouble // Otherwise, parse the double
        }
    }
}

