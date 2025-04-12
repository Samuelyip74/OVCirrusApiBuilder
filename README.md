
# OmniVista Cirrus API Builder - **OVCApiBuilder**

**OVCirrusApiBuilder** is a lightweight Android library that enables developers to interact seamlessly with the OmniVista Cirrus RESTful API.

---

## ✨ Supported APIs

The full API documentation is available at: [OmniVista Cirrus API Docs](https://eu.manage.ovcirrus.com/apidoc/apidoc.html)

- Authentication API
- User API
- Device API
- Organization API
- Site API

---

## 🛠️ Built With

- **Kotlin**
- **Retrofit** – for API calls
- **Gson Converter** – for converting JSON to Kotlin objects
- **Logging Interceptor** – for logging raw HTTP data

---

## 🚀 Installation

> This library is not available on the Google Play Store.

To install manually:

1. Download the `ovcirrusapi-x.x.x.aar` file and place it in the `libs/` folder of your project.
2. Add the following to your `build.gradle.kts`:
   ```kotlin
   implementation(files("libs/ovcirrusapi-x.x.x.aar"))
   ```
3. Sync your Gradle files.

---

## 📦 Usage Guide

### Step 1: Declare the API client

In your `MainActivity` or `Application` class:

```kotlin
private lateinit var apiClient: OVCirrusApiClient
```

### Step 2: Initialize the client

```kotlin
val apiClient = OVCirrusApiBuilder.initialize(context).apply {
    setEmail("your_email")
    setPassword("your_password")
    setAppId("your_app_id")
    setAppSecret("your_app_secret")
    setBaseUrl("https://eu.manage.ovcirrus.com/")
}.build()
```

### Step 3: Make API calls

```kotlin
GlobalScope.launch(Dispatchers.Main) {
    try {
        val result = apiClient.getUserProfile<UserProfile>()
        val userProfile = Gson().fromJson(Gson().toJson(result.data), UserProfile::class.java)

       if (result.status == 200 && result.data != null) {
            Log.d("API", "Success: ${userProfile.data}")
        } else {
            Log.e("API", "Error: ${result.errorMsg}")
        }
    } catch (e: Exception) {
        Log.e("API", "Initialization error: ${e.message}")
    }
}
```

---

### Accessing the API Client in Other Activities or Fragments

#### Step 1: Retrieve the instance

```kotlin
val apiClient = OVCirrusApiBuilder.getInstance()
```

#### Step 2: Make API calls (same as above)

> **Note**: This library requires usage with Kotlin coroutines.

---

## 🧪 Try the Demo App

1. Clone the repository to your local device.
2. Open the project using Android Studio.
3. Build and run the app on a physical device.
4. Generate a **Text QR Code** with the following format:
   ```json
   {
       "email": "<your_email>",
       "password": "<your_password>",
       "appId": "<your_app_id>",
       "appSecret": "<your_app_secret>",
       "apiBaseUrl": "<your_api_base_url>"
   }
   ```
5. Scan the QR code with your device's camera.
6. Tap the **Login** button to authenticate.

   View the demo [here](https://www.youtube.com/shorts/Fq7wqiczGAw)


---

## 📚 Available Methods

| Method                        | Description                                      | Documentation Link                                                                                                                                      |
|------------------------------|--------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------|
| `authenticate`               | Authenticates and retrieves a token              | [Authentication API](https://docs.ovcirrus.com/ov/authentication-api)                                                                                   |
| `getUserProfile`             | Retrieves the user profile                       | [User Profile - GET](https://eu.manage.ovcirrus.com/apidoc/apidoc.html#tag/User/paths/~1ov~1v1~1user~1profile/get)                                      |
| `updateUserProfile`          | Updates the user profile                         | [User Profile - PUT](https://eu.manage.ovcirrus.com/apidoc/apidoc.html#tag/User/paths/~1ov~1v1~1user~1profile/put)                                      |
| `createADevice`              | Creates a device under a site                    | [Create Device](https://eu.manage.ovcirrus.com/apidoc/apidoc.html#tag/Device/paths/~1ov~1v1~1organizations~1%7BorgId%7D~1sites~1%7BsiteId%7D~1devices/post) |
| `getAllDevices`              | Gets all devices within a specific site          | [Site Devices](https://eu.manage.ovcirrus.com/apidoc/apidoc.html#tag/Device/paths/~1ov~1v1~1organizations~1%7BorgId%7D~1sites~1%7BsiteId%7D~1devices/get) |
| `getAllDevicesFromOrganization` | Retrieves all devices across an organization   | [Organization Devices](https://eu.manage.ovcirrus.com/apidoc/apidoc.html#tag/Device/paths/~1ov~1v1~1organizations~1%7BorgId%7D~1sites~1devices/get)        |

---

## 📦 Releases

| Version          | Date       | Notes           |
|------------------|------------|-----------------|
| v10.4.3 Build 03 | 2025-04-10 | Initial release |

Visit the [Releases](https://github.com/Samuelyip74/OVCirrusApiBuilder/releases/tag/10.4.3) page to download `.zip` packages.

---

## 📄 License

```
Copyright (c) Samuel Yip Kah Yean 2025

This software is licensed for personal, non-commercial use only.

You are NOT permitted to:
- Use this software for any commercial purposes.
- Modify, adapt, reverse-engineer, or create derivative works.
- Distribute, sublicense, or share this software.

All rights are reserved by the author.

For commercial licensing or permission inquiries, please contact:
kahyean.yip@gmail.com
```


