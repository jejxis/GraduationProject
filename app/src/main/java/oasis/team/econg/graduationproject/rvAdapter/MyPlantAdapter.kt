package oasis.team.econg.graduationproject.rvAdapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import oasis.team.econg.graduationproject.R
import oasis.team.econg.graduationproject.data.PlantsResponseDto
import oasis.team.econg.graduationproject.databinding.ItemMyPlantBinding
import oasis.team.econg.graduationproject.retrofit.RetrofitManager
import oasis.team.econg.graduationproject.samplePreference.MyApplication
import oasis.team.econg.graduationproject.utils.API
import oasis.team.econg.graduationproject.utils.Constants.TAG
import oasis.team.econg.graduationproject.utils.RESPONSE_STATE
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
        holder.saveData(data)
        holder.setData(data)
        holder.itemView.setOnClickListener {
            listener!!.onClicked(data.id, data.name)
        }
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    inner class MyPlantHolder(val binding: ItemMyPlantBinding): RecyclerView.ViewHolder(binding.root){
        private lateinit var data: PlantsResponseDto
        fun saveData(data: PlantsResponseDto){
            this.data = data
        }
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
                postCalendars("w", it)
            }
            binding.nutrientsCheck.setOnClickListener {
                postCalendars("n", it)
            }
            binding.repottingCheck.setOnClickListener {
                postCalendars("r", it)
            }
        }

        private fun postCalendars(type: String, view: View){
            RetrofitManager.instance.postCalendars(auth = MyApplication.prefs.token, plantId = data.id, type = type, completion = {
                responseState, message ->
                when(responseState){
                    RESPONSE_STATE.OKAY -> {
                        Log.d(TAG, "MyPlantAdapter - postCalendars: $message")
                        checkEachCultureStyle(view as TextView)
                    }
                    RESPONSE_STATE.FAIL -> {
                        Toast.makeText(context, "재배 기록 등록 실패했습니다.", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "MyPlantAdapter - postCalendars: fail")
                    }
                    else -> {
                        Toast.makeText(context, "재배 기록 등록 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
    }

    interface OnItemClickListener{
        fun onClicked(id:Long, name: String)
    }

    fun setData(list: MutableList<PlantsResponseDto>?){
        listData = list as ArrayList<PlantsResponseDto>
    }

    private fun checkEachCultureStyle(view: TextView){
        view.setTextColor(Color.parseColor(("#000000")))
        view.setBackgroundResource(R.drawable.button_background_gray)
    }
}