package com.example.demo

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.al_enterprise.OVCirrusApiBuilder
import com.al_enterprise.dataclasses.Organization
import com.example.demo.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set up the Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigation_drawer)
        bottomNavigationView = findViewById(R.id.bottom_nav)

        // Get the NavController from the NavHostFragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Set up the navigation drawer with the NavController
        NavigationUI.setupWithNavController(navigationView, navController)

        // Set up Bottom Navigation with the NavController
        NavigationUI.setupWithNavController(bottomNavigationView, navController)

        // Handle navigation item clicks in the Navigation Drawer
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_dashboard -> {
                    navController.navigate(R.id.dashboardFragment)
                }
                R.id.nav_alarm -> {
                    navController.navigate(R.id.alarmFragment)
                }
                R.id.nav_profile -> {
                    navController.navigate(R.id.profileFragment)
                }
                R.id.nav_logout -> {
                    logout()
                }
            }
            // Close the navigation drawer after an item is selected
            drawerLayout.closeDrawers()
            true
        }

        // Handle bottom navigation item clicks
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_dashboard -> {
                    navController.navigate(R.id.dashboardFragment)
                }
                R.id.nav_alarm -> {
                    navController.navigate(R.id.alarmFragment)
                }
                R.id.nav_profile -> {
                    navController.navigate(R.id.profileFragment)
                }
            }
            true
        }

        // Enable the Drawer Toggle (Hamburger icon)
        val drawerToggle = androidx.appcompat.app.ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer
        )
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()  // Sync the state of the drawer toggle


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

    private fun logout() {}
    // TODO
}

