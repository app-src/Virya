package com.scammer101.Virya.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.scammer101.Virya.Models.CustomGoalModel
import com.scammer101.Virya.R
import com.scammer101.Virya.databinding.GoalsAsanasItemViewBinding

class GoalsAdapter (var context: Context, var list: List<CustomGoalModel>) :
    RecyclerView.Adapter<GoalsAdapter.GoalViewHolder>() {


    inner class GoalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding = GoalsAsanasItemViewBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        return GoalViewHolder(
            LayoutInflater.from(context).inflate(R.layout.goals_asanas_item_view, parent, false)
        )
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {

        val item = list[position]
        holder.binding.poseCount.text = item.repeat.toString()
        holder.binding.poseTimer.text = item.timer.toString()
        holder.binding.poseName.text = item.name.toString()


//        holder.itemView.setOnClickListener {
//            var intent = Intent(context, DetailHistoryActivity::class.java)
//            intent.putExtra("paid", item.isPaid)
//            intent.putExtra("name", item.name)
//            intent.putExtra("amount", item.amount)
//            intent.putExtra("paytm", item.paytm)
//            intent.putExtra("date", item.date)
//            intent.putExtra("time", item.time)
//            intent.putExtra("userId", item.userId)
//            context.startActivity(intent)
//        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

}