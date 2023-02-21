package oasis.team.econg.graduationproject.menuFragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import oasis.team.econg.graduationproject.DetailPlantSpeciesActivity
import oasis.team.econg.graduationproject.MainActivity
import oasis.team.econg.graduationproject.R
import oasis.team.econg.graduationproject.data.PlantSpecies
import oasis.team.econg.graduationproject.databinding.FragmentSearchBinding
import oasis.team.econg.graduationproject.rvAdapter.PlantSpeciesAdapter
import oasis.team.econg.graduationproject.searchFragments.AllGardenFragment
import oasis.team.econg.graduationproject.searchFragments.GuideFragment
import oasis.team.econg.graduationproject.searchFragments.SearchResultFragment
import oasis.team.econg.graduationproject.utils.Constants.GUIDELINE

class SearchFragment : Fragment() {

    lateinit var binding: FragmentSearchBinding
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
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        showGuideFragment()

        binding.searchView.setOnClickListener {
            binding.btnShowGuide.visibility = View.VISIBLE
            showAllGardenFragment()
        }

        binding.btnShowGuide.setOnClickListener {
            showGuideFragment()
            it.visibility = View.GONE
        }

        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(key: String?): Boolean {
                if(!key.isNullOrEmpty()){
                    showSearchResultFragment(key)
                }
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }
        })

        return binding.root
    }

    private fun showAllGardenFragment(){
        childFragmentManager.beginTransaction()
            .replace(R.id.searchFrame, AllGardenFragment())
            .commitAllowingStateLoss()
    }

    private fun showSearchResultFragment(key:String){
        childFragmentManager.beginTransaction()
            .replace(R.id.searchFrame, SearchResultFragment().newInstance(key))
            .commitAllowingStateLoss()
    }

    private fun showGuideFragment(){
        childFragmentManager.beginTransaction()
            .replace(R.id.searchFrame, GuideFragment())
            .commitAllowingStateLoss()
    }

}