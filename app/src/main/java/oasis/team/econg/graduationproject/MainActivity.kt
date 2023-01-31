package oasis.team.econg.graduationproject

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import oasis.team.econg.graduationproject.databinding.ActivityMainBinding
import oasis.team.econg.graduationproject.menuFragments.*

class MainActivity : AppCompatActivity() {
    val binding by lazy{ActivityMainBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        /*Firebase.dynamicLinks.
        getDynamicLink(intent)
            .addOnSuccessListener(this){ pendingDynamicLinkData ->
                var deeplink: Uri? = null
                if(pendingDynamicLinkData != null){
                    deeplink = pendingDynamicLinkData.link
                }
                if(deeplink != null){
                    //val key = deeplink.getQueryParameter("key")
                }
            }*/

        binding.mainToolBar.selectedItemId = R.id.menuHome
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
                R.id.menuSearch -> {
                    showSearchFragment()
                    return@setOnItemSelectedListener true
                }
                R.id.menuUser -> {
                    showUserFragment()
                    return@setOnItemSelectedListener true
                }
                R.id.menuMap -> {
                    showMapFragment()
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

    private fun showSearchFragment(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.menuFrame, SearchFragment())
            .commitAllowingStateLoss()
    }

    private fun showUserFragment(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.menuFrame, UserFragment())
            .commitAllowingStateLoss()
    }

    private fun showMapFragment(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.menuFrame, MapFragment())
            .commitAllowingStateLoss()
    }
}