package com.example.myday.food.tabview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myday.databinding.SelectedListOneBinding
import com.example.myday.food.Selected
import com.example.myday.food.Time

class ListOneAdapter(private val tabId: Time): RecyclerView.Adapter<ListOneAdapter.MyListOneViewHolder>() {
    var breakfast = mutableListOf<Selected>()
    var lunch = mutableListOf<Selected>()
    var dinner = mutableListOf<Selected>()

    inner class MyListOneViewHolder(private val binding: SelectedListOneBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Selected, position: Int) {
            "${position+1}. ${data.name}".also {binding.selectedFoodName.text = it}
            "${data.kcal}kcal".also { binding.selectedFoodKcal.text = it }
            "${data.count}인분".also { binding.selectedFoodCount.text = it }
            "=> ${data.kcal} X ${data.count} = ${data.count?.times(data.kcal!!)}kcal".also { binding.selectedFoodSum.text = it }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyListOneViewHolder {
        val binding = SelectedListOneBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyListOneViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyListOneViewHolder, position: Int) {
        when (tabId) {
            Time.BREAKFAST -> holder.bind(breakfast[position], position)
            Time.LUNCH -> holder.bind(lunch[position], position)
            else -> holder.bind(dinner[position], position)
        }
    }

    override fun getItemCount(): Int {
        return when (tabId) {
            Time.BREAKFAST -> breakfast.size
            Time.LUNCH -> lunch.size
            else -> dinner.size
        }
    }

    fun updateData() {
        notifyDataSetChanged()
    }

}