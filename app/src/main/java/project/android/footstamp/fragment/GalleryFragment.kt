package project.android.footstamp.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import project.android.footstamp.R
import project.android.footstamp.adapter.PagerAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import project.android.footstamp.R.id.*
import project.android.footstamp.StampApplication
import project.android.footstamp.adapter.GalleryViewAdapter
import project.android.footstamp.model.Stamp
import project.android.footstamp.viewmodel.StampViewModel
import project.android.footstamp.viewmodel.StampViewModelFactory

class GalleryFragment : Fragment() {

    var items = mutableListOf<Stamp>()
    lateinit var adapter: GalleryViewAdapter
    lateinit var tabLayout: TabLayout



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_gallery, container, false)

        val Gpager = view.findViewById<ViewPager2>(R.id.pager)
        val fragmentList = listOf(ViewFragment(),ViewFragment2())
        val adapter = PagerAdapter(requireActivity())
        adapter.fragments = fragmentList
        Gpager.adapter = adapter


        val Gtab = view.findViewById<TabLayout>(R.id.GalleryTab)
        TabLayoutMediator(Gtab,Gpager){tab, position -> tab.text = "Tab"}
            .attach()




        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fun newInstance(param1: String, param2: String) =
            GalleryFragment().apply {
                // TODO: 일단 냅둬
            }
    }
}