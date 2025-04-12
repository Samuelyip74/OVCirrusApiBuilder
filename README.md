#  OmniVista Cirrus API Builder - OVCApiBuilder

**OVCApiBuilder** is an Android library that lets developers interact with OmniVista Cirrus Restful API

---

## ✨ API implemented
Complete API can be found here (https://eu.manage.ovcirrus.com/apidoc/apidoc.html)

- Authentication API
- User API
- Device API
- Organization API
- Site API


---

## 🛠️ Built With

- **Kotlin**
- **Retrofit** – To make API calls
- **Gson convertor** – to convert JSON to Kotlin objects
- **Logging Interceptor Database** – for logging http raw data

---

## 🚀 Installation

> This app is not available on the Play Store.

To install:

1. Download ovcirrusapi-x.x.x.aar library and import into libs/ folder
2. Add implementation implementation(files("libs/ovcirrusapi-x.x.x.aar")) in build.gradle.kts
3. Sync gradle

---
## 📦 How to use the library

Step 1: In MainActivity or My Application, declare an object of OVCirrusApiBuilder

    class MainActivity : AppCompatActivity() {

        // declare the OVCirrusApiBuilder object and name as apiClient
        private lateinit var apiClient: OVCirrusApiClient

Step 2: Initialize the builder

    val apiClient = OVCirrusApiBuilder.initialize(context).apply {
        setEmail("email")
        setPassword("password")
        setAppId("appId")
        setAppSecret("appSecret")
        setBaseUrl("baseUrl eg. https://eu.manage.ovcirrus.com/")
    }.build()  

Step 3: Use the object to make API calls

    GlobalScope.launch(Dispatchers.Main) {
        try {

            val result = apiClient.getUserProfile<UserProfile>()
            if (result.status == 200 && result.data != null) {
                Log.d("API", "API Success: ${result.data}")
            } else {
                Log.e("API", "API Error: ${result.errorMsg} - ${result.errorMsg}")
            }

        } catch (e: Exception) {
            Log.e("API", "Error during initialization: ${e.message}")
        }
    }

Note: Calling OVCirrusApiBuilder instance from other fragments or activities.

Step 1: Get the instance

    val apiClient = OVCirrusApiBuilder.getInstance()

Step 2: Use the instance to make API calls

    GlobalScope.launch(Dispatchers.Main) {
        try {

            val result = apiClient.getUserProfile<UserProfile>()
            if (result.status == 200 && result.data != null) {
                Log.d("API", "API Success: ${result.data}")
            } else {
                Log.e("API", "API Error: ${result.errorMsg} - ${result.errorMsg}")
            }

        } catch (e: Exception) {
            Log.e("API", "Error during initialization: ${e.message}")
        }
    }

Note: Library must be use with coroutines.

---
## Try the demo app

Step 1: Clone the git repository to your device

Step 2: Open the project with Android Studio

Step 3: Build and run the app on your physical device

Step 4: Create a Text QR code with the following parameters

    {
        "email"         :       "<email>"
        "password"      :       "<password>"
        "appId"         :       "<appId>"
        "appSecret"     :       "<appSecret>"
        "apiBaseUrl"    :       "<apiBaseUrl>"
    }

Step 5: Scan the QR code with the camera on your device

Step 6. Click Login button

---
## 📦 Methods

| Methods                       | Function                                     | Remarks                                                                                                                                    |
|-------------------------------|----------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------|
| authenticate                  | Perform API authentication to retrieve token | https://docs.ovcirrus.com/ov/authentication-api                                                                                            |
| getUserProfile                | Get User Profile                             | https://eu.manage.ovcirrus.com/apidoc/apidoc.html#tag/User/paths/~1ov~1v1~1user~1profile/get                                               |
| updateUserProfile             | Update User Profile                          | https://eu.manage.ovcirrus.com/apidoc/apidoc.html#tag/User/paths/~1ov~1v1~1user~1profile/put                                               |
| createADevice                 | Create a device                              | https://eu.manage.ovcirrus.com/apidoc/apidoc.html#tag/Device/paths/~1ov~1v1~1organizations~1%7BorgId%7D~1sites~1%7BsiteId%7D~1devices/post |
| getAllDevices                 | Get all devices declared in a site           | https://eu.manage.ovcirrus.com/apidoc/apidoc.html#tag/Device/paths/~1ov~1v1~1organizations~1%7BorgId%7D~1sites~1%7BsiteId%7D~1devices/get  |                                                                                                                                           
| getAllDevicesFromOrganization | Get all devices declared in a organization   | https://eu.manage.ovcirrus.com/apidoc/apidoc.html#tag/Device/paths/~1ov~1v1~1organizations~1%7BorgId%7D~1sites~1devices/get                |
|                               |                                              |                                                                                                                                            |
    

---
## 📦 Releases

| Version          | Date       | Notes           |
|------------------|------------|-----------------|
| v10.4.3 Build 03 | 2025-04-10 | Initial release |
|                  |            |                 |


Check the [Releases](https://github.com/Samuelyip74/OVCirrusApiBuilder/releases/tag/10.4.3) tab for `.zip` downloads.

---

## 📄 License

```text
Copyright (c) [Samuel Yip Kah Yean] 2025

This software is licensed for personal, non-commercial use only.

You are NOT permitted to:
- Use this software for any commercial purposes.
- Modify, adapt, reverse-engineer, or create derivative works based on this software.
- Distribute, sublicense, or share this software in any form.

All rights are reserved by the author.

For commercial licensing inquiries or permissions, please contact: [kahyean.yip@gmail.com]

