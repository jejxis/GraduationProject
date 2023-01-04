package oasis.team.econg.graduationproject.rvAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import oasis.team.econg.graduationproject.data.Diary
import oasis.team.econg.graduationproject.databinding.ItemDiaryBinding

class DiaryAdapter(val context: Context?): RecyclerView.Adapter<DiaryAdapter.DiaryHolder>() {
    var listData = mutableListOf<Diary>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryHolder {
        val binding = ItemDiaryBinding.inflate(LayoutInflater.from(context), parent, false)
        return DiaryHolder(binding)
    }

    override fun onBindViewHolder(holder: DiaryHolder, position: Int) {
        val data = listData.get(position)
        holder.setData(data)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    fun setData(arrData: MutableList<Diary>?){
        listData = arrData as ArrayList<Diary>
    }

    inner class DiaryHolder(val binding: ItemDiaryBinding): RecyclerView.ViewHolder(binding.root){
        fun setData(data: Diary){
            binding.date.text = data.date
            binding.whatDay.text = data.whatDay
            binding.imgDiary.setImageResource(data.img)
            binding.content.text = data.content
        }
    }
}