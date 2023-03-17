package com.scammer101.Virya.Screens

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.scammer101.Virya.Utilities.ConstantsValues
import com.scammer101.Virya.Utilities.DateTimeOnline
import com.scammer101.Virya.Utilities.PreferenceManager
import com.scammer101.Virya.databinding.ActivityCustomGoalScreenBinding
import com.scammer101.Virya.databinding.FragmentProfileBinding

class CustomGoalScreen : AppCompatActivity() {

    private lateinit var binding: ActivityCustomGoalScreenBinding
    private var preferenceManager: PreferenceManager? = null
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomGoalScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        setListener()

    }


    private fun init()
    {
        setStatusBarColor(Color.parseColor("#ffffff"))
        preferenceManager = PreferenceManager(applicationContext)
        firestore = FirebaseFirestore.getInstance()
        //getDateOnline()
    }

    private fun setListener()
    {
        binding.setGoalButton.setOnClickListener { setGoal() }
    }


    private fun setGoal()
    {
        val map = HashMap<String, Any>()
        val date = preferenceManager!!.getString(ConstantsValues.KEY_DATE_ONLY)
        map["date"] = date
        map["name"] = binding.customGoalYogaName.text.toString()
        map["repeat"] = binding.customGoalCount.text.toString()
        map["timer"] = 0
        map["dateAndUser"] = date+FirebaseAuth.getInstance().uid.toString()

        firestore.collection("CustomGoals").document(date+FirebaseAuth.getInstance().uid.toString()).set(map)
            .addOnCompleteListener {
               Toast.makeText(this, "Custom Goal Set.",Toast.LENGTH_SHORT).show()
                onBackPressed()
            }.addOnFailureListener { e ->
                Toast.makeText(
                    applicationContext,
                    "Error : " + e.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

    }

//    private fun getDateOnline() {
//        val dateTimeOnline = DateTimeOnline(this)
//        dateTimeOnline.getDateTime(object : DateTimeOnline.VolleyCallBack {
//            override fun onGetDateTime(date: String?, time: String?) {
//                var n: String = date.toString().replace('/', '-')
//
//            }
//        })
//
//    }

    fun Activity.setStatusBarColor(color: Int) {
        var flags = window?.decorView?.systemUiVisibility // get current flag
        if (flags != null) {
            if (isColorDark(color)) {
                flags = flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
                window?.decorView?.systemUiVisibility = flags
            } else {
                flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                window?.decorView?.systemUiVisibility = flags
            }
        }
        window?.statusBarColor = color
    }

    fun Activity.isColorDark(color: Int): Boolean {
        val darkness =
            1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255
        return darkness >= 0.5
    }

}