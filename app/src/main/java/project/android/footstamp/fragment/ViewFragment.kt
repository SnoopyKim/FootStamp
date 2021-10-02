package project.android.footstamp.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import project.android.footstamp.R
import project.android.footstamp.activity.SplashActivity
import project.android.footstamp.adapter.GalleryViewAdapter
import project.android.footstamp.model.Stamp
import project.android.footstamp.viewmodel.StampViewModel

class ViewFragment : Fragment() {
    // TODO: Rename and change types of parameters
    var items = mutableListOf<Stamp>()
    lateinit var galadapter : GalleryViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_view, container, false)
        // Inflate the layout for this fragment
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
            ViewFragment().apply {

                }
            }

