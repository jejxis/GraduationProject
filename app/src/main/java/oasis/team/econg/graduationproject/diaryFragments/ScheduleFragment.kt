package oasis.team.econg.graduationproject.diaryFragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import oasis.team.econg.graduationproject.MainActivity
import oasis.team.econg.graduationproject.R
import oasis.team.econg.graduationproject.data.Schedule
import oasis.team.econg.graduationproject.databinding.FragmentScheduleBinding
import oasis.team.econg.graduationproject.rvAdapter.ScheduleAdapter

class ScheduleFragment : Fragment() {

    lateinit var binding: FragmentScheduleBinding
    lateinit var main: MainActivity

    var schedules = mutableListOf<Schedule>()
    lateinit var scheduleAdapter: ScheduleAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        main = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentScheduleBinding.inflate(inflater, container, false)

        scheduleAdapter = ScheduleAdapter(main)
        loadData()
        setAdapter()
        return binding.root
    }

    private fun setAdapter() {
        scheduleAdapter.setData(schedules)
        binding.rvSchedule.layoutManager = LinearLayoutManager(main, LinearLayoutManager.VERTICAL, false)
        binding.rvSchedule.adapter = scheduleAdapter
    }

    private fun loadData(){
        for(i in 0..17){
            schedules.add(
                Schedule(
                    "$i",
                    "식물$i",
                    i%2==0
                )
            )
        }
    }

}