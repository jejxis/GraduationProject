package oasis.team.econg.graduationproject.rvAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import oasis.team.econg.graduationproject.data.Plant
import oasis.team.econg.graduationproject.databinding.ItemHomeDiaryBinding

class HomeDiaryAdapter(val context: Context?): RecyclerView.Adapter<HomeDiaryAdapter.HomeDiaryHolder>() {
    var listData = mutableListOf<Plant>()
    var listener: HomeDiaryAdapter.OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeDiaryHolder {
        val binding = ItemHomeDiaryBinding.inflate(LayoutInflater.from(context), parent, false)
        return HomeDiaryHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeDiaryHolder, position: Int) {
        val data = listData.get(position)
        holder.setData(data)

        holder.itemView.rootView.setOnClickListener {
            listener!!.onClicked(data.plantId)
        }
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    inner class HomeDiaryHolder(val binding: ItemHomeDiaryBinding): RecyclerView.ViewHolder(binding.root){
        fun setData(data: Plant){
            binding.diaryThumbnail.setImageResource(data.thumb)
            binding.days.text = "함께한 지 " + data.days.toString()+"일!"
            binding.name.text = data.name
            binding.humidity.text = "토양 습도: " + data.hum.toString()
            binding.temperature.text = "온도: " + data.temp.toString()
        }
    }

    interface OnItemClickListener{
        fun onClicked(id:String)
    }

    fun setData(list: MutableList<Plant>?){
        listData = list as ArrayList<Plant>
    }
}