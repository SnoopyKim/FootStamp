package project.android.footstamp.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import project.android.footstamp.R
import project.android.footstamp.activity.ViewActivity
import project.android.footstamp.adapter.ContentsModel
import project.android.footstamp.adapter.GalleryVIewAdapter

class GalleryFragment : Fragment() {
    val items = mutableListOf<ContentsModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_gallery, container, false)


        items.add(ContentsModel("날짜 위치",
                                 "메모",
                                   "사진"))

        val rv = view.findViewById<RecyclerView>(R.id.GalleryRCView)
        val rvAdapter = GalleryVIewAdapter(items)

        rv.adapter = rvAdapter
        rv.layoutManager = GridLayoutManager(context,3)

        rvAdapter.itemClick = object : GalleryVIewAdapter.ItemClick{
            override fun onClick(view: View, Position: Int) {
                val intent = Intent(context, ViewActivity::class.java)
                startActivity(intent)
            }

        }

        return view
    }

        fun newInstance(param1: String, param2: String) =
            GalleryFragment().apply {
                // TODO: 일단 냅둬
            }
}
