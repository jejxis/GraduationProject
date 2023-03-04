package oasis.team.econg.graduationproject.rvAdapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import oasis.team.econg.graduationproject.R
import oasis.team.econg.graduationproject.bluetooth.BluetoothConnector
import oasis.team.econg.graduationproject.data.PlantsResponseDto
import oasis.team.econg.graduationproject.databinding.ItemHomeDiaryBinding
import oasis.team.econg.graduationproject.retrofit.RetrofitManager
import oasis.team.econg.graduationproject.utils.API
import oasis.team.econg.graduationproject.utils.Constants.TAG
import oasis.team.econg.graduationproject.utils.RESPONSE_STATE
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
        holder.setData(data, position)

        holder.itemView.rootView.setOnClickListener {
            listener!!.onClicked(data.id)
        }
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    inner class HomeDiaryHolder(val binding: ItemHomeDiaryBinding): RecyclerView.ViewHolder(binding.root){
        var btConnector = BluetoothConnector(context!!, binding.sensorValue)
        fun setData(data: PlantsResponseDto, position: Int){
            var bitmap: Bitmap? = null
            var message = ""
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
                binding.dday.text = "함께한지 "+data.dday.toString()+"일!"
                binding.name.text = data.name
                binding.recentRecordDate.text = "최근 기록 날짜: " + data.recentRecordDate
                binding.diaryThumbnail.setImageBitmap(bitmap)
                if(data.star){
                    binding.star.setImageResource(R.drawable.ic_baseline_star_45_true)
                    binding.bluetoothLayout.visibility = View.VISIBLE
                    binding.btnBluetooth.setOnClickListener {
                        //data.sensorValue
                        btConnector.searchDevice()
                    }
                    //btConnector.beginListenForData()
                    //binding.sensorValue.text = btConnector.message
                }
                else binding.star.setImageResource(R.drawable.ic_baseline_star_45_false)
                binding.star.setOnClickListener {
                    proceedStarPlants(data.star, data.id)
                    data.star = !data.star
                    this@HomeDiaryAdapter.notifyItemChanged(position)
                }
            }catch(e: InterruptedException){
                e.printStackTrace()
            }
        }

        private fun proceedStarPlants(isStar: Boolean, id: Long){
            RetrofitManager.instance.starPlants(API.HEADER_TOKEN, id, completion = {
                    responseState, s ->
                when(responseState){
                    RESPONSE_STATE.OKAY -> {
                        Log.d(TAG, "HomeDiaryAdapter - proceedStarPlants: $s")
                        if(isStar){
                            binding.star.setImageResource(R.drawable.ic_baseline_star_45_false)
                        } else{
                            binding.star.setImageResource(R.drawable.ic_baseline_star_45_true)
                        }
                    }
                    RESPONSE_STATE.FAIL -> {
                        Log.d(TAG, "HomeDiaryAdapter - proceedStarPlants: FAIL")
                    }
                }
            })
        }
    }

    interface OnItemClickListener{
        fun onClicked(id:Long)
    }

    fun setData(list: MutableList<PlantsResponseDto>?){
        listData = list as ArrayList<PlantsResponseDto>
    }
}