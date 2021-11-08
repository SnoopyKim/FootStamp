package project.android.footstamp.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import project.android.footstamp.R
import project.android.footstamp.adapter.GalleryViewAdapter
import project.android.footstamp.adapter.GalleryViewAdapter2
import project.android.footstamp.databinding.FragmentView2Binding
import project.android.footstamp.model.Stamp
import project.android.footstamp.utils.*

class ViewFragment2 : Fragment() {
    // TODO: Rename and change types of parameters
    lateinit var binding: FragmentView2Binding
    private val postDataList = mutableListOf<PostModel>()
    private lateinit var rvAdapter : GalleryViewAdapter2
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private var area = getAreas()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        database = Firebase.database.reference
        binding = FragmentView2Binding.inflate(layoutInflater)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val rv = binding.GalleryRCView2
        resetUI()

        binding.viewSpn3.adapter =
            ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, area)
        binding.viewSpn3.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                binding.viewSpn4.adapter = ArrayAdapter(requireContext(),
                    R.layout.support_simple_spinner_dropdown_item,
                    getDistrictsFromArea(area[position]))

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
        rvAdapter = GalleryViewAdapter2(requireContext(), postDataList)
        rv.adapter = rvAdapter

        rv.layoutManager = LinearLayoutManager(context)

        getFBData()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun getFBData(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                postDataList.clear()

                for (dataModel in dataSnapshot.children) {
                    val item = dataModel.getValue(PostModel::class.java)
                    postDataList.add(item!!)
                }
                //데이터 대입
                rvAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("ContentsListActivity", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.uidRef.child(FBAuth.getUid()).addValueEventListener(postListener)
    }
    private fun resetUI() {
        binding.apply {
            viewSpn3.setSelection(0)
            viewSpn4.setSelection(0)
        }
    }
}
