package oasis.team.econg.graduationproject.rvAdapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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
                binding.plantSpeciesPicture.setImageResource(R.drawable.flower_pot)

            try{
                if(!data.picture.isNullOrEmpty()){
                    thread.join()
                    binding.plantSpeciesPicture.setImageBitmap(bitmap)
                }
                binding.plantSpeciesName.text = data.name
                binding.plantSpeciesManageLevel.text = data.manageLevel
            }catch (e:InterruptedException){
                e.printStackTrace()
            }
        }
    }

    interface OnClickedItem{
        fun onClick(id: Long)
    }
}