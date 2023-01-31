package oasis.team.econg.graduationproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import oasis.team.econg.graduationproject.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {
    val binding by lazy{ActivitySignupBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var pictureAdder = PictureAdder(this, binding.signupProfile)

        binding.siggnupChangeProfile.setOnClickListener{
            pictureAdder.launch()
        }

        binding.btnGoToLogin.setOnClickListener {
            val intent = Intent(this@SignupActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}