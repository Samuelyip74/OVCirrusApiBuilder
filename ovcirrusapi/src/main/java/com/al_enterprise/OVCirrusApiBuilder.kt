package com.al_enterprise

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
        fun newInstance(context: Context): OVCirrusApiBuilder {
            return OVCirrusApiBuilder(context)
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

        apiClient = ApiClient(baseUrl!!, email, password, appId, appSecret, tokenProvider)

        val isAuthenticated = authenticate()

        if (!isAuthenticated) {
            throw Exception("Authentication failed")
        }

        return this
    }

    private suspend fun authenticate(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val isAuthenticated = apiClient.authenticate()
                if (isAuthenticated) {
                    Log.d("OVCApiBuilder", "Authentication successful.")
                } else {
                    Log.e("OVCApiBuilder", "Authentication failed.")
                }
                isAuthenticated
            } catch (e: Exception) {
                Log.e("OVCApiBuilder", "Error during authentication: ${e.message}")
                false
            }
        }
    }

    // region User API methods
    suspend fun <T> getUserProfile(): ApiResponse<UserProfile> {
        val endpoint: String = "api/ov/v1/user/profile"
        return apiClient.getApiService<ApiService>().getData(endpoint)
    }

    suspend fun <T> updateUserProfile(userProfile: UserProfile): ApiResponse<UserProfile> {
        val endpoint: String = "api/ov/v1/user/profile"
        return apiClient.getApiService<ApiService>().putData(endpoint,userProfile)
    }

    // endregion

    // region Device API methods
    suspend fun <T> getDeviceById(orgId: String, deviceId: String): ApiResponse<DeviceData> {
        val endpoint: String = "api/ov/v1/organizations/$orgId/devices/$deviceId"
        return apiClient.getApiService<ApiService>().getData(endpoint)
    }

    suspend fun <T> updateADevice(orgId: String, siteId: String, deviceId: String, deviceData: DeviceData): ApiResponse<DeviceData> {
        val endpoint: String = "api/ov/v1/organizations/$orgId/sites/$siteId/devices/$deviceId"
        return apiClient.getApiService<ApiService>().putData(endpoint,deviceData)
    }

    suspend fun <T> deleteADevice(orgId: String, siteId: String, deviceId: String): ApiResponse<String> {
        val endpoint: String = "api/ov/v1/organizations/$orgId/sites/$siteId/devices/$deviceId"
        return apiClient.getApiService<ApiService>().deleteData(endpoint)
    }

    suspend fun <T> updateRemoteAP(orgId: String, siteId: String, deviceId: String, deviceData: DeviceData): ApiResponse<DeviceData> {
        val endpoint: String = "api/ov/v1/organizations/$orgId/sites/$siteId/remote-aps/$deviceId"
        return apiClient.getApiService<ApiService>().putData(endpoint,deviceData)
    }

    // This method specifies that you want the response to be of type `UserProfile`
    suspend fun <T> getAllDevices(orgId: String, siteId: String): ApiResponse<List<DeviceDetail>> {
        val endpoint: String = "api/ov/v1/organizations/$orgId/sites/$siteId/devices"
        return apiClient.getApiService<ApiService>().getData(endpoint)
    }

    suspend fun <T> getAllDevicesFromOrganization(orgId: String): ApiResponse<List<DeviceDetail>> {
        val endpoint: String = "api/ov/v1/organizations/$orgId/sites/devices"
        return apiClient.getApiService<ApiService>().getData(endpoint)
    }

    suspend fun <T> getDeviceDetails(orgId: String,deviceId: String): ApiResponse<DeviceDetail> {
        val endpoint: String = "api/ov/v1/organizations/$orgId/devices/$deviceId/details"
        return apiClient.getApiService<ApiService>().getData(endpoint)
    }
    // endregion

    // region Organization API
    suspend fun <T> getAllOrganization(): ApiResponse<Organization> {
        val endpoint: String = "api/ov/v1/organizations"
        return apiClient.getApiService<ApiService>().getData(endpoint)
    }

    suspend fun <T> getOrganizationSettings(orgId: String): ApiResponse<Organization> {
        val endpoint: String = "api/ov/v1/organizations/$orgId/settings/basic"
        return apiClient.getApiService<ApiService>().getData(endpoint)
    }

    suspend fun <T> getOrganization(orgId: String): ApiResponse<Organization> {
        val endpoint: String = "api/ov/v1/organizations/$orgId"
        return apiClient.getApiService<ApiService>().getData(endpoint)
    }

    suspend fun <T> updateOrganization(orgId: String, orgConfig: OrganizationConfig): ApiResponse<Organization> {
        val endpoint: String = "api/ov/v1/organizations/$orgId"
        return apiClient.getApiService<ApiService>().putData(endpoint,orgConfig)
    }
    suspend fun <T> deleteOrganization(orgId: String): ApiResponse<String> {
        val endpoint: String = "api/ov/v1/organizations/$orgId"
        return apiClient.getApiService<ApiService>().deleteData(endpoint)
    }

    suspend fun <T> getUsersInOrganization(orgId: String): ApiResponse<List<User>> {
        val endpoint: String = "api/ov/v1/organizations/$orgId/users"
        return apiClient.getApiService<ApiService>().getData(endpoint)
    }
    // endregion

    // region Site API
    suspend fun <T> createASite(orgId: String, siteConfig: Site): ApiResponse<Site> {
        val endpoint: String = "api/ov/v1/$orgId/sites"
        return apiClient.getApiService<ApiService>().postData(endpoint,siteConfig)
    }

    suspend fun <T> getOrgSites(orgId: String): ApiResponse<List<Site>> {
        val endpoint: String = "api/ov/v1/$orgId/sites"
        return apiClient.getApiService<ApiService>().getData(endpoint)
    }

    suspend fun <T> getOrgSitesBuildingsFloors(orgId: String): ApiResponse<List<Site>> {
        val endpoint: String = "api/ov/v1/$orgId/sites/buildings/floors"
        return apiClient.getApiService<ApiService>().getData(endpoint)
    }

    suspend fun <T> getASite(orgId: String, siteId: String): ApiResponse<Site> {
        val endpoint: String = "api/ov/v1/$orgId/sites/$siteId"
        return apiClient.getApiService<ApiService>().getData(endpoint)
    }

    suspend fun <T> updateASite(orgId: String, siteId: String, siteConfig: Site): ApiResponse<Site> {
        val endpoint: String = "api/ov/v1/$orgId/sites/$siteId"
        return apiClient.getApiService<ApiService>().putData(endpoint,siteConfig)
    }

    suspend fun <T> deleteASite(orgId: String, siteId: String): ApiResponse<Site> {
        val endpoint: String = "api/ov/v1/$orgId/sites/$siteId"
        return apiClient.getApiService<ApiService>().deleteData(endpoint)
    }

    // endregion

    // region SSID API
    suspend fun <T> getALlSSIDs(orgId: String): ApiResponse<SSID> {
        val endpoint: String = "api/ov/v1/$orgId/wlan/ssids"
        return apiClient.getApiService<ApiService>().getData(endpoint)
    }

    suspend fun <T> createSSID(orgId: String, ssidConfig: SSID): ApiResponse<SSID> {
        val endpoint: String = "api/ov/v1/$orgId/wlan/ssids"
        return apiClient.getApiService<ApiService>().postData(endpoint,ssidConfig)
    }

    suspend fun <T> updateSSID(orgId: String, ssidConfig: SSID): ApiResponse<SSID> {
        val endpoint: String = "api/ov/v1/$orgId/wlan/ssids"
        return apiClient.getApiService<ApiService>().putData(endpoint,ssidConfig)
    }

    suspend fun <T> getSSIDsByGroup(orgId: String, siteId: String, groupId: String): ApiResponse<Int> {
        val endpoint: String = "api/ov/v1/$orgId/site/$siteId/groups/$groupId/wlan/ssids/count"
        return apiClient.getApiService<ApiService>().getData(endpoint)
    }

    // TODO correct the response type
    suspend fun <T> getSSIDsByName(orgId: String, name: String): ApiResponse<SSID> {
        val endpoint: String = "api/ov/v1/$orgId/wlan/ssids/name/$name"
        return apiClient.getApiService<ApiService>().getData(endpoint)
    }

    suspend fun <T> deleteSSID(orgId: String, names: String): ApiResponse<List<SSIDResponse>> {
        val endpoint: String = "api/ov/v1/$orgId/wlan/ssids"
        return apiClient.getApiService<ApiService>().deleteDataWithBody(endpoint, names)
    }

    // endregion

}

