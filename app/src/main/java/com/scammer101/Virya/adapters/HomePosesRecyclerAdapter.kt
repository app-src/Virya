package com.scammer101.Virya.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.scammer101.Virya.Models.YogaPose
import com.scammer101.Virya.R
import com.scammer101.Virya.Screens.DemoVideoScreen
import com.scammer101.Virya.Screens.PoseDetectorActivity
import com.scammer101.Virya.databinding.ItemPosesRecyclerviewBinding

class HomePosesRecyclerAdapter(var context : Context, var list: List<YogaPose>) : RecyclerView.Adapter<HomePosesRecyclerAdapter.HomePosesViewHolder>() {

    inner class HomePosesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding = ItemPosesRecyclerviewBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomePosesRecyclerAdapter.HomePosesViewHolder {
        return HomePosesViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_poses_recyclerview, parent, false)
        )
    }

    override fun onBindViewHolder(holder: HomePosesRecyclerAdapter.HomePosesViewHolder, position: Int) {
        val yoga = list[position]
        holder.binding.sessionIv.setImageResource(yoga.yogaImage)
        holder.itemView.setOnClickListener {

            var intent = Intent(context, DemoVideoScreen::class.java)
            intent.putExtra("yoga", yoga.yogaName)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}