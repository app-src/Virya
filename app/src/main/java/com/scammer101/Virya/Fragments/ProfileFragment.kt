package com.scammer101.Virya.Fragments

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.scammer101.Virya.Models.DailyYogaModel
import com.scammer101.Virya.Models.UserModel
import com.scammer101.Virya.R
import com.scammer101.Virya.Screens.AddDetailsScreen
import com.scammer101.Virya.Screens.EquipmentScreen
import com.scammer101.Virya.Screens.NumberScreen
import com.scammer101.Virya.Utilities.PreferenceManager
import com.scammer101.Virya.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var firestore : FirebaseFirestore
    private var userModel: UserModel? = null
    private lateinit var auth : FirebaseAuth
    private var preferenceManager: PreferenceManager? = null
    private var dailyModel: DailyYogaModel? = null
    private var totalAsanaIsFinished : Int = 0
    private var totalTime : Int = 0
    private var totalAsana : Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        preferenceManager = PreferenceManager(requireContext())
        binding.profileAddDetail.setOnClickListener {
            var intent = Intent(context, AddDetailsScreen::class.java)
            startActivity(intent)
        }
        firestore = FirebaseFirestore.getInstance()
        firestore.collection("Users").document(FirebaseAuth.getInstance().uid.toString()).get()
            .addOnSuccessListener { doc ->
                try {
                    userModel = doc.toObject(UserModel::class.java)
                    binding.profileUserName.setText(userModel!!.name.toString())
                    if(userModel!!.goalWeight!="") {
                        binding.profileGoalWeight.setText(userModel!!.goalWeight.toString() + " Kg")
                    }
                    if(userModel!!.startWeight!="") {
                        binding.profileStartWeight.setText(userModel!!.startWeight.toString() + " Kg")
                    }
                    if(userModel!!.currentWeight!="") {
                        binding.profileCurrentWeight.setText(userModel!!.currentWeight.toString() + " Kg")
                    }
                    binding.profileTrainingCompleted.setText(userModel!!.trainingCompleted.toString())
                    binding.profileTotalTime.setText(userModel!!.totalTimeCompleted.toString())
                    binding.profileTotalAsanas.setText(userModel!!.totalAsanasCompleted.toString())

                    if (userModel!!.profileImage != "") {
                        val decodedString: ByteArray = Base64.decode(userModel!!.profileImage, Base64.DEFAULT)
                        val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)

                        Glide.with(this)
                            .load(decodedByte).thumbnail(Glide.with(this).load(R.drawable.spinner))
                            .into(binding.profileUserImage)
                    } else {
                        binding.profileUserImage.setImageResource(R.drawable.demo_user)
                    }

                    if(userModel!!.isSubscribe)
                    {
                        binding.subscribeLockUnlock.setImageResource(R.drawable.lock_open_asset)
                    }
                    else
                    {
                        binding.subscribeLockUnlock.setImageResource(R.drawable.lock_asset)
                    }

                } catch (e: Exception) {
                    Toast.makeText(context, "Error:"+e.message, Toast.LENGTH_SHORT).show()

                }

            }

        firestore.collection("DailyYoga").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    try {
                        if(document.get("userId")!! == FirebaseAuth.getInstance().uid.toString()){
                            dailyModel = document.toObject(DailyYogaModel::class.java)
                            totalAsana+= dailyModel!!.finished + dailyModel!!.inProgress
                            totalTime+= dailyModel!!.timeSpent
                            totalAsanaIsFinished+= dailyModel!!.finished
                        }
                    } catch (e: Exception) {

                    }
                }
                binding.profileTrainingCompleted.text = totalAsana.toString()
                binding.profileTotalTime.text = totalTime.toString()
                binding.profileTotalAsanas.text = totalAsanaIsFinished.toString()
            }

        binding.profileLogout.setOnClickListener{
            auth.signOut()
            var intent = Intent(context, NumberScreen::class.java)
            startActivity(intent)
        }

        binding.profileEquipmentSelling.setOnClickListener{
            var intent = Intent(context, EquipmentScreen::class.java)
            startActivity(intent)
        }

        binding.profileSubscription.setOnClickListener {
            firestore.collection("Users").document(FirebaseAuth.getInstance().uid.toString()).update( "subscribe" , true)
           preferenceManager!!.putInt("starttime", System.currentTimeMillis().toInt() / 1000)
            Log.v("Subscribe", (System.currentTimeMillis().toInt() / 1000).toString())
            Toast.makeText(context, "Activating Subscription.",Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

}