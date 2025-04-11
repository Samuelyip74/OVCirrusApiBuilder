package com.al_enterprise.helper

import com.al_enterprise.dataclasses.ApiResponse
import com.al_enterprise.dataclasses.AuthToken
import com.al_enterprise.dataclasses.LoginCredentials
import com.al_enterprise.dataclasses.UserProfile
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Url

interface ApiService {

    // Authentication request to get the token
    @POST("api/ov/v1/applications/authenticate")
    suspend fun authenticate(
        @Body request: LoginCredentials
    ): Response<AuthToken>  // Specify the expected response type (AuthToken)

    // GET request with dynamic endpoint

    @GET
    suspend fun <T> getData(
        @Url url: String,
    ): ApiResponse<T>  // Using @Url instead of @Path

    // POST request with dynamic endpoint
    @POST("{endpoint}")
    suspend fun <T> postData(
        @Path("endpoint") endpoint: String,
        @Body body: Any
    ): ApiResponse<T>

    // PUT request with dynamic endpoint
    @PUT("{endpoint}")
    suspend fun <T> putData(
        @Path("endpoint") endpoint: String,
        @Body body: Any
    ): ApiResponse<T>

    // DELETE request with dynamic endpoint
    @DELETE("{endpoint}")
    suspend fun <T> deleteData(
        @Path("endpoint") endpoint: String
    ): ApiResponse<T>

    // DELETE request with dynamic endpoint
    @DELETE("{endpoint}")
    suspend fun <T> deleteDataWithBody(
        @Path("endpoint") endpoint: String,
        @Body body: Any
    ): ApiResponse<T>
}

