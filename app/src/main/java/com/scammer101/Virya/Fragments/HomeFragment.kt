package com.scammer101.Virya.Fragments

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.scammer101.Virya.Models.DailyYogaModel
import com.scammer101.Virya.Models.YogaPose
import com.scammer101.Virya.R
import com.scammer101.Virya.Utilities.ConstantsValues
import com.scammer101.Virya.Utilities.DateTimeOnline
import com.scammer101.Virya.Utilities.PreferenceManager
import com.scammer101.Virya.adapters.HomeDaysRecyclerAdapter
import com.scammer101.Virya.adapters.HomePosesRecyclerAdapter
import com.scammer101.Virya.databinding.FragmentHomeBinding
import java.util.*


class HomeFragment : Fragment(),HomeDaysRecyclerAdapter.OnClick {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var list : ArrayList<YogaPose>
    private var preferenceManager: PreferenceManager? = null
    private lateinit var firestore : FirebaseFirestore
    private var dailyModel: DailyYogaModel? = null
    private var finished : Int = 0
    private var inProgress : Int = 0
    private var timeSpent : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val date = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        binding = FragmentHomeBinding.inflate(layoutInflater)
        binding.greetings.text = getGreetingMessage()
        binding.day.text = buildString {
            append("Day ")
            append(getDay())
        }
        binding.daysRecyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false)
        binding.daysRecyclerView.adapter = HomeDaysRecyclerAdapter(date,this)
        binding.posesRecyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false)
        binding.posesRecyclerView.setHasFixedSize(true)
        list = ArrayList()

        list.add(YogaPose(R.drawable.warrior_2_pose_image_recyclerview, "warrior2pose"))
        list.add(YogaPose(R.drawable.treepose_image_recyclerview, "treepose"))
        list.add(YogaPose(R.drawable.tpose_image, "tpose"))

        binding.posesRecyclerView.adapter = HomePosesRecyclerAdapter(requireActivity(), list)
        binding.monthNyear.text = buildString {
            append(Calendar.getInstance().getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()).subSequence(0, 3))
            append(" ")
            append(Calendar.getInstance().get(Calendar.YEAR))
        }

        if(date>4){
            binding.daysRecyclerView.scrollToPosition(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)-4)
        }

        preferenceManager = PreferenceManager(requireContext())
        firestore = FirebaseFirestore.getInstance()
        getDateOnline()
        checkSubscription()

        return binding.root
    }

    private fun getDay(): String{
        val c = Calendar.getInstance()
        return (c.get(Calendar.DAY_OF_WEEK)).toString()
    }

    private fun checkSubscription()
    {
        val startTime = preferenceManager!!.getInt("starttime")
        val currentTime = (System.currentTimeMillis().toInt() / 1000)
        val timeofVip = currentTime - startTime //calculate the time of his VIP-being time
        Log.v("Subscribe", "startTime : " + startTime.toString())
        Log.v("Subscribe", "currentTime : " + currentTime.toString())
        Log.v("Subscribe", "timeOfVip : " + timeofVip.toString())
        if (timeofVip >= 2592000) //2592000 is 30 days in seconds
        {
//30 days over, update user to non-vip
            firestore.collection("Users").document(FirebaseAuth.getInstance().uid.toString()).update("subscribe", false)
        }
    }

    fun getGreetingMessage():String{
        val c = Calendar.getInstance()
        return when (c.get(Calendar.HOUR_OF_DAY)) {
            in 0..11 -> "Good Morning,"
            in 12..15 -> "Good Afternoon,"
            in 16..20 -> "Good Evening,"
            in 21..23 -> "Good Night,"
            else -> "Hello,"
        }
    }

    private fun getDateOnline() {
        val dateTimeOnline = DateTimeOnline(requireActivity())
        dateTimeOnline.getDateTime(object : DateTimeOnline.VolleyCallBack {
            override fun onGetDateTime(date: String?, time: String?) {
                var n: String = date.toString().replace('/', '-')
                preferenceManager!!.putString(
                    ConstantsValues.KEY_DATE,
                    n + FirebaseAuth.getInstance().uid!!.toString()
                )
                preferenceManager!!.putString(ConstantsValues.KEY_DATE_ONLY, n)
                checkLimit(n + FirebaseAuth.getInstance().uid!!.toString(), n)
            }
        })

    }

    private fun checkLimit(todayLimit: String, date: String) {
        firestore.collection("DailyYoga").document(todayLimit).get()
            .addOnSuccessListener { doc ->
                try {
                    dailyModel = doc.toObject(DailyYogaModel::class.java)
                    finished = dailyModel!!.finished
                    inProgress = dailyModel!!.inProgress
                    timeSpent = dailyModel!!.timeSpent

                    setDailyData(finished, inProgress, timeSpent)
                } catch (e: Exception) {
                    Log.v("limit error", e.message.toString())
                    if (e.message.toString().equals(null) || e.message.toString().equals("null")) {
                        firestore.collection("DailyYoga").document(todayLimit.toString()).set(
                            mapOf(
                                "finished" to 0,
                                "inProgress" to 0,
                                "timeSpent" to 0,
                                "warriorPoseCountPose" to 0,
                                "warriorPoseCountTimer" to 0,
                                "tPoseCountPose" to 0,
                                "tPoseCountTimer" to 0,
                                "treePoseCountPose" to 0,
                                "treePoseCountTimer" to 0,
                                "date" to date,
                                "userAndDate" to date+FirebaseAuth.getInstance().uid.toString(),
                                "userId" to FirebaseAuth.getInstance().uid.toString()
                            )
                        )
                        setDailyData(0, 0, 0)
                    }
                }

            }


    }

    private fun setDailyData(f : Int, i : Int, t : Int)
    {
        binding.finishedYogaTV.text = f.toString()
        binding.inProgressYogaTV.text = i.toString()
        binding.timeSpentYogaTV.text = t.toString()
    }


    override fun onDayClickListener(date: String) {
        Log.v("Homedate", date)
        checkLimit(date+FirebaseAuth.getInstance().uid.toString(), date)
    }

}