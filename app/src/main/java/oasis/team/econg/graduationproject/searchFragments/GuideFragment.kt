package oasis.team.econg.graduationproject.searchFragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import oasis.team.econg.graduationproject.MainActivity
import oasis.team.econg.graduationproject.R
import oasis.team.econg.graduationproject.databinding.FragmentGuideBinding
import oasis.team.econg.graduationproject.utils.Constants


class GuideFragment : Fragment() {

    private lateinit var binding: FragmentGuideBinding
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
        binding = FragmentGuideBinding.inflate(inflater, container, false)

        binding.btnGuideline.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(Constants.GUIDELINE))
            startActivity(intent)
        }

        return binding.root
    }

}