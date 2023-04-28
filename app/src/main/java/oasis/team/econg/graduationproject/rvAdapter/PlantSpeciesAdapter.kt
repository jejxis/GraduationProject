package oasis.team.econg.graduationproject.rvAdapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import oasis.team.econg.graduationproject.R
import oasis.team.econg.graduationproject.data.GardenDto
import oasis.team.econg.graduationproject.databinding.ItemPlantSpeciesBinding
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class PlantSpeciesAdapter(val context: Context?):RecyclerView.Adapter<PlantSpeciesAdapter.PlantSpeciesHolder>() {
    var listData = mutableListOf<GardenDto>()
    var listener: OnClickedItem? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantSpeciesHolder {
        val binding = ItemPlantSpeciesBinding.inflate(LayoutInflater.from(context), parent, false)
        return PlantSpeciesHolder(binding)
    }

    override fun onBindViewHolder(holder: PlantSpeciesHolder, position: Int) {
        val data = listData.get(position)
        holder.setData(data)
        holder.itemView.rootView.setOnClickListener {
            listener!!.onClick(data.id)
        }
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    fun setData(arrData: MutableList<GardenDto>?){
        listData = arrData as ArrayList<GardenDto>
    }

    inner class PlantSpeciesHolder(val binding: ItemPlantSpeciesBinding):RecyclerView.ViewHolder(binding.root){
        fun setData(data: GardenDto){
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
                        binding.plantSpeciesPicture.setImageBitmap(bitmap)
                    }catch(e: IOException){
                        binding.plantSpeciesPicture.setImageResource(R.drawable.flower_pot)
                        e.printStackTrace()
                    }
                }
            }
            else{
                binding.plantSpeciesPicture.setImageResource(R.drawable.flower_pot)
            }
            binding.plantSpeciesName.text = data.name
            binding.plantSpeciesManageLevel.text = when(data.manageLevel){
                "초보자" -> "⭐"
                "경험자" -> "⭐⭐"
                "전문가" -> "⭐⭐⭐"
                else -> ""
            }
        }
    }

    interface OnClickedItem{
        fun onClick(id: Long)
    }
}