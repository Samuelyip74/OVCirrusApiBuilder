package com.al_enterprise.dataclasses

data class ApiResponse<T>(
    val status: Int? = null,
    val message: String? = null,
    val data: T? = null,
    val errorCode: Int? = null,
    val errorMsg: String? = null,
    val errorResourceName: String? = null,
    val errorPropertyName: String? = null,
    val errorDetails: String? = null,
    val errorDetailsCode: String? = null,
    val errorLabel: String? = null,
    val conflictValues: List<String>? = null,
    val errors: List<ErrorDetail>? = null
)

data class ErrorDetail(
    val type: String,
    val field: String,
    val errorMsg: String
)