package project.android.footstamp.fragment

import android.content.Intent
import android.os.Binder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import project.android.footstamp.R
import project.android.footstamp.adapter.PagerAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import project.android.footstamp.R.id.*
import project.android.footstamp.StampApplication
import project.android.footstamp.activity.PostActivity
import project.android.footstamp.adapter.GalleryViewAdapter
import project.android.footstamp.databinding.FragmentGalleryBinding
import project.android.footstamp.databinding.FragmentView2Binding
import project.android.footstamp.databinding.FragmentViewBinding
import project.android.footstamp.model.Stamp
import project.android.footstamp.viewmodel.StampViewModel
import project.android.footstamp.viewmodel.StampViewModelFactory

class GalleryFragment : Fragment() {
    lateinit var binding: FragmentGalleryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentGalleryBinding.inflate(layoutInflater)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        // Inflate the layout for this fragment

        val fragmentList = listOf(ViewFragment(),ViewFragment2())
        val adapter = PagerAdapter(requireActivity())
        adapter.fragments = fragmentList
        binding.pager.adapter = adapter

        TabLayoutMediator(binding.GalleryTab,binding.pager){tab, position ->
        if(position == 0){
            tab.text = "지역별 보기"
        } else {
            tab.text = "날짜순 보기"}
        }
            .attach()



        binding.GFloatingBtn.setOnClickListener {
            val intent = Intent(context,PostActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fun newInstance(param1: String, param2: String) =
            GalleryFragment().apply {
                // TODO: 일단 냅둬
            }
    }
}