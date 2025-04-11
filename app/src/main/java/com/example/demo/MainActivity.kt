package com.example.demo

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.al_enterprise.OVCirrusApiBuilder
import com.al_enterprise.dataclasses.Organization
import com.example.demo.databinding.ActivityMainBinding
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        apiTest()

    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun apiTest() {

        // In MainActivity
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val apiClient = OVCirrusApiBuilder(this@MainActivity).apply {
                    setEmail("kahyean.yip@gmail.com")
                    setPassword("Ciscotac%2688")
                    setAppId("671f13d3e0748137d6fc5a27")
                    setAppSecret("db0553664df3a0c7f86986619748096dab6d1b58a91f1be9dffd093e50426280")
                    setBaseUrl("https://eu.manage.ovcirrus.com/")
                }.build()  // This will ensure authentication is completed


                val result = apiClient.getUsersInOrganization<Organization>("632a9823803a31ad755226ee")
                if (result.status == 200 && result.data != null) {
                    Log.d("API", "API Success: ${result.data}")
                } else {
                    Log.e("API", "API Error: ${result.errorMsg} - ${result.errorMsg}")
                }

            } catch (e: Exception) {
                Log.e("API", "Error during initialization: ${e.message}")
            }
        }


    }
}

