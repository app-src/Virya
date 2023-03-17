package com.scammer101.Virya.Screens

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.scammer101.Virya.Models.DailyYogaModel
import com.scammer101.Virya.Models.UserModel
import com.scammer101.Virya.R
import com.scammer101.Virya.databinding.ActivityAddDetailsScreenBinding
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.InputStream

class AddDetailsScreen : AppCompatActivity() {

    private lateinit var binding : ActivityAddDetailsScreenBinding
    private lateinit var firestore : FirebaseFirestore
    private var userModel: UserModel? = null
    private var profileImage: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddDetailsScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        setListeners()

    }

    private fun init()
    {
        setStatusBarColor(Color.parseColor("#ffffff"))
        firestore = FirebaseFirestore.getInstance()
        getUserData()
    }

    private fun setListeners()
    {
        binding.profileDetailsBackButton.setOnClickListener{onBackPressed()}
        binding.detailsSaveButton.setOnClickListener { saveUserData() }
        try {
            binding.detailProfileImage.setOnClickListener {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                pickImage.launch(intent)
            }
        } catch (e: Exception) {
            Toast.makeText(applicationContext, "Error : " + e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun getUserData() {
        firestore.collection("Users").document(FirebaseAuth.getInstance().uid.toString()).get()
            .addOnSuccessListener { doc ->
                try {
                    userModel = doc.toObject(UserModel::class.java)
                    binding.detailsUserName.setText(userModel!!.name.toString())
                    binding.detailsDob.setText(userModel!!.dob.toString())
                    binding.detailsGoalWeight.setText(userModel!!.goalWeight.toString())
                    binding.detailsStartWeight.setText(userModel!!.startWeight.toString())
                    binding.detailsCurrentWeight.setText(userModel!!.currentWeight.toString())
                    binding.detailsHeight.setText(userModel!!.height.toString())
                    binding.detailsGender.setText(userModel!!.gender.toString())

                    if (userModel!!.profileImage != "") {
                        val decodedString: ByteArray = Base64.decode(userModel!!.profileImage, Base64.DEFAULT)
                        val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)

                        Glide.with(this)
                            .load(decodedByte).thumbnail(Glide.with(this).load(R.drawable.spinner))
                            .into(binding.detailProfileImage)
                    } else {
                        binding.detailProfileImage.setImageResource(R.drawable.demo_user)
                    }

                } catch (e: Exception) {
                   Toast.makeText(applicationContext, "Error:"+e.message,Toast.LENGTH_SHORT).show()

                }

            }


    }

    private fun saveUserData()
    {
        val name = binding.detailsUserName.text.toString()
        val dob = binding.detailsDob.text.toString()
        val startW = binding.detailsStartWeight.text.toString()
        val height = binding.detailsHeight.text.toString()
        val gender = binding.detailsGender.text.toString()
        val goalW = binding.detailsGoalWeight.text.toString()
        val currentW = binding.detailsCurrentWeight.text.toString()

        if (profileImage != "") {
            firestore.collection("Users").document(FirebaseAuth.getInstance().uid.toString())
                .update("profileImage", profileImage)
        } else {
            binding.detailProfileImage.setImageResource(R.drawable.demo_user)
        }

        firestore.collection("Users")
            .document(FirebaseAuth.getInstance().uid!!)
            .update(mapOf(
                "name" to name,
                "dob" to dob,
                "goalWeight" to goalW,
                "currentWeight" to currentW,
                "startWeight" to startW,
                "height" to height,
                "gender" to gender,
            ))
        onBackPressed()
        Toast.makeText(applicationContext, "User Profile Updated.", Toast.LENGTH_SHORT).show()
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

    private val pickImage = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            if (result.data != null) {
                val imageUri = result.data!!.data
                try {
                    val inputStream: InputStream? =
                        applicationContext?.getContentResolver()?.openInputStream(imageUri!!)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    binding.detailProfileImage.setImageBitmap(bitmap)
                    profileImage = profileImage(bitmap)!!
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun profileImage(bitmap: Bitmap): String? {
        val previewWidth = 160
        val previewHeight = bitmap.height * previewWidth / bitmap.width
        val previewbitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false)
        val byteArrayOutputStream = ByteArrayOutputStream()
        previewbitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val bytes = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

}