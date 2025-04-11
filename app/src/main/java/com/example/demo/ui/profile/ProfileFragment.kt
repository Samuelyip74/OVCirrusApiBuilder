package com.example.demo.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.al_enterprise.OVCirrusApiBuilder
import com.al_enterprise.dataclasses.Organization
import com.al_enterprise.dataclasses.UserProfile
import com.example.demo.MainActivity
import com.example.demo.R
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileFragment : Fragment() {


    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val apiClient = OVCirrusApiBuilder.getInstance()


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

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        // Inflate the fragment_alarm layout
        return view
    }
}