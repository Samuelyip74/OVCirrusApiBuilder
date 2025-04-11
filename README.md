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

Method 1:
1. Download .aar library and import into Android Studio
2. Add implementation (project(':ovcirrusapi')) in gradle
3. Sync gradle

Method 2:
1. Add "maven( url = "https://jitpack.io")" to repositories
2. Add implementation("com.al_enterprise.ovcirrusapi:10.4.3"))
3. Sync gradle

---
## 📦 How to use the library

Step 1: Initialize the builder

    val apiClient = OVCirrusApiBuilder(context).apply {
        setEmail("email")
        setPassword("password")
        setAppId("appId")
        setAppSecret("appSecret")
        setBaseUrl("baseUrl eg. https://eu.manage.ovcirrus.com/")
    }.build()  

Step 2: Use the builder to make API calls


    val result = apiClient.getUsersInOrganization<Organization>("orgId")
    if (result.status == 200 && result.data != null) {
        Log.d("API", "API Success: ${result.data}")
    } else {
        Log.e("API", "API Error: ${result.errorMsg} - ${result.errorMsg}")
    }

Note: Library must be use with coroutines.

---
## 📦 Releases

| Version          | Date       | Notes           |
|------------------|------------|-----------------|
| v10.4.3 Build 03 | 2025-04-10 | Initial release |
|                  |            |                 |


Check the [Releases](#) tab for `.zip` downloads.

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

