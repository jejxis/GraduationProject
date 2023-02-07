package oasis.team.econg.graduationproject.rvAdapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import oasis.team.econg.graduationproject.R
import oasis.team.econg.graduationproject.data.DiaryPlant
import oasis.team.econg.graduationproject.data.Plant
import oasis.team.econg.graduationproject.data.PlantsResponseDto
import oasis.team.econg.graduationproject.databinding.ItemMyPlantBinding
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class MyPlantAdapter(val context: Context?): RecyclerView.Adapter<MyPlantAdapter.MyPlantHolder>(){
    var listData: MutableList<PlantsResponseDto> = mutableListOf()
    var listener: MyPlantAdapter.OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPlantHolder {
        val binding = ItemMyPlantBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyPlantHolder(binding)
    }

    override fun onBindViewHolder(holder: MyPlantHolder, position: Int) {
        val data = listData.get(position)
        holder.setData(data)
        holder.itemView.setOnClickListener {
            listener!!.onClicked(data.id)
        }
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    inner class MyPlantHolder(val binding: ItemMyPlantBinding): RecyclerView.ViewHolder(binding.root){
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
                binding.plantName.text = data.name
                binding.plantImg.setImageBitmap(bitmap)
                binding.latestDiary.text = data.recentRecordDate
            }catch(e: InterruptedException){
                e.printStackTrace()
            }

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
        fun onClicked(id:Long)
    }

    fun setData(list: MutableList<PlantsResponseDto>?){
        listData = list as ArrayList<PlantsResponseDto>
    }

    private fun checkEachCultureStyle(view: TextView){
        view.setTextColor(Color.parseColor(("#000000")))
        view.setBackgroundResource(R.drawable.button_background_gray)
    }
}