package oasis.team.econg.graduationproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import oasis.team.econg.graduationproject.databinding.ActivityUserInfoBinding

class UserInfoActivity : AppCompatActivity() {
    val binding by lazy{ActivityUserInfoBinding.inflate(layoutInflater)}
    private lateinit var pictureAdder: PictureAdder
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnClose.setOnClickListener {
            finish()
        }

        pictureAdder = PictureAdder(this, binding.userProfile)

        binding.btnEditProfile.setOnClickListener {
            binding.editUserNickname.isEnabled = true
            binding.editUserPw.isEnabled = true
            binding.editUserProfileText.visibility = View.VISIBLE
            binding.userProfile.isClickable = true
            binding.userProfile.setOnClickListener {
                pictureAdder.getImage()
            }
            binding.whenEdit.visibility = View.VISIBLE
            binding.btnEditProfile.visibility = View.GONE
        }

        binding.btnProfileCancel.setOnClickListener {
            binding.editUserNickname.isEnabled = false
            binding.editUserPw.isEnabled = false
            binding.editUserProfileText.visibility = View.GONE
            binding.userProfile.isClickable = false
            binding.whenEdit.visibility = View.GONE
            binding.btnEditProfile.visibility = View.VISIBLE
        }

        binding.btnProfileSave.setOnClickListener {}
    }
}