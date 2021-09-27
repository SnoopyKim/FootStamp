package project.android.footstamp.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import androidx.constraintlayout.helper.widget.Carousel
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import project.android.footstamp.R
import project.android.footstamp.R.id.pager
import project.android.footstamp.activity.SplashActivity
import project.android.footstamp.adapter.GalleryViewAdapter
import project.android.footstamp.adapter.PagerAdapter
import project.android.footstamp.model.Stamp
import project.android.footstamp.viewmodel.StampViewModel
import java.util.*

class GalleryFragment : Fragment() {
    var items = mutableListOf<Stamp>()
    lateinit var galadapter : GalleryViewAdapter
    lateinit var PagerAdapter : PagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_view, container, false)
        val rv = view.findViewById<RecyclerView>(R.id.GalleryRCView)
        galadapter = GalleryViewAdapter(items)

        rv.adapter = galadapter
        rv.layoutManager = GridLayoutManager(context,3)

            galadapter.itemClick = object : GalleryViewAdapter.ItemClick{
            override fun onClick(view: View, Position: Int) {
                val intent = Intent(context, ViewFragment::class.java)
                startActivity(intent)
            }

        }
        val TabLayout = view.findViewById<TabLayout>(R.id.GalleryTab)
        val ViewPager = view.findViewById<ViewPager2>(pager)
        TabLayoutMediator(TabLayout,ViewPager){tab, position ->
            tab.text = "지역별 보기"
        }.attach();



        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val stampViewModel = StampViewModel(requireActivity().application)
        CoroutineScope(Dispatchers.Main).launch {
            stampViewModel.getAll().observe(viewLifecycleOwner, { stamps ->
             galadapter.setList(stamps)
            })
        }
        val addBtn = view.findViewById<FloatingActionButton>(R.id.addBtn)
        addBtn.setOnClickListener {
            val intent = Intent(context, SplashActivity::class.java)
            startActivity(intent)
        }
    }


        fun newInstance(param1: String, param2: String) =
            GalleryFragment().apply {
                // TODO: 일단 냅둬
            }
}
