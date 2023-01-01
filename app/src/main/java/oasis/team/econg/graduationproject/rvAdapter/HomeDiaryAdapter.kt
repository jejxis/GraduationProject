package oasis.team.econg.graduationproject.rvAdapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import oasis.team.econg.graduationproject.data.Plant
import oasis.team.econg.graduationproject.databinding.ItemHomeDiaryBinding

class HomeDiaryAdapter(val context: Context?): RecyclerView.Adapter<HomeDiaryAdapter.HomeDiaryHolder>() {
    var listData = listOf<Plant>()
    var listener: HomeDiaryAdapter.OnItemClickListener? = null

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeDiaryHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: HomeDiaryHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}