package oasis.team.econg.graduationproject.rvAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import oasis.team.econg.graduationproject.data.PlantSpecies
import oasis.team.econg.graduationproject.databinding.ItemPlantSpeciesBinding

class PlantSpeciesAdapter(val context: Context?):RecyclerView.Adapter<PlantSpeciesAdapter.PlantSpeciesHolder>() {
    var listData = mutableListOf<PlantSpecies>()
    var listener: OnClickedItem? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantSpeciesHolder {
        val binding = ItemPlantSpeciesBinding.inflate(LayoutInflater.from(context), parent, false)
        return PlantSpeciesHolder(binding)
    }

    override fun onBindViewHolder(holder: PlantSpeciesHolder, position: Int) {
        val data = listData.get(position)
        holder.setData(data)
        holder.itemView.rootView.setOnClickListener {
            listener!!.onClick(data.no)
        }
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    fun setData(arrData: MutableList<PlantSpecies>?){
        listData = arrData as ArrayList<PlantSpecies>
    }

    inner class PlantSpeciesHolder(val binding: ItemPlantSpeciesBinding):RecyclerView.ViewHolder(binding.root){
        fun setData(data: PlantSpecies){
            //binding.plantSpeciesPicture
            binding.plantSpeciesName.text = data.plantSpeciesName
        }
    }

    interface OnClickedItem{
        fun onClick(id: String)
    }
}