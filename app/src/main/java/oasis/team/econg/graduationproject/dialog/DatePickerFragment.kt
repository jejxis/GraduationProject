package oasis.team.econg.graduationproject.dialog

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import oasis.team.econg.graduationproject.AddPlantActivity
import oasis.team.econg.graduationproject.MainActivity
import oasis.team.econg.graduationproject.R
import oasis.team.econg.graduationproject.databinding.FragmentDatePickerBinding
import java.util.*

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener  {
    private var _binding: FragmentDatePickerBinding? = null
    private val binding get() = _binding!!
    private lateinit var addPlant: AddPlantActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        addPlant = context as AddPlantActivity
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDatePickerBinding.inflate(inflater,container,false)
        val view = binding.root

        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c: Calendar = Calendar.getInstance()
        val year: Int = c.get(Calendar.YEAR)
        val month: Int = c.get(Calendar.MONTH)
        val day: Int = c.get(Calendar.DAY_OF_MONTH)
        return DatePickerDialog(addPlant, this, year, month, day)
    }

    override fun onDateSet(datePicker: DatePicker?, year: Int, month: Int, day: Int) {
        addPlant.processDatePickerResult(year,month,day)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}