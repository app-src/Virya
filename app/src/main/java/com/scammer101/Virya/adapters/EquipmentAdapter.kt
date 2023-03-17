package com.scammer101.Virya.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.scammer101.Virya.Models.EquipmentDataClass
import com.scammer101.Virya.R
import com.scammer101.Virya.databinding.EquipmentItemsBinding
import com.scammer101.Virya.databinding.ItemPosesRecyclerviewBinding

class EquipmentAdapter(private val itemList:List<EquipmentDataClass>) : RecyclerView.Adapter<EquipmentAdapter.EquipmentViewHolder>()
{
    class EquipmentViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
    {
        var binding = EquipmentItemsBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EquipmentViewHolder {
        return EquipmentViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.equipment_items, parent, false)
        )
    }

    override fun onBindViewHolder(holder: EquipmentViewHolder, position: Int) {
        val equip = itemList[position]

        holder.binding.equipmentImage.setImageResource(equip.equipmentImage)
        holder.binding.equipmentPrize.text = "₹"+equip.equipmentPrize.toString()
        holder.binding.equipmentDiscount.text = equip.equipment_discount.toString()+"% off"
        holder.binding.equipmentName.text = equip.equipmentName.toString()
    }

    override fun getItemCount(): Int {
       return itemList.size
    }

}