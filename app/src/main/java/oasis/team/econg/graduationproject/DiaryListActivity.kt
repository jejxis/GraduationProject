package oasis.team.econg.graduationproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import oasis.team.econg.graduationproject.data.Diary
import oasis.team.econg.graduationproject.databinding.ActivityDiaryListBinding
import oasis.team.econg.graduationproject.rvAdapter.DiaryAdapter

class DiaryListActivity : AppCompatActivity() {
    val binding by lazy{ActivityDiaryListBinding.inflate(layoutInflater)}
    var diaryList = mutableListOf<Diary>()
    var diaryAdapter = DiaryAdapter(this)
    var id: Long = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnClose.setOnClickListener {
            finish()
        }

        if(intent.hasExtra("id")){
            id = intent.getLongExtra("id", -1)
        }

        binding.addDiary.setOnClickListener {
            var intent = Intent(this, AddDiaryActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }
        loadData()
        setAdapter()

    }

    private fun loadData(){
        for(i in 0..17){
            diaryList.add(
                Diary(
                    "$i",
                    "2023.01.$i",
                    "${i}요일",
                    "안고 그들에게 밝은 길을 찾아 주며 그들을 행복스럽고 평화스러운 곳으로 인도하겠다는 커다란 이상을 품었기 때문이다 그러므로 그들은 길지 아니한 목숨을 사는가 싶이 살았으며 그들의 그림자는 천고에 사라지지 않는 것이다 이것은 현저하게 일월과"
                )
            )
        }
    }

    private fun setAdapter(){
        binding.rvDiary.addItemDecoration(DividerItemDecoration(this, LinearLayout.VERTICAL))
        diaryAdapter.setData(diaryList)
        binding.rvDiary.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvDiary.adapter = diaryAdapter
    }
}