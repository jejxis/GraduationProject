package oasis.team.econg.graduationproject.rvAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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
            binding.waterCheck.visibility = if(data.waterCheck) View.VISIBLE else View.INVISIBLE

            binding.giveWater.setOnClickListener {
                binding.waterCheck.visibility = View.VISIBLE
            }
        }
    }

    interface OnItemClickListener{
        fun onClicked(id:String)
    }

    fun setData(list: MutableList<DiaryPlant>?){
        listData = list as ArrayList<DiaryPlant>
    }
}