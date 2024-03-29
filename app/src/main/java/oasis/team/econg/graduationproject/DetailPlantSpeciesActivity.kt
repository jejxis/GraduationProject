package oasis.team.econg.graduationproject

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import oasis.team.econg.graduationproject.data.*
import oasis.team.econg.graduationproject.databinding.ActivityDetailPlantSpeciesBinding
import oasis.team.econg.graduationproject.retrofit.RetrofitManager
import oasis.team.econg.graduationproject.rvAdapter.MyPlantAdapter
import oasis.team.econg.graduationproject.samplePreference.MyApplication
import oasis.team.econg.graduationproject.utils.API
import oasis.team.econg.graduationproject.utils.Constants.TAG
import oasis.team.econg.graduationproject.utils.RESPONSE_STATE
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class DetailPlantSpeciesActivity : AppCompatActivity() {
    val binding by lazy{ActivityDetailPlantSpeciesBinding.inflate(layoutInflater)}
    var id: Long = -1
    var gardenDetailDto: GardenDetailDto? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if(intent.hasExtra("id")){
            id = intent.getLongExtra("id", -1)
        }

        loadData()

        binding.btnBookmark.setOnClickListener {
            postBookmarks()
        }

        binding.btnClose.setOnClickListener {
            finish()
        }

        binding.btnAddMyPlant.setOnClickListener {
            if(gardenDetailDto != null){
                val intent = Intent(this, AddPlantActivity::class.java)
                var detailToMineDto = DetailToMineDto(
                    sunshine = gardenDetailDto!!.light,
                    water = gardenDetailDto!!.waterSupply,
                    temperature = gardenDetailDto!!.temperature
                )
                intent.putExtra("Mine", detailToMineDto)
                startActivity(intent)
            }

        }
    }

    private fun loadData(){
        RetrofitManager.instance.getGardenDetail(auth = MyApplication.prefs.token, gardenId = id, completion = {
            responseState, responseBody ->
            when(responseState){
                RESPONSE_STATE.OKAY -> {
                    setScreen(responseBody)
                    gardenDetailDto = responseBody
                }
                RESPONSE_STATE.FAIL -> {
                    Toast.makeText(this@DetailPlantSpeciesActivity, "데이터를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(this@DetailPlantSpeciesActivity, "데이터를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun setScreen(dto: GardenDetailDto) {
        var bitmap: Bitmap? = null
        val thread = object: Thread(){
            override fun run() {
                try{
                    var url = URL(dto.picture)
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
            binding.plantSpeciesPicture.setImageBitmap(bitmap)
            binding.plantSpeciesName.text = dto.name
            binding.plantPlace.text = dto.place
            binding.plantHumidity.text = dto.humidity
            binding.plantTemperature.text = dto.temperature
            binding.waterSupply.text = dto.waterSupply
            binding.manageLevel.text = dto.manageLevel
            binding.plantLight.text = dto.light
            binding.plantBug.text = dto.bug
            binding.plantDescription.text =dto.adviceInfo
            if(dto.bookmark){
                binding.btnBookmark.setImageResource(R.drawable.ic_baseline_bookmark_45)
            }
            else{
                binding.btnBookmark.setImageResource(R.drawable.ic_baseline_bookmark_border_45)
            }
        }catch (e: InterruptedException){
            e.printStackTrace()
        }

    }

    private fun postBookmarks(){
        RetrofitManager.instance.postBookmarks(MyApplication.prefs.token, id, completion = {
            responseState, msg ->
            when(responseState){
                RESPONSE_STATE.OKAY -> {
                    Log.d(TAG, "DetailPlantSpeciesActivity - postBookmarks: $msg")
                    if(msg == "Bookmark Add"){
                        binding.btnBookmark.setImageResource(R.drawable.ic_baseline_bookmark_45)
                    }
                    else{//Bookmark Delete
                        binding.btnBookmark.setImageResource(R.drawable.ic_baseline_bookmark_border_45)
                    }
                }
                RESPONSE_STATE.FAIL -> {
                    Toast.makeText(this@DetailPlantSpeciesActivity, "데이터를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(this@DetailPlantSpeciesActivity, "데이터를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}