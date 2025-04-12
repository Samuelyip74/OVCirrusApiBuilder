package com.example.demo.ui.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.al_enterprise.OVCirrusApiBuilder
import com.al_enterprise.dataclasses.ApiResponse
import com.al_enterprise.dataclasses.UserProfile
import com.example.demo.LoginActivity

import com.example.demo.R
import com.example.demo.utils.formatDate
import com.google.gson.Gson
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ProfileFragment : Fragment(), GestureDetector.OnGestureListener {

    private lateinit var profileCard: CardView
    private lateinit var firstnameTextView: TextView
    private lateinit var lastnameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var countryTextView: TextView
    private lateinit var lastLoginTextView: TextView
    private lateinit var twoFAEnabledTextView: TextView
    private lateinit var gestureDetector: GestureDetector
    private lateinit var company: String
    private lateinit var lastLoginDate: String
    private var verified: Boolean = false
    private lateinit var closestRegion: String
    private lateinit var address: String
    private lateinit var zipCode: String
    private lateinit var city: String
    private lateinit var phoneNumber: String
    private lateinit var preferredLanguage: String
    private var isTwoFAEnabled: Boolean = false
    private lateinit var faMethod: String

    private lateinit var apiClient: OVCirrusApiBuilder

    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        apiClient = OVCirrusApiBuilder.getInstance()

                // Bind views
        profileCard = view.findViewById(R.id.profileCard)
        firstnameTextView = view.findViewById(R.id.tvFirstName)
        lastnameTextView = view.findViewById(R.id.tvLastName)
        emailTextView = view.findViewById(R.id.tvEmail)
        countryTextView = view.findViewById(R.id.tvCountry)
        lastLoginTextView = view.findViewById(R.id.tvLastLogin)
        twoFAEnabledTextView = view.findViewById(R.id.tvTwoFAStatus)

        // Initialize gesture detector
        gestureDetector = GestureDetector(requireContext(), this)
        profileCard.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            true
        }

        // Load user data and display
        loadUserData()

        GlobalScope.launch(Dispatchers.Main) {
            if (apiClient.authenticate()) {
                // If logged in, show main content
                loadUserData()
            } else {
                // If not logged in, redirect to LoginActivity
                val intent = Intent(requireActivity(), LoginActivity::class.java)
                startActivity(intent)
                requireActivity().finish() // Close MainActivity so the user can't go back
            }
        }
        return view
    }

    @SuppressLint("SetTextI18n")
    private fun loadUserData() {
        // Simulate loading user data. This should ideally come from the API response.
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                // Get user auth setting from API
                val response = apiClient.getUserProfile()
                val userProfile = Gson().fromJson(Gson().toJson(response.data), UserProfile::class.java)

                if (response.status == 200) {
                     withContext(Dispatchers.Main) {
                        firstnameTextView.text = userProfile.firstname
                        lastnameTextView.text = userProfile.lastname
                        emailTextView.text = userProfile.email
                        countryTextView.text = userProfile.country
                        lastLoginTextView.text = userProfile.lastLoginDate?.let { formatDate(it) }
                        twoFAEnabledTextView.text =
                            if (userProfile.isTwoFAEnabled == true) "Yes" else "No"
                        company = userProfile.companyName.toString()
                        lastLoginDate = userProfile.lastLoginDate.toString()
                        verified = userProfile.verified == true
                        closestRegion = userProfile.closestRegion.toString()
                        address = userProfile.address.toString()
                        zipCode = userProfile.zipCode.toString()
                        city = userProfile.city.toString()
                        phoneNumber = userProfile.phoneNumber.toString()
                        preferredLanguage = userProfile.preferredLanguage.toString()
                        isTwoFAEnabled = userProfile.isTwoFAEnabled == true
                        faMethod = userProfile.faMethod.toString()
                        //isSuperAdmin = response.data.isSuperAdmin ?: false
                    }
                    //TODO
                } else {
                    // TODO

                }
            } catch (e:Exception){
                // TODO
            }
        }
    }

    // Function to show a dialog or EditText for editing profile
    private fun showEditDialog() {
        // Inflate custom layout with EditTexts for each profile field
        val layout = layoutInflater.inflate(R.layout.dialog_edit_profile, null)

        // Get references to the EditText fields
        val editFirstName = layout.findViewById<EditText>(R.id.editFirstName)
        val editLastName = layout.findViewById<EditText>(R.id.editLastName)
        val editEmail = layout.findViewById<EditText>(R.id.editEmail)
        val editCountry = layout.findViewById<EditText>(R.id.editCountry)

        // Pre-fill the EditText fields with the current profile information
        editFirstName.setText(firstnameTextView.text)
        editLastName.setText(lastnameTextView.text)
        editEmail.setText(emailTextView.text)
        editCountry.setText(countryTextView.text)

        // Create the AlertDialog builder
        val builder = AlertDialog.Builder(requireContext())
            .setTitle("Edit Profile")
            .setView(layout)
            .setPositiveButton("Save") { _, _ ->
                // Save the updated values when the user clicks "Save"
                firstnameTextView.text = editFirstName.text
                lastnameTextView.text = editLastName.text
                emailTextView.text = editEmail.text
                countryTextView.text = editCountry.text

                // Update the profile on the server by calling an API here
                val userRequest = UserProfile(
                    firstname = editFirstName.text.toString(),
                    lastname = editLastName.text.toString(),
                    email = editEmail.text.toString(),
                    country = editCountry.text.toString(),
                    companyName = company,
                    closestRegion = closestRegion,
//                    isSuperAdmin = isSuperAdmin,
                )
                lifecycleScope.launch(Dispatchers.IO) {
                    try {
                        val response = apiClient.updateUserProfile(userProfile = userRequest)
                        if (response.status == 200) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    requireContext(),
                                    "Profile updated successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    requireContext(),
                                    "Failed to update profile",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } catch (e : Exception){
                        // TODO
                    }
                }

            }
            .setNegativeButton("Cancel", null)

        builder.show()
    }


    override fun onDown(e: MotionEvent): Boolean {
        return true
    }

    override fun onShowPress(e: MotionEvent) {
        TODO("Not yet implemented")
    }

    override fun onSingleTapUp(e: MotionEvent): Boolean {
        return true
    }

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        return true
    }

    override fun onLongPress(e: MotionEvent) {
        TODO("Not yet implemented")
    }

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        if (e1 != null && e2 != null) {
            if (e1.x - e2.x > 200) {
                // Swipe-right detected, trigger the edit functionality
                showEditDialog()
            }
        }
        return true
    }

}
