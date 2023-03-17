package com.scammer101.Virya.Screens

import android.app.Activity
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.scammer101.Virya.Models.EquipmentDataClass
import com.scammer101.Virya.R
import com.scammer101.Virya.adapters.EquipmentAdapter
import com.scammer101.Virya.databinding.ActivityEquipmentScreenBinding

class EquipmentScreen : AppCompatActivity() {

    private lateinit var binding : ActivityEquipmentScreenBinding
    private lateinit var itemList : ArrayList<EquipmentDataClass>
    private lateinit var adapter : EquipmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEquipmentScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

    }

    private fun init()
    {
        setStatusBarColor(Color.parseColor("#ffffff"))
        itemList = ArrayList()
        binding.equipmentRecyclerView.setHasFixedSize(true)
        addList()
        adapter = EquipmentAdapter(itemList)
        binding.equipmentRecyclerView.adapter = adapter

    }

    private fun addList()
    {
        itemList.add(EquipmentDataClass(R.drawable.equip_1, "2.5 Kg Dumbbell", 500, 40))
        itemList.add(EquipmentDataClass(R.drawable.equip_2, "Sports Shoes", 1000, 10))
        itemList.add(EquipmentDataClass(R.drawable.equip_3, "Skipping Rope", 100, 10))
        itemList.add(EquipmentDataClass(R.drawable.equip_4, "Yoga Mat", 5000, 20))
        itemList.add(EquipmentDataClass(R.drawable.equip_5, "Yoga Mat Cover", 200, 30))
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