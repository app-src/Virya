package com.scammer101.Virya.Fragments

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.scammer101.Virya.Models.CustomGoalModel
import com.scammer101.Virya.Models.DailyYogaModel
import com.scammer101.Virya.Screens.CustomGoalScreen
import com.scammer101.Virya.Utilities.ConstantsValues
import com.scammer101.Virya.Utilities.PreferenceManager
import com.scammer101.Virya.adapters.GoalsAdapter
import com.scammer101.Virya.databinding.FragmentGoalsBinding
import java.util.*

class GoalsFragment : Fragment() {

    private var _binding: FragmentGoalsBinding? = null
    private val binding get() = _binding!!
    private var currentProgress: Int = 0
    private var preferenceManager: PreferenceManager? = null
    private lateinit var firestore: FirebaseFirestore
    private var dailyModel: DailyYogaModel? = null
    private var customModel: CustomGoalModel? = null
    private lateinit var yogaArrayList: ArrayList<CustomGoalModel>
    private lateinit var adapter: GoalsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGoalsBinding.inflate(inflater, container, false)

        init()
        val date = preferenceManager!!.getString(ConstantsValues.KEY_DATE_ONLY)

        firestore.collection("CustomGoals")
            .whereEqualTo("dateAndUser", date + FirebaseAuth.getInstance().uid.toString())
            .addSnapshotListener(
                EventListener<QuerySnapshot?> { value, error ->

                    for (dc in value!!.documentChanges) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            yogaArrayList!!.add(dc.document.toObject(CustomGoalModel::class.java))
                        }
                        adapter.notifyDataSetChanged()
                    }
                })


        binding.setCustomGoal.setOnClickListener {
            var intent = Intent(context, CustomGoalScreen::class.java)
            startActivity(intent)
        }

        firestore.collection("CustomGoals").document(preferenceManager!!.getString(ConstantsValues.KEY_DATE)).get()
            .addOnSuccessListener { doc ->
                try {
                    customModel = doc.toObject(CustomGoalModel::class.java)
                    preferenceManager!!.putString(ConstantsValues.KEY_LATEST_CUSTOM_GOAL, customModel!!.name.toString().lowercase())
                    preferenceManager!!.putString(ConstantsValues.KEY_REPEAT, customModel!!.repeat.toString().lowercase())
                } catch (e: Exception) {
                    Log.v("limit error", e.message.toString())
                }

            }

        binding.goalsShareSuccess.setOnClickListener {

            if(preferenceManager!!.getString(ConstantsValues.KEY_LATEST_CUSTOM_GOAL.toString())=="" || preferenceManager!!.getString(ConstantsValues.KEY_LATEST_CUSTOM_GOAL.toString())==null)
            {
                val shareBody =          """
            Hey, I'm using Virya as my Virtual Fitness Instructor and the results are great.
            Download from Play Store
            https://play.google.com/store/apps/details?id=${requireActivity().packageName}
            """.trimIndent()
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_TEXT, shareBody)
                startActivity(intent)
            }
            else
            {
                val pose = preferenceManager!!.getString(ConstantsValues.KEY_LATEST_CUSTOM_GOAL).toString()
                val count = preferenceManager!!.getString(ConstantsValues.KEY_REPEAT).toString()

                val shareBody =          """
            Hey, I'm using Virya as my Virtual Fitness Instructor and the results are great.
            Download from Play Store
            https://play.google.com/store/apps/details?id=${requireActivity().packageName}
            """.trimIndent() + "\nMy Goal for today is to perform " + pose + " pose " + count + " times."
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_TEXT, shareBody)
                startActivity(intent)
            }




        }

        firestore.collection("DailyYoga").document(preferenceManager!!.getString(ConstantsValues.KEY_DATE)).get()
            .addOnSuccessListener { doc ->
                try {
                    dailyModel = doc.toObject(DailyYogaModel::class.java)

                    val name = preferenceManager!!.getString(ConstantsValues.KEY_LATEST_CUSTOM_GOAL)
                    val repeat = preferenceManager!!.getString(ConstantsValues.KEY_REPEAT)

                    if(name.contains("tree"))
                    {
                        preferenceManager!!.putString(ConstantsValues.KEY_FIREBASE_REPEAT, dailyModel!!.treePoseCountPose.toString())
                        preferenceManager!!.putString(ConstantsValues.KEY_FIREBASE_TIMER, dailyModel!!.treePoseCountTimer.toString())
                    }
                    else if(name.contains("tpose"))
                    {
                        preferenceManager!!.putString(ConstantsValues.KEY_FIREBASE_REPEAT, dailyModel!!.gettPoseCountPose().toString())
                        preferenceManager!!.putString(ConstantsValues.KEY_FIREBASE_TIMER, dailyModel!!.gettPoseCountTimer().toString())
                    }
                    else if(name.contains("warrior"))
                    {
                        preferenceManager!!.putString(ConstantsValues.KEY_FIREBASE_REPEAT, dailyModel!!.warriorPoseCountPose.toString())
                        preferenceManager!!.putString(ConstantsValues.KEY_FIREBASE_TIMER, dailyModel!!.warriorPoseCountTimer.toString())
                    }



                } catch (e: Exception) {
                    Log.v("limit error", e.message.toString())
                }

            }


        try {
            val customSetCount = preferenceManager!!.getString(ConstantsValues.KEY_REPEAT).toInt()
            val customFirebaseTimer = preferenceManager!!.getString(ConstantsValues.KEY_FIREBASE_TIMER).toInt()
            val customFirebaseCount = preferenceManager!!.getString(ConstantsValues.KEY_FIREBASE_REPEAT).toInt()
            binding.goalTotalTimer.text = customFirebaseTimer.toString() + " sec"
            currentProgress = (customFirebaseCount/customSetCount).toInt()*100
            Log.v("CurrentProgress", currentProgress.toString())
            Log.v("CurrentProgress", customFirebaseCount.toString())
            Log.v("CurrentProgress", customSetCount.toString())
            if(currentProgress>100)
            {
                currentProgress = 100
            }
            binding.goalCompletedText.text = "Goal Completed : " + currentProgress.toString() + "%"
            ObjectAnimator.ofInt(binding.goalsProgressBar, "progress", currentProgress)
                .setDuration(2000).start()
        } catch (e: Exception) {
        }

        return binding.root
    }

    private fun init() {

        preferenceManager = PreferenceManager(context)
        firestore = FirebaseFirestore.getInstance()
        yogaArrayList = ArrayList()
        adapter = GoalsAdapter(requireContext(), yogaArrayList)
        binding.goalsRecyclerView.adapter = adapter
    }


}