package oasis.team.econg.graduationproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import oasis.team.econg.graduationproject.data.JournalsResponseDto
import oasis.team.econg.graduationproject.databinding.ActivityDiaryListBinding
import oasis.team.econg.graduationproject.dialog.CheckDeletePlantFragment
import oasis.team.econg.graduationproject.retrofit.RetrofitManager
import oasis.team.econg.graduationproject.rvAdapter.DiaryAdapter
import oasis.team.econg.graduationproject.samplePreference.MyApplication
import oasis.team.econg.graduationproject.utils.API
import oasis.team.econg.graduationproject.utils.Constants.TAG
import oasis.team.econg.graduationproject.utils.RESPONSE_STATE

class DiaryListActivity : AppCompatActivity() {
    val binding by lazy{ActivityDiaryListBinding.inflate(layoutInflater)}
    var diaryList = mutableListOf<JournalsResponseDto>()
    var diaryAdapter = DiaryAdapter(this)
    var id: Long = -1
    var name: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val deletePlantDialog = CheckDeletePlantFragment()
        deletePlantDialog.setDialogListener(object : CheckDeletePlantFragment.CheckDeleteListener{
            override fun onDeleteClicked() {
                proceedDeletePlant()
            }
        })

        binding.btnClose.setOnClickListener {
            finish()
        }

        if(intent.hasExtra("id")){
            id = intent.getLongExtra("id", -1)
        }
        if(intent.hasExtra("name")){
            name = intent.getStringExtra("name").toString()
        }
        binding.diaryPlantName.text = name
        binding.btnVisibleGone.setOnClickListener {
            if(binding.buttons.visibility == View.VISIBLE){
                binding.buttons.visibility = View.GONE
                binding.btnVisibleGone.setImageResource(R.drawable.ic_baseline_chevron_left_24)
            }
            else{
                binding.buttons.visibility = View.VISIBLE
                binding.btnVisibleGone.setImageResource(R.drawable.ic_baseline_chevron_right_24)
            }
        }

        binding.btnGoToMyPlantInfo.setOnClickListener {
            val intent = Intent(this@DiaryListActivity, MyPlantInfoActivity::class.java)
            intent.putExtra("plantId", id)
            startActivity(intent)
        }

        binding.btnDeletePlant.setOnClickListener {
            deletePlantDialog.show(supportFragmentManager, "deletePlantDialog")
        }
        binding.addDiary.setOnClickListener {
            var intent = Intent(this, AddDiaryActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }
        loadData()
    }

    private fun loadData(){
        RetrofitManager.instance.getJournals(auth = MyApplication.prefs.token, plantId = id, completion = {
            responseState, responseBody ->
            when(responseState){
                RESPONSE_STATE.OKAY ->{
                    Log.d(TAG, "DiaryListActivity - loadData(): api call success : ${responseBody.toString()}")
                    diaryList = responseBody
                    setAdapter()
                }
                RESPONSE_STATE.FAIL ->{
                    Toast.makeText(this@DiaryListActivity, "다이어리 리스트를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "DiaryListActivity - loadData(): api call fail : $responseBody")
                }
                else -> {
                    Toast.makeText(this@DiaryListActivity, "다이어리 리스트를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun setAdapter(){
        binding.rvDiary.addItemDecoration(DividerItemDecoration(this, LinearLayout.VERTICAL))
        diaryAdapter.setData(diaryList)
        binding.rvDiary.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvDiary.adapter = diaryAdapter
    }

    private fun proceedDeletePlant(){
        if(id < 0) return
        RetrofitManager.instance.deletePlants(MyApplication.prefs.token, id, completion = {
            responseState, s ->
            when(responseState){
                RESPONSE_STATE.OKAY -> {
                    Log.d(TAG, "proceedDeletePlant: msg: $s")
                    val intent = Intent(this@DiaryListActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                RESPONSE_STATE.FAIL -> {
                    Toast.makeText(this@DiaryListActivity, "데이터를 삭제하는 데 실패했습니다.",
                    Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(this@DiaryListActivity, "데이터를 삭제하는 데 실패했습니다.",
                        Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}