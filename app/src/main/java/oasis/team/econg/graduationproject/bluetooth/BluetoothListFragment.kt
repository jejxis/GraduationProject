package oasis.team.econg.graduationproject.bluetooth

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import oasis.team.econg.graduationproject.R
import oasis.team.econg.graduationproject.databinding.FragmentBluetoothListBinding

class BluetoothListFragment(context: Context, devices: MutableList<String>) : DialogFragment() {
    var deviceArray = devices
    private var _binding: FragmentBluetoothListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBluetoothListBinding.inflate(inflater, container, false)
        return binding.root
    }

}