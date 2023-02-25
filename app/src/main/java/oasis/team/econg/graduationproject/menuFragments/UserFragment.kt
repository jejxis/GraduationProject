package oasis.team.econg.graduationproject.menuFragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import oasis.team.econg.graduationproject.*
import oasis.team.econg.graduationproject.databinding.FragmentUserBinding
import oasis.team.econg.graduationproject.samplePreference.MyApplication

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
        }
        return binding.root
    }

}