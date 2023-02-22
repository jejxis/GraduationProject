package oasis.team.econg.graduationproject.rvAdapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import oasis.team.econg.graduationproject.data.JournalsResponseDto
import oasis.team.econg.graduationproject.databinding.ItemDiaryBinding
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class DiaryAdapter(val context: Context?): RecyclerView.Adapter<DiaryAdapter.DiaryHolder>() {
    var listData = mutableListOf<JournalsResponseDto>()

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

    fun setData(arrData: MutableList<JournalsResponseDto>?){
        listData = arrData as ArrayList<JournalsResponseDto>
    }

    inner class DiaryHolder(val binding: ItemDiaryBinding): RecyclerView.ViewHolder(binding.root){
        fun setData(data: JournalsResponseDto){
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
            if(!data.picture.isNullOrEmpty())
                thread.start()
            else
                binding.imgDiary.visibility = View.GONE

            try{
                if(!data.picture.isNullOrEmpty()){
                    thread.join()
                    binding.imgDiary.setImageBitmap(bitmap)
                }
                binding.date.text = data.date
                binding.content.text = data.content
            }catch (e: InterruptedException){
                e.printStackTrace()
            }
        }
    }
}