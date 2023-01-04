package oasis.team.econg.graduationproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import oasis.team.econg.graduationproject.databinding.ActivityMainBinding
import oasis.team.econg.graduationproject.menuFragments.DiaryFragment
import oasis.team.econg.graduationproject.menuFragments.HomeFragment

class MainActivity : AppCompatActivity() {
    val binding by lazy{ActivityMainBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        showHomeFragment()

        getNavi()
    }

    private fun getNavi(){
        binding.mainToolBar.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.menuHome -> {
                    showHomeFragment()
                    return@setOnItemSelectedListener true
                }
                R.id.menuDiary -> {
                    showDiaryFragment()
                    return@setOnItemSelectedListener true
                }
                else -> return@setOnItemSelectedListener true
            }
        }
    }

    private fun showHomeFragment(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.menuFrame, HomeFragment())
            .commitAllowingStateLoss()
    }

    private fun showDiaryFragment(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.menuFrame, DiaryFragment())
            .commitAllowingStateLoss()
    }
}