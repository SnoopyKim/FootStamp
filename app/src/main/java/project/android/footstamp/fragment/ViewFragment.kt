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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import project.android.footstamp.R
import project.android.footstamp.activity.SplashActivity
import project.android.footstamp.adapter.GalleryViewAdapter
import project.android.footstamp.databinding.FragmentViewBinding
import project.android.footstamp.model.Stamp

class ViewFragment : Fragment() {

   lateinit var binding: FragmentViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentViewBinding.inflate(layoutInflater)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_view, container, false)
        val rv : RecyclerView = view.findViewById(R.id.GalleryRCView)



        val items = ArrayList<String>()
        items.add("a")
        items.add("b")
        items.add("c")
        items.add("a")
        items.add("a")

        val rvAdapter = GalleryViewAdapter(items)
        rv.adapter = rvAdapter
        rv.layoutManager = LinearLayoutManager(context)

        return view
    }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

    }
}