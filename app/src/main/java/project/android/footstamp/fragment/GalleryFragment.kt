package project.android.footstamp.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import project.android.footstamp.R
import project.android.footstamp.StampApplication
import project.android.footstamp.activity.ViewActivity
import project.android.footstamp.adapter.GalleryViewAdapter
import project.android.footstamp.model.Stamp
import project.android.footstamp.viewmodel.StampViewModel
import project.android.footstamp.viewmodel.StampViewModelFactory

class GalleryFragment : Fragment() {

    private val stampViewModel: StampViewModel by activityViewModels {
        StampViewModelFactory(
            (activity?.application as StampApplication).repository
        )
    }

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

        val rv = view.findViewById<RecyclerView>(R.id.GalleryRCView)
        adapter = GalleryViewAdapter(items)

        rv.adapter = adapter
        rv.layoutManager = GridLayoutManager(context,3)

        adapter.itemClick = object : GalleryViewAdapter.ItemClick{
            override fun onClick(view: View, Position: Int) {
                val intent = Intent(context, ViewActivity::class.java)
                startActivity(intent)
            }

        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        stampViewModel.allStamps.observe(viewLifecycleOwner, { stamps ->
            adapter.setList(stamps)
            Log.d("GalleryFragment", "observe: ${stamps.size} ${items.size} ${adapter.itemCount}")
        })
    }

    fun newInstance(param1: String, param2: String) =
        GalleryFragment().apply {
            // TODO: 일단 냅둬
        }
}
