package oasis.team.econg.graduationproject.rvAdapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import oasis.team.econg.graduationproject.R
import oasis.team.econg.graduationproject.bluetooth.BluetoothConnector
import oasis.team.econg.graduationproject.data.PlantsResponseDto
import oasis.team.econg.graduationproject.databinding.ItemHomeDiaryBinding
import oasis.team.econg.graduationproject.retrofit.RetrofitManager
import oasis.team.econg.graduationproject.samplePreference.MyApplication
import oasis.team.econg.graduationproject.utils.API
import oasis.team.econg.graduationproject.utils.Constants.TAG
import oasis.team.econg.graduationproject.utils.RESPONSE_STATE
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class HomeDiaryAdapter(val context: Context?): RecyclerView.Adapter<HomeDiaryAdapter.HomeDiaryHolder>() {
    var listData = mutableListOf<PlantsResponseDto>()
    var listener: HomeDiaryAdapter.OnItemClickListener? = null
    var starCount = 0

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
            if(!data.picture.isNullOrEmpty()){
                CoroutineScope(Dispatchers.Main).launch{
                    try{
                        var bitmap: Bitmap? = withContext(Dispatchers.IO){
                            var url = URL(data.picture)
                            val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
                            conn.connect()
                            val inputStream = conn.inputStream
                            BitmapFactory.decodeStream(inputStream)
                        }
                        binding.diaryThumbnail.setImageBitmap(bitmap)
                    }catch(e: IOException){
                        binding.diaryThumbnail.setImageResource(R.drawable.logo_pot)
                        e.printStackTrace()
                    }
                }
            }
            else{
                binding.diaryThumbnail.setImageResource(R.drawable.logo_pot)
            }

            binding.dday.text = "함께한지 "+data.dday.toString()+"일!"
            binding.name.text = data.name
            binding.recentRecordDate.text = "최근 기록 날짜: " + data.recentRecordDate
            if(data.star){
                binding.star.setImageResource(R.drawable.ic_baseline_star_45_true)
                binding.bluetoothLayout.visibility = View.VISIBLE
                binding.btnBluetooth.setOnClickListener {
                    btConnector.searchDevice()
                }
            }
            else {
                binding.star.setImageResource(R.drawable.ic_baseline_star_45_false)
                binding.bluetoothLayout.visibility = View.GONE
            }
            binding.star.setOnClickListener {
                if(starCount > 0 && !data.star){
                    Toast.makeText(context, "대표 식물은 하나만 지정 가능합니다.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                proceedStarPlants(data.star, data.id)
                data.star = !data.star
                this@HomeDiaryAdapter.notifyItemChanged(position)
            }
        }

        private fun proceedStarPlants(isStar: Boolean, id: Long){
            RetrofitManager.instance.starPlants(auth = MyApplication.prefs.token, id, completion = {
                    responseState, s ->
                when(responseState){
                    RESPONSE_STATE.OKAY -> {
                        Log.d(TAG, "HomeDiaryAdapter - proceedStarPlants: $s")
                        if(isStar){
                            starCount -= 1
                            binding.star.setImageResource(R.drawable.ic_baseline_star_45_false)
                        } else{
                            starCount += 1
                            binding.star.setImageResource(R.drawable.ic_baseline_star_45_true)
                        }
                    }
                    RESPONSE_STATE.FAIL -> {
                        Log.d(TAG, "HomeDiaryAdapter - proceedStarPlants: FAIL")
                    }
                    else -> {

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
        if(listData != null){
            for(i in listData.size-1 downTo 0){
                if(listData[i].star){
                    starCount += 1
                    val data = listData[i]
                    listData.removeAt(i)
                    listData.add(0, data)
                }
            }
        }
    }
}