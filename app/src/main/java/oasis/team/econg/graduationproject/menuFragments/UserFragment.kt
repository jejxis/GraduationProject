package oasis.team.econg.graduationproject.menuFragments

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import oasis.team.econg.graduationproject.*
import oasis.team.econg.graduationproject.databinding.FragmentUserBinding
import oasis.team.econg.graduationproject.retrofit.RetrofitManager
import oasis.team.econg.graduationproject.samplePreference.MyApplication
import oasis.team.econg.graduationproject.utils.Constants
import oasis.team.econg.graduationproject.utils.RESPONSE_STATE
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class UserFragment : Fragment() {
    lateinit var binding: FragmentUserBinding
    lateinit var main: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        main = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserBinding.inflate(inflater, container, false)

        var bitmap: Bitmap? = null
        val thread = object: Thread(){
            override fun run() {
                try{
                    var url = URL(MyApplication.prefs.picture)
                    val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
                    conn.connect()
                    val inputStream = conn.inputStream
                    bitmap =  BitmapFactory.decodeStream(inputStream)
                }catch(e: IOException){
                    e.printStackTrace()
                }
            }
        }
        if(!MyApplication.prefs.picture.isNullOrEmpty())
            thread.start()

        try{
            if(!MyApplication.prefs.picture.isNullOrEmpty()){
                thread.join()
                binding.profileImage.setImageBitmap(bitmap)
            }
            setScreen()
        }catch (e: InterruptedException){
            e.printStackTrace()
        }

        return binding.root
    }

    private fun setScreen(){
        binding.userName.text = "${MyApplication.prefs.nickname}님"

        binding.favoritePlants.setOnClickListener {
            val intent = Intent(main, FavoritePlantActivity::class.java)
            startActivity(intent)
        }
        binding.editUserInfo.setOnClickListener {
            val intent = Intent(main, UserInfoActivity::class.java)
            startActivity(intent)
        }

        binding.changePw.setOnClickListener {
            val intent = Intent(main, EditUserPwActivity::class.java)
            startActivity(intent)
        }

        binding.logout.setOnClickListener {
            MyApplication.prefs.logout()
            val intent = Intent(main, LoginActivity::class.java)
            startActivity(intent)
            main.finish()
        }
    }

}