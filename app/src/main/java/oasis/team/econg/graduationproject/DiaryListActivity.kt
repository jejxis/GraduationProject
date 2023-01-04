package oasis.team.econg.graduationproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import oasis.team.econg.graduationproject.databinding.ActivityDiaryListBinding

class DiaryListActivity : AppCompatActivity() {
    val binding by lazy{ActivityDiaryListBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.rvDiary.addItemDecoration(DividerItemDecoration(this, LinearLayout.VERTICAL))
    }
}