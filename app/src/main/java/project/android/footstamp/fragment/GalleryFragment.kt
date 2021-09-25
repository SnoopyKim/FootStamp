package project.android.footstamp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import project.android.footstamp.R
import project.android.footstamp.adapter.GalleryVIewAdapter

class GalleryFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_gallery, container, false)

        val items = mutableListOf<String>()
        items.add("날짜 샘플1")
        items.add("날짜 샘플2")
        items.add("날짜 샘플3")
        items.add("날짜 샘플4")

        val rv = view.findViewById<RecyclerView>(R.id.GalleryRCView)
        val rvAdapter = GalleryVIewAdapter(items)

        rv.adapter = rvAdapter
        rv.layoutManager = LinearLayoutManager(context)

        return view
    }

    fun newInstance(param1: String, param2: String) =
        GalleryFragment().apply {
            // TODO: 일단 냅둬
        }
}
