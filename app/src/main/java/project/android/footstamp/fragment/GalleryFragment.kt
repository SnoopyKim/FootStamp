package project.android.footstamp.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import project.android.footstamp.R
import project.android.footstamp.R.id.pager
import project.android.footstamp.adapter.PagerAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import project.android.footstamp.StampApplication
import project.android.footstamp.adapter.GalleryViewAdapter
import project.android.footstamp.model.Stamp
import project.android.footstamp.viewmodel.StampViewModel
import project.android.footstamp.viewmodel.StampViewModelFactory

class GalleryFragment : Fragment() {

    var items = mutableListOf<Stamp>()
    lateinit var adapter: GalleryViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_gallery, container, false)

        //뷰페이저 미구현
        val ViewPager2 = view.findViewById<ViewPager2>(pager)
//        ViewPager2.adapter = PagerAdapter(GalleryFragment())


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