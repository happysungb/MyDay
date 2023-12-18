package com.example.myday.food


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.NumberPicker
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.myday.R
import com.example.myday.databinding.FoodListBinding

class NutritionAdapter(private val callback: DialogCallback): RecyclerView.Adapter<NutritionAdapter.MyNutritionViewHolder>() {
    var datas = mutableListOf<Row>()
    inner class MyNutritionViewHolder(private val binding: FoodListBinding): RecyclerView.ViewHolder(binding.root) {
        val btn: Button = binding.foodSelectBtn
        fun bind(data: Row) {
            binding.foodName.text = data.DESC_KOR
            "${data.NUTR_CONT1.toDouble().toInt()}kcal".also { binding.foodKcal.text = it }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyNutritionViewHolder {
        val binding = FoodListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyNutritionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyNutritionViewHolder, position: Int) {
        holder.bind(datas[position])

        holder.btn.setOnClickListener {
            val dialogView = LayoutInflater.from(it.context).inflate(R.layout.food_dialog, null)
            val np = dialogView.findViewById<NumberPicker>(R.id.food_count)
            np.minValue = 1
            np.maxValue = 50
            val intKcal = datas[position].NUTR_CONT1.toDouble().toInt()
            AlertDialog.Builder(it.context)
                .setTitle("음식 수량 입력")
                ?.setMessage("선택하신 음식은 1회 제공량당 ${intKcal}kcal 입니다.\n몇 인분을 드셨나요?")
                ?.setView(dialogView)
                ?.setPositiveButton("확인")
                { dialog, id ->
                    val selectedVal = np.value
                    callback.onConfirm(datas[position], selectedVal)
                }
                ?.setNegativeButton("취소")
                { dialog, id ->
                    dialog.dismiss()
                }
                ?.show()
        }
    }
    override fun getItemCount(): Int {
        return datas.size
    }
}