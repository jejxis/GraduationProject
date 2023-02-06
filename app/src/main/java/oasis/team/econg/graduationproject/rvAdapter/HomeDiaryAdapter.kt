package oasis.team.econg.graduationproject.rvAdapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import oasis.team.econg.graduationproject.data.PlantsResponseDto
import oasis.team.econg.graduationproject.databinding.ItemHomeDiaryBinding
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class HomeDiaryAdapter(val context: Context?): RecyclerView.Adapter<HomeDiaryAdapter.HomeDiaryHolder>() {
    var listData = mutableListOf<PlantsResponseDto>()
    var listener: HomeDiaryAdapter.OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeDiaryHolder {
        val binding = ItemHomeDiaryBinding.inflate(LayoutInflater.from(context), parent, false)
        return HomeDiaryHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeDiaryHolder, position: Int) {
        val data = listData.get(position)
        holder.setData(data)

        holder.itemView.rootView.setOnClickListener {
            listener!!.onClicked(data.id)
        }
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    inner class HomeDiaryHolder(val binding: ItemHomeDiaryBinding): RecyclerView.ViewHolder(binding.root){
        fun setData(data: PlantsResponseDto){
            var bitmap: Bitmap? = null
            val thread = object: Thread(){
                override fun run() {
                    try{
                        var url = URL(data.picture)
                        val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
                        conn.connect()
                        val inputStream = conn.inputStream
                        bitmap =  BitmapFactory.decodeStream(inputStream)
                    }catch(e: IOException){
                        e.printStackTrace()
                    }
                }
            }
            thread.start()

            try{
                thread.join()
                binding.dday.text = "물 주기까지 "+data.dday.toString()+"일!"
                binding.name.text = data.name
                binding.recentRecordDate.text = "최근 기록 날짜: " + data.recentRecordDate
                binding.diaryThumbnail.setImageBitmap(bitmap)
            }catch(e: InterruptedException){
                e.printStackTrace()
            }
        }
    }

    interface OnItemClickListener{
        fun onClicked(id:Long)
    }

    fun setData(list: MutableList<PlantsResponseDto>?){
        listData = list as ArrayList<PlantsResponseDto>
    }
}