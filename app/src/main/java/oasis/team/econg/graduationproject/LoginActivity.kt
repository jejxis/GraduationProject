package oasis.team.econg.graduationproject

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import oasis.team.econg.graduationproject.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    val binding by lazy{ActivityLoginBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("www.naver.com"))
            startActivity(intent)
        }
    }
}