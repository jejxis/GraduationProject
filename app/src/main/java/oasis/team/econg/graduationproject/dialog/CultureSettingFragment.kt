package oasis.team.econg.graduationproject.dialog

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import oasis.team.econg.graduationproject.AddPlantActivity
import oasis.team.econg.graduationproject.R
import oasis.team.econg.graduationproject.databinding.FragmentCultureSettingBinding

class CultureSettingFragment : DialogFragment() {
    var _binding: FragmentCultureSettingBinding? = null
    private val binding get() = _binding!!
    lateinit var addPlant: AddPlantActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        addPlant = context as AddPlantActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCultureSettingBinding.inflate(inflater, container, false)
        binding.btnOK.setOnClickListener {
            addPlant.setCultureSetting(binding.waterCycle.text.toString())
            this.dismiss()
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}