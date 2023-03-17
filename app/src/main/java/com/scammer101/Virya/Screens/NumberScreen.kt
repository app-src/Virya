package com.scammer101.Virya.Screens

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.scammer101.Virya.databinding.ActivityNumberScreenBinding

class NumberScreen : AppCompatActivity() {

    private lateinit var binding : ActivityNumberScreenBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNumberScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        if(auth.currentUser!=null)
        {
            startActivity(Intent(this, MainScreen::class.java))
            finish()
        }

        binding.numberContinueButton.setOnClickListener{

            if(binding.numberPhoneNumber.text!!.isEmpty() || binding.numberPhoneNumber.text!!.length<10)
            {
                binding.numberTextLayout.setError("Enter Valid Number")
            }
            else
            {
                var intent = Intent(this, OTPScreen::class.java)
                intent.putExtra("number", binding.numberPhoneNumber.text.toString())
                startActivity(intent)
                finish()
            }
        }

    }

    fun init()
    {
        auth = FirebaseAuth.getInstance()
        setStatusBarColor(Color.parseColor("#CC2E2D3B"))
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