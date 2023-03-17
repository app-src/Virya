package com.scammer101.Virya.Screens

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.scammer101.Virya.databinding.ActivityOtpscreenBinding
import java.util.concurrent.TimeUnit
import com.scammer101.Virya.R

class OTPScreen : AppCompatActivity() {

    private lateinit var binding: ActivityOtpscreenBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var verificationId: String
    private var pd: ProgressDialog? = null
    private lateinit var firestore: FirebaseFirestore
    private var query: Query? = null
    private var threadOTP: Thread? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        pd!!.show()

        val phoneNumber = "+91" + intent.getStringExtra("number")
        val parseNumber = intent.getStringExtra("number")
        val phoneNumberShow = "+91 " + intent.getStringExtra("number")
        binding.otpPhoneShowText.setText(phoneNumberShow)
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {

                    if (binding.otpOTPCode.text!!.isEmpty()) {

                    } else {
                        pd!!.show()
                        val credential = PhoneAuthProvider.getCredential(
                            verificationId,
                            binding.otpOTPCode.text!!.toString()
                        )
                        auth.signInWithCredential(credential)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    Handler(Looper.getMainLooper()).postDelayed({
                                        pd!!.dismiss()
                                        var intent = Intent(this@OTPScreen, MainScreen::class.java)
                                        startActivity(intent)
                                        finish()
                                    }, 8000);
                                } else {
                                    pd!!.dismiss()
                                    Toast.makeText(
                                        this@OTPScreen,
                                        "Error : " + it.exception,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                    }

                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    pd!!.dismiss()
                    Toast.makeText(this@OTPScreen, "Error : " + p0.message, Toast.LENGTH_LONG)
                        .show()
                }

                override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(p0, p1)

                    pd!!.dismiss()
                    verificationId = p0
                }
            }).build()

        PhoneAuthProvider.verifyPhoneNumber(options)

        binding.otpVerifyButton.setOnClickListener {

            if (binding.otpOTPCode.text!!.isEmpty()) {
                binding.otpTextLayout.setError("Invalid OTP")
            } else {
                pd!!.show()
                val credential = PhoneAuthProvider.getCredential(
                    verificationId,
                    binding.otpOTPCode.text!!.toString()
                )
                auth.signInWithCredential(credential)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            pd!!.dismiss()
                            try {
                                createUser(auth.currentUser!!, parseNumber!!)
                            } catch (e: Exception) {
                                Log.e("FireErrorBase : ", e.message.toString())
                                Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
                            }

                        } else {
                            pd!!.dismiss()
                            Toast.makeText(
                                this@OTPScreen,
                                "Error : " + it.exception,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            }

        }


    }

    fun init() {
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        setStatusBarColor(Color.parseColor("#CC2E2D3B"))

        pd = ProgressDialog(this@OTPScreen)
        pd!!.show()
        pd!!.setContentView(R.layout.progress_dialog_verification)
        pd!!.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)

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

    fun createUser(user: FirebaseUser, number: String) {
        val runnableOTP = Runnable {
            try {
                val userName: String = number.substring(number.length - 3)
                val uid = user.uid
                val map = HashMap<String, Any>()
                map["name"] = "user$userName"
                map["contact"] = number
                map["uid"] = user.uid
                map["email"] = "Email"
                map["profileImage"] = ""
                map["asanas"] = 0
                map["ban"] = false
                map["subscribe"] = false
                map["trainingCompleted"] = 0
                map["totalTimeCompleted"] = 0
                map["totalAsanasCompleted"] = 0
                map["dob"] = ""
                map["currentWeight"] = ""
                map["startWeight"] = ""
                map["goalWeight"] = ""
                map["height"] = ""
                map["gender"] = ""


                query = firestore.collection("Users").whereEqualTo("contact", number)
                query!!.addSnapshotListener(EventListener<QuerySnapshot?> { value, error ->
                    if (!value!!.isEmpty) {
                        val intent = Intent(this@OTPScreen, MainScreen::class.java)
                        Log.d("OTPActivity : ", "login")
                        startActivity(intent)
                        finish()
                    } else {
                        firestore.collection("Users").document(user.uid).set(map)
                            .addOnCompleteListener {
                                Handler(Looper.getMainLooper()).postDelayed({
                                    Log.d("OTPActivity : ", "register")
                                    val intent = Intent(this@OTPScreen, MainScreen::class.java)
                                    startActivity(intent)
                                    finish()
                                }, 8000);
                            }.addOnFailureListener { e ->
                                Toast.makeText(
                                    applicationContext,
                                    "Error : " + e.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }
                })
            } catch (e: Exception) {
                Toast.makeText(this@OTPScreen, "Error : " + e.message, Toast.LENGTH_SHORT)
                    .show()
            }
        }
        threadOTP = Thread(runnableOTP)

        threadOTP!!.start()

    }


}