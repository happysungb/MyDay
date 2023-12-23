package com.example.myday.food.tabview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myday.databinding.SelectedListTwoBinding
import com.example.myday.food.Selected
import com.example.myday.food.Time

class ListTwoAdapter(private val tabId: Time): RecyclerView.Adapter<ListTwoAdapter.MyListTwoViewHolder>() {
    var breakfast = mutableListOf<Selected>()
    var lunch = mutableListOf<Selected>()
    var dinner = mutableListOf<Selected>()
    inner class MyListTwoViewHolder(private val binding: SelectedListTwoBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Selected, position: Int) {
            "${position+1}. ${data.name}".also {binding.list2Name.text = it}
            "${data.carbohydrate}g".also { binding.list2Carbo.text = it }
            "${data.protein}g".also { binding.list2Protein.text = it }
            "${data.fat}g".also { binding.list2Fat.text = it }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyListTwoViewHolder {
        val binding = SelectedListTwoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyListTwoViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return when (tabId) {
            Time.BREAKFAST -> breakfast.size
            Time.LUNCH -> lunch.size
            else -> dinner.size
        }
    }

    override fun onBindViewHolder(holder: MyListTwoViewHolder, position: Int) {
        when (tabId) {
            Time.BREAKFAST -> holder.bind(breakfast[position], position)
            Time.LUNCH -> holder.bind(lunch[position], position)
            else -> holder.bind(dinner[position], position)
        }
    }

    fun updateData() {
        notifyDataSetChanged()
    }

}