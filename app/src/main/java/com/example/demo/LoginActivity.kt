package com.example.demo

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.al_enterprise.OVCirrusApiBuilder
import com.google.gson.Gson
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var scanQrButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var apiClient: OVCirrusApiBuilder


    private var email: String? = null
    private var password: String? = null
    private var appId: String? = "671f13d3e0748137d6fc5a27"
    private var appSecret: String? = "db0553664df3a0c7f86986619748096dab6d1b58a91f1be9dffd093e50426280"
    private var apiBaseUrl: String? = "https://eu.manage.ovcirrus.com/"

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPreferences = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        scanQrButton = findViewById(R.id.scanQrButton)
        progressBar = findViewById(R.id.progress_bar)


        GlobalScope.launch(Dispatchers.Main) {
            checkStoredCredentials()
        }


        // Login action
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if(appId != null && appSecret != null && apiBaseUrl != null) {

                if (email.isNotEmpty() && password.isNotEmpty()) {

                    fadeOutLoginForm()
                    // Show the spinner (ProgressBar)
                    progressBar.visibility = ProgressBar.VISIBLE

                    GlobalScope.launch(Dispatchers.Main) {
                        apiClient = OVCirrusApiBuilder.initialize(this@LoginActivity).apply {
                            setEmail(email)
                            setPassword(password)
                            setAppId(appId!!)
                            setAppSecret(appSecret!!)
                            setBaseUrl(apiBaseUrl!!)
                        }.build()

                        if(apiClient.authenticate()){
                            // Hide the spinner after the login attempt
                            progressBar.visibility = ProgressBar.GONE
                            Log.d("Login", "Login successful.")
                            // Navigate to MainActivity on successful login
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish() // Close LoginActivity
                        }
                        else {
                            unfadeLoginForm()
                            Log.e("Login", "Login failed.")
                            Toast.makeText(this@LoginActivity, "Login failed. Please check your credentials.", Toast.LENGTH_SHORT).show()
                        }

                    }
                } else {
                    Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(this, "Missing credentials. Please Scan QR code to begin", Toast.LENGTH_SHORT).show()
            }
        }

        // QR Code scan action
        scanQrButton.setOnClickListener {
            val integrator = IntentIntegrator(this)
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            integrator.setPrompt("Scan QR Code")
            integrator.setOrientationLocked(true)
            integrator.setCameraId(0)
            integrator.setBeepEnabled(true)
            integrator.initiateScan()
        }
    }

    // Handle QR code scan result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: android.content.Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Scan cancelled", Toast.LENGTH_SHORT).show()
            } else {
                // Parse the QR code data and auto-populate fields
                handleQRCodeData(result.contents)
            }
        }
    }

    private fun handleQRCodeData(qrCodeData: String) {
        try {
            // Parse the JSON string into a data class
            val userData = Gson().fromJson(qrCodeData, UserData::class.java)

            // Check if any required data is null
            if (userData.email.isNullOrEmpty() || userData.password.isNullOrEmpty() ||
                userData.appId.isNullOrEmpty() || userData.appSecret.isNullOrEmpty() || userData.apiBaseUrl.isNullOrEmpty()) {
                Toast.makeText(this, "Invalid QR code data.", Toast.LENGTH_SHORT).show()
                return
            }

            // Auto-fill email and password
            runOnUiThread {
                emailEditText.setText(userData.email)
                passwordEditText.setText(userData.password)
                email = userData.email
                password = userData.password
                appId = userData.appId
                appSecret = userData.appSecret
                apiBaseUrl = userData.apiBaseUrl

                Log.d("handleQRCodeData", "UserData: $email, $password, $appId, $appSecret, $apiBaseUrl")
            }

            // Store the credentials in storage
            sharedPreferences.edit().putString("EMAIL", email).apply()
            sharedPreferences.edit().putString("PASSWORD", password).apply()
            sharedPreferences.edit().putString("APP_ID", appId).apply()
            sharedPreferences.edit().putString("APP_SECRET", appSecret).apply()
            sharedPreferences.edit().putString("API_BASE_URL", apiBaseUrl).apply()

        } catch (e: Exception) {
            Toast.makeText(this, "Error parsing QR code data.", Toast.LENGTH_SHORT).show()
        }
    }


    private suspend fun checkStoredCredentials() {

        email = sharedPreferences.getString("EMAIL", null)
        password = sharedPreferences.getString("PASSWORD", null)
        appId = sharedPreferences.getString("APP_ID", null)
        appSecret = sharedPreferences.getString("APP_SECRET", null)
        apiBaseUrl = sharedPreferences.getString("API_BASE_URL", null)


        if (email != null && password!= null && appId != null && appSecret != null && apiBaseUrl != null) {

            fadeOutLoginForm()
            progressBar.visibility = ProgressBar.VISIBLE

            apiClient = OVCirrusApiBuilder.initialize(this).apply {
                setEmail(email!!)
                setPassword(password!!)
                setAppId(appId!!)
                setAppSecret(appSecret!!)
                setBaseUrl(apiBaseUrl!!)
            }.build()

            if(apiClient.authenticate()){
                // Navigate to MainActivity
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                finish()

            } else {
                unfadeLoginForm()
                // Show login failed message
                Toast.makeText(this@LoginActivity, "Login failed.", Toast.LENGTH_SHORT).show()
            }
        } else {
            // No stored credentials found, show the login form
            Toast.makeText(this, "No stored credentials found. Please Scan QR code to begin", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fadeOutLoginForm() {
        // Fade out the login form (email, password, and button)
        emailEditText.visibility = EditText.GONE
        passwordEditText.visibility = EditText.GONE
        loginButton.visibility = Button.GONE
        scanQrButton.visibility = Button.GONE
    }

    private fun unfadeLoginForm() {
        emailEditText.visibility = EditText.VISIBLE
        passwordEditText.visibility = EditText.VISIBLE
        loginButton.visibility = Button.VISIBLE
        scanQrButton.visibility = Button.VISIBLE
    }
}

data class UserData(
    val email: String?,
    val password: String?,
    val appId: String?,
    val appSecret: String?,
    val apiBaseUrl: String?
)