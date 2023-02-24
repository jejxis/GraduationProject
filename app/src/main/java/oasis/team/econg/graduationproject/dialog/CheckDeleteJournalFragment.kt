package oasis.team.econg.graduationproject.dialog

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import oasis.team.econg.graduationproject.DiaryListActivity
import oasis.team.econg.graduationproject.R
import oasis.team.econg.graduationproject.databinding.FragmentCheckDeletecJournalBinding

class CheckDeleteJournalFragment : DialogFragment() {

    var _binding: FragmentCheckDeletecJournalBinding? = null
    private val binding get() = _binding!!
    lateinit var diaryListActivity: DiaryListActivity
    var checkDeleteListener : CheckDeleteListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        diaryListActivity = context as DiaryListActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCheckDeletecJournalBinding.inflate(inflater, container, false)
        binding.btnCheckDelete.setOnClickListener {
            checkDeleteListener!!.onDeleteClicked()
            this.dismiss()
        }
        binding.btnCheckCancel.setOnClickListener {
            this.dismiss()
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun setDialogListener(checkDeleteListener: CheckDeleteListener){
        this.checkDeleteListener = checkDeleteListener
    }

    interface CheckDeleteListener{
        fun onDeleteClicked()
    }
}