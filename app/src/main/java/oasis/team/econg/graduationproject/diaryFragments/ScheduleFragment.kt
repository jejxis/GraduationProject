package oasis.team.econg.graduationproject.diaryFragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import oasis.team.econg.graduationproject.MainActivity
import oasis.team.econg.graduationproject.R
import oasis.team.econg.graduationproject.data.Schedule
import oasis.team.econg.graduationproject.databinding.FragmentScheduleBinding
import oasis.team.econg.graduationproject.retrofit.RetrofitManager
import oasis.team.econg.graduationproject.rvAdapter.ScheduleAdapter
import oasis.team.econg.graduationproject.utils.API
import oasis.team.econg.graduationproject.utils.Constants.TAG
import oasis.team.econg.graduationproject.utils.RESPONSE_STATE
import java.text.SimpleDateFormat
import java.util.*

class ScheduleFragment : Fragment() {

    lateinit var binding: FragmentScheduleBinding
    lateinit var main: MainActivity

    var schedules = mutableListOf<Schedule>()
    var dateSchedules = mutableListOf<Schedule>()
    lateinit var scheduleAdapter: ScheduleAdapter
    var selectedDate = ""

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
        initDate()
        scheduleAdapter = ScheduleAdapter(main)
        loadData()
        
        binding.calendar.setOnDateChangeListener { view, y, m, d ->
            val year = y.toString()
            val month = String.format("%02d", m+1)
            val day = String.format("%02d", d)
            selectedDate = year + month + day
            Log.d(TAG, "selectedDate: $selectedDate")

            changeData(selectedDate)
            scheduleAdapter.setData(dateSchedules)
            scheduleAdapter.notifyDataSetChanged()
        }
        return binding.root
    }

    private fun setAdapter() {
        scheduleAdapter.setData(dateSchedules)
        binding.rvSchedule.layoutManager = LinearLayoutManager(main, LinearLayoutManager.VERTICAL, false)
        binding.rvSchedule.adapter = scheduleAdapter
    }

    private fun loadData(){
        RetrofitManager.instance.getCalendars(API.HEADER_TOKEN, completion = {
            responseState, responseBody->
            when(responseState){
                RESPONSE_STATE.OKAY -> {
                    Log.d(TAG, "ScheduleFragment - getCalendars: SUCCESS")
                    schedules = responseBody
                    if(selectedDate.isNotEmpty()) {
                        changeData(selectedDate)
                        setAdapter()
                    }
                }
                RESPONSE_STATE.FAIL -> {
                    Log.d(TAG, "ScheduleFragment - getCalendars: FAIL")
                    Toast.makeText(main, "데이터를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun initDate(){
        val now: Long = System.currentTimeMillis()
        val date = Date(now)
        val sdf = SimpleDateFormat("yyyyMMdd")
        selectedDate = sdf.format(date)
    }
    
    private fun changeData(date: String){
        val temp = mutableListOf<Schedule>()
        schedules.forEach { 
            if(date == it.date){
                temp.add(it)
            }
        }
        dateSchedules = temp
    }

}