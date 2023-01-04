package oasis.team.econg.graduationproject.menuFragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import oasis.team.econg.graduationproject.MainActivity
import oasis.team.econg.graduationproject.R
import oasis.team.econg.graduationproject.databinding.FragmentDiaryBinding
import oasis.team.econg.graduationproject.diaryFragments.MyPlantFragment

class DiaryFragment : Fragment() {
    lateinit var binding: FragmentDiaryBinding
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
        binding = FragmentDiaryBinding.inflate(inflater, container, false)

        setMenuPlant()

        binding.plant.setOnClickListener {
            setMenuPlant()
        }

        binding.schedule.setOnClickListener {
            setMenuSchedule()
        }
        return binding.root
    }

    private fun setMenuPlant(){
        binding.plant.setTextColor(Color.parseColor(("#ffffff")))
        binding.plant.setBackgroundResource(R.drawable.button_background)
        binding.schedule.setTextColor(Color.parseColor(("#000000")))
        binding.schedule.setBackgroundColor(Color.parseColor("#00ffffff"))

        showMyPlantFragment()
    }

    private fun setMenuSchedule(){
        binding.schedule.setTextColor(Color.parseColor(("#ffffff")))
        binding.schedule.setBackgroundResource(R.drawable.button_background)
        binding.plant.setTextColor(Color.parseColor(("#000000")))
        binding.plant.setBackgroundColor(Color.parseColor("#00ffffff"))
    }

    private fun showMyPlantFragment() {
        childFragmentManager.beginTransaction()
            .replace(R.id.diaryFrame, MyPlantFragment())
            .commitAllowingStateLoss()
    }

}