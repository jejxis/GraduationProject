package oasis.team.econg.graduationproject.menuFragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import oasis.team.econg.graduationproject.MainActivity
import oasis.team.econg.graduationproject.R
import oasis.team.econg.graduationproject.databinding.FragmentSearchBinding
import oasis.team.econg.graduationproject.searchFragments.AllGardenFragment
import oasis.team.econg.graduationproject.searchFragments.GuideFragment
import oasis.team.econg.graduationproject.searchFragments.SearchResultFragment

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

        showAllGardenFragment()
        binding.searchView.setOnClickListener {
            binding.btnShowAll.visibility = View.VISIBLE
        }

        binding.btnShowAll.setOnClickListener {
            showAllGardenFragment()
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

}