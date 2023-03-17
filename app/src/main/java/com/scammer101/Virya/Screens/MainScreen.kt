package com.scammer101.Virya.Screens

import android.app.Activity
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.scammer101.Virya.Fragments.GoalsFragment
import com.scammer101.Virya.Fragments.HomeFragment
import com.scammer101.Virya.Fragments.ProfileFragment
import com.scammer101.Virya.R
import com.scammer101.Virya.databinding.ActivityMainScreenBinding

class MainScreen : AppCompatActivity() {

    private lateinit var binding: ActivityMainScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        setListeners()
    }


    private fun init() {
        setStatusBarColor(Color.parseColor("#EAFDDD"))
        binding.bottomNavigation.setItemSelected(R.id.home)
        firstReplacementFragment(HomeFragment())
        binding.bottomNavigation.showBadge(R.id.profile)
        //binding.bottomNavigation.showBadge(R.id.goals, 10)
    }

    private fun setListeners()
    {
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it) {

                R.id.home -> {
                    replaceFragment(HomeFragment())
                    setStatusBarColor(Color.parseColor("#EAFDDD"))
                }
                R.id.goals -> {
                    replaceFragment(GoalsFragment())
                    setStatusBarColor(Color.parseColor("#EAFDDD"))
                }
                R.id.profile -> {
                    replaceFragment(ProfileFragment())
                    setStatusBarColor(Color.parseColor("#ffffff"))
                    }
            }

    }
    }

    private fun replaceFragment(fragment : Fragment)
    {
        try{
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right)
            fragmentTransaction.replace(R.id.fragment_container, fragment)
            fragmentTransaction.commit()
        } catch (e: Exception) {
            Toast.makeText(this@MainScreen, "Error : " + e.message, Toast.LENGTH_SHORT)
                .show()
        }

    }

    private fun firstReplacementFragment(fragment : Fragment)
    {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }

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
