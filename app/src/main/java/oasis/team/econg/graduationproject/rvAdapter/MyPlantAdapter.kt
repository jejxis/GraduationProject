package oasis.team.econg.graduationproject.rvAdapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import oasis.team.econg.graduationproject.R
import oasis.team.econg.graduationproject.data.DiaryPlant
import oasis.team.econg.graduationproject.data.Plant
import oasis.team.econg.graduationproject.databinding.ItemMyPlantBinding

class MyPlantAdapter(val context: Context?): RecyclerView.Adapter<MyPlantAdapter.MyPlantHolder>(){
    var listData: MutableList<DiaryPlant> = mutableListOf()
    var listener: MyPlantAdapter.OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPlantHolder {
        val binding = ItemMyPlantBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyPlantHolder(binding)
    }

    override fun onBindViewHolder(holder: MyPlantHolder, position: Int) {
        val data = listData.get(position)
        holder.setData(data)
        holder.itemView.setOnClickListener {
            listener!!.onClicked(data.plantId)
        }
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    inner class MyPlantHolder(val binding: ItemMyPlantBinding): RecyclerView.ViewHolder(binding.root){
        fun setData(data: DiaryPlant){
            binding.plantName.text = data.name
            binding.plantImg.setImageResource(data.thumb)
            binding.latestDiary.text = data.latestDiary

            binding.waterCheck.setOnClickListener {
                checkEachCultureStyle(it as TextView)
            }
            binding.nutrientsCheck.setOnClickListener {
                checkEachCultureStyle(it as TextView)
            }
            binding.repottingCheck.setOnClickListener {
                checkEachCultureStyle(it as TextView)
            }
        }
    }

    interface OnItemClickListener{
        fun onClicked(id:String)
    }

    fun setData(list: MutableList<DiaryPlant>?){
        listData = list as ArrayList<DiaryPlant>
    }

    private fun checkEachCultureStyle(view: TextView){
        view.setTextColor(Color.parseColor(("#000000")))
        view.setBackgroundResource(R.drawable.button_background_gray)
    }
}