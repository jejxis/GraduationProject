package oasis.team.econg.graduationproject.rvAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import oasis.team.econg.graduationproject.data.Schedule
import oasis.team.econg.graduationproject.databinding.ItemScheduleBinding

class ScheduleAdapter(val context: Context?): RecyclerView.Adapter<ScheduleAdapter.ScheduleHolder>() {
    var listData = mutableListOf<Schedule>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleHolder {
        val binding = ItemScheduleBinding.inflate(LayoutInflater.from(context), parent, false)
        return ScheduleHolder(binding)
    }

    override fun onBindViewHolder(holder: ScheduleHolder, position: Int) {
        val data = listData.get(position)
        holder.setData(data)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    fun setData(arrData: MutableList<Schedule>?){
        listData = arrData as ArrayList<Schedule>

    }
    inner class ScheduleHolder(val binding: ItemScheduleBinding): RecyclerView.ViewHolder(binding.root){
        fun setData(data: Schedule){
            binding.plantName.text = data.plantName
            when(data.careType){
                "WATER" -> binding.careType.text = "물주기"
                "NUTRITION" -> binding.careType.text = "영양제"
                "REPOTTING" -> binding.careType.text = "분갈이"
            }
        }
    }
}