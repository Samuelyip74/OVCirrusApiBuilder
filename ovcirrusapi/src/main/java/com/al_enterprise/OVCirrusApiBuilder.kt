package com.al_enterprise

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.al_enterprise.dataclasses.ApiResponse
import com.al_enterprise.dataclasses.DeviceData
import com.al_enterprise.dataclasses.DeviceDetail
import com.al_enterprise.dataclasses.Organization
import com.al_enterprise.dataclasses.OrganizationConfig
import com.al_enterprise.dataclasses.SSID
import com.al_enterprise.dataclasses.SSIDResponse
import com.al_enterprise.dataclasses.Site
import com.al_enterprise.dataclasses.User
import com.al_enterprise.dataclasses.UserProfile
import com.al_enterprise.helper.ApiClient
import com.al_enterprise.helper.ApiService
import com.al_enterprise.helper.TokenProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OVCirrusApiBuilder(private val context: Context) {

    private var baseUrl: String? = null
    private var email: String? = null
    private var password: String? = null
    private var appId: String? = null
    private var appSecret: String? = null
    private lateinit var apiClient: ApiClient
    private val tokenProvider: TokenProvider = TokenProvider(context)

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var INSTANCE: OVCirrusApiBuilder? = null

        fun initialize(context: Context): OVCirrusApiBuilder {
            if (INSTANCE == null) {
                INSTANCE = OVCirrusApiBuilder(context)
            }
            return INSTANCE!!
        }

        fun getInstance(): OVCirrusApiBuilder {
            return INSTANCE ?: throw IllegalStateException("OVCirrusApiBuilder is not initialized. Call initialize(context) first.")
        }
    }

    // Setter methods for each required field, enabling method chaining
    fun setBaseUrl(baseUrl: String): OVCirrusApiBuilder {
        this.baseUrl = baseUrl
        return this
    }

    fun setEmail(email: String): OVCirrusApiBuilder {
        this.email = email
        return this
    }

    fun setPassword(password: String): OVCirrusApiBuilder {
        this.password = password
        return this
    }

    fun setAppId(appId: String): OVCirrusApiBuilder {
        this.appId = appId
        return this
    }

    fun setAppSecret(appSecret: String): OVCirrusApiBuilder {
        this.appSecret = appSecret
        return this
    }

    // Method to build the ApiClient and initialize the builder
    suspend fun build(): OVCirrusApiBuilder {
        if (baseUrl == null || email == null || password == null || appId == null || appSecret == null) {
            throw IllegalStateException("All fields (baseUrl, email, password, appId, appSecret) must be set")
        }

        apiClient = ApiClient(baseUrl!!, email!!, password!!, appId!!, appSecret!!, tokenProvider)

        val isAuthenticated = authenticate()

        if (!isAuthenticated) {
            throw Exception("Authentication failed")
        }

        return this
    }

    suspend fun authenticate(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                // Check if expires in is empty
                var isAuthenticated: Boolean
                val tokenExpiry = tokenProvider.getExpiresIn()
                val token =  tokenProvider.getToken()
                val currentTime = System.currentTimeMillis()
                val fiveMinutes = 5 * 60 * 1000L // 5 minutes

                // Check token is present and token is less than 5 minutes before expiry
                if(token != null && tokenExpiry - currentTime < fiveMinutes) {
                    Log.d("OVCApiBuilder", "Token is about to expire. Refreshing...")
                    isAuthenticated = apiClient.authenticate(
                        email!!,
                        password!!,
                        appId!!,
                        appSecret!!,
                        baseUrl!!
                    )
                    if (isAuthenticated) {
                        Log.d("OVCApiBuilder", "Token refresh successful.")
                    } else {
                        Log.e("OVCApiBuilder", "Token refresh failed.")
                    }
                } else if (token == null) {
                    isAuthenticated = apiClient.authenticate(
                        email!!,
                        password!!,
                        appId!!,
                        appSecret!!,
                        baseUrl!!
                    )
                    if (isAuthenticated) {
                        Log.d("OVCApiBuilder", "Authentication successful.")
                    } else {
                        Log.e("OVCApiBuilder", "Authentication failed.")
                    }
                } else {
                    isAuthenticated = true
                    //Log.d("OVCApiBuilder", "Token is still valid.")
                }
                isAuthenticated
            } catch (e: Exception) {
                //Log.e("OVCApiBuilder", "Error during authentication: ${e.message}")
                false
            }
        }
    }

    fun logout(){
        tokenProvider.removeToken()
        //Log.d("OVCApiBuilder", "Token removed.")
    }

    // region User API methods
    suspend fun getUserProfile(): ApiResponse<UserProfile> {
        val endpoint = "api/ov/v1/user/profile"
        return apiClient.getApiService<ApiService>().getData(endpoint)
    }

    suspend fun updateUserProfile(userProfile: UserProfile): ApiResponse<UserProfile> {
        val endpoint = "api/ov/v1/user/profile"
        return apiClient.getApiService<ApiService>().putData(endpoint,userProfile)
    }

    // endregion

    // region Device API methods

    suspend fun createADevice(orgId: String, siteId: String, deviceData: DeviceData): ApiResponse<DeviceData> {
        val endpoint = "api/ov/v1/organizations/$orgId/sites/$siteId/devices"
        return apiClient.getApiService<ApiService>().postData(endpoint,deviceData)
    }

    suspend fun getAllDevices(orgId: String, siteId: String): ApiResponse<List<DeviceDetail>> {
        val endpoint = "api/ov/v1/organizations/$orgId/sites/$siteId/devices"
        return apiClient.getApiService<ApiService>().getData(endpoint)
    }

    suspend fun getAllDevicesFromOrganization(orgId: String): ApiResponse<List<DeviceDetail>> {
        val endpoint = "api/ov/v1/organizations/$orgId/sites/devices"
        return apiClient.getApiService<ApiService>().getData(endpoint)
    }

    suspend fun getDeviceById(orgId: String, deviceId: String): ApiResponse<DeviceData> {
        val endpoint = "api/ov/v1/organizations/$orgId/devices/$deviceId"
        return apiClient.getApiService<ApiService>().getData(endpoint)
    }

    suspend fun updateADevice(orgId: String, siteId: String, deviceId: String, deviceData: DeviceData): ApiResponse<DeviceData> {
        val endpoint = "api/ov/v1/organizations/$orgId/sites/$siteId/devices/$deviceId"
        return apiClient.getApiService<ApiService>().putData(endpoint,deviceData)
    }

    suspend fun deleteADevice(orgId: String, siteId: String, deviceId: String): ApiResponse<String> {
        val endpoint = "api/ov/v1/organizations/$orgId/sites/$siteId/devices/$deviceId"
        return apiClient.getApiService<ApiService>().deleteData(endpoint)
    }

    suspend fun updateRemoteAP(orgId: String, siteId: String, deviceId: String, deviceData: DeviceData): ApiResponse<DeviceData> {
        val endpoint = "api/ov/v1/organizations/$orgId/sites/$siteId/remote-aps/$deviceId"
        return apiClient.getApiService<ApiService>().putData(endpoint,deviceData)
    }

    suspend fun getDeviceDetails(orgId: String,deviceId: String): ApiResponse<DeviceDetail> {
        val endpoint = "api/ov/v1/organizations/$orgId/devices/$deviceId/details"
        return apiClient.getApiService<ApiService>().getData(endpoint)
    }
    // endregion

    // region Organization API
    suspend fun getAllOrganization(): ApiResponse<Organization> {
        val endpoint = "api/ov/v1/organizations"
        return apiClient.getApiService<ApiService>().getData(endpoint)
    }

    suspend fun getOrganizationSettings(orgId: String): ApiResponse<Organization> {
        val endpoint = "api/ov/v1/organizations/$orgId/settings/basic"
        return apiClient.getApiService<ApiService>().getData(endpoint)
    }

    suspend fun getOrganization(orgId: String): ApiResponse<Organization> {
        val endpoint = "api/ov/v1/organizations/$orgId"
        return apiClient.getApiService<ApiService>().getData(endpoint)
    }

    suspend fun updateOrganization(orgId: String, orgConfig: OrganizationConfig): ApiResponse<Organization> {
        val endpoint = "api/ov/v1/organizations/$orgId"
        return apiClient.getApiService<ApiService>().putData(endpoint,orgConfig)
    }
    suspend fun deleteOrganization(orgId: String): ApiResponse<String> {
        val endpoint = "api/ov/v1/organizations/$orgId"
        return apiClient.getApiService<ApiService>().deleteData(endpoint)
    }

    suspend fun getUsersInOrganization(orgId: String): ApiResponse<List<User>> {
        val endpoint = "api/ov/v1/organizations/$orgId/users"
        return apiClient.getApiService<ApiService>().getData(endpoint)
    }
    // endregion

    // region Site API
    suspend fun createASite(orgId: String, siteConfig: Site): ApiResponse<Site> {
        val endpoint = "api/ov/v1/$orgId/sites"
        return apiClient.getApiService<ApiService>().postData(endpoint,siteConfig)
    }

    suspend fun getOrgSites(orgId: String): ApiResponse<List<Site>> {
        val endpoint = "api/ov/v1/$orgId/sites"
        return apiClient.getApiService<ApiService>().getData(endpoint)
    }

    suspend fun getOrgSitesBuildingsFloors(orgId: String): ApiResponse<List<Site>> {
        val endpoint = "api/ov/v1/$orgId/sites/buildings/floors"
        return apiClient.getApiService<ApiService>().getData(endpoint)
    }

    suspend fun getASite(orgId: String, siteId: String): ApiResponse<Site> {
        val endpoint = "api/ov/v1/$orgId/sites/$siteId"
        return apiClient.getApiService<ApiService>().getData(endpoint)
    }

    suspend fun updateASite(orgId: String, siteId: String, siteConfig: Site): ApiResponse<Site> {
        val endpoint = "api/ov/v1/$orgId/sites/$siteId"
        return apiClient.getApiService<ApiService>().putData(endpoint,siteConfig)
    }

    suspend fun deleteASite(orgId: String, siteId: String): ApiResponse<Site> {
        val endpoint = "api/ov/v1/$orgId/sites/$siteId"
        return apiClient.getApiService<ApiService>().deleteData(endpoint)
    }

    // endregion

    // region SSID API
    suspend fun getALlSSIDs(orgId: String): ApiResponse<SSID> {
        val endpoint = "api/ov/v1/$orgId/wlan/ssids"
        return apiClient.getApiService<ApiService>().getData(endpoint)
    }

    suspend fun createSSID(orgId: String, ssidConfig: SSID): ApiResponse<SSID> {
        val endpoint = "api/ov/v1/$orgId/wlan/ssids"
        return apiClient.getApiService<ApiService>().postData(endpoint,ssidConfig)
    }

    suspend fun updateSSID(orgId: String, ssidConfig: SSID): ApiResponse<SSID> {
        val endpoint = "api/ov/v1/$orgId/wlan/ssids"
        return apiClient.getApiService<ApiService>().putData(endpoint,ssidConfig)
    }

    suspend fun getSSIDsByGroup(orgId: String, siteId: String, groupId: String): ApiResponse<Int> {
        val endpoint = "api/ov/v1/$orgId/site/$siteId/groups/$groupId/wlan/ssids/count"
        return apiClient.getApiService<ApiService>().getData(endpoint)
    }

    // TODO correct the response type
    suspend fun getSSIDsByName(orgId: String, name: String): ApiResponse<SSID> {
        val endpoint = "api/ov/v1/$orgId/wlan/ssids/name/$name"
        return apiClient.getApiService<ApiService>().getData(endpoint)
    }

    suspend fun deleteSSID(orgId: String, names: String): ApiResponse<List<SSIDResponse>> {
        val endpoint = "api/ov/v1/$orgId/wlan/ssids"
        return apiClient.getApiService<ApiService>().deleteDataWithBody(endpoint, names)
    }

    // endregion

}

