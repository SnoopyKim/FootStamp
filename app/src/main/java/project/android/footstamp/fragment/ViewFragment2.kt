package project.android.footstamp.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import project.android.footstamp.R
import project.android.footstamp.adapter.GalleryViewAdapter
import project.android.footstamp.adapter.ViewAdapter
import project.android.footstamp.databinding.FragmentView2Binding
import project.android.footstamp.model.Stamp
import project.android.footstamp.utils.FBAuth
import project.android.footstamp.utils.FBRef
import project.android.footstamp.utils.PostModel
import project.android.footstamp.utils.getDistrictsFromArea

class ViewFragment2 : Fragment() {
    // TODO: Rename and change types of parameters
    var items = mutableListOf<Stamp>()
    lateinit var galadapter : GalleryViewAdapter
    lateinit var binding: FragmentView2Binding
    private val postDataList = mutableListOf<PostModel>()
    private lateinit var rvAdapter : GalleryViewAdapter
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding = FragmentView2Binding.inflate(layoutInflater)


    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_view, container, false)
        val rv = view.findViewById<RecyclerView>(R.id.GalleryRCView)
        val sv = view.findViewById<RecyclerView>(R.id.spinRV)
        val item = mutableListOf<String>()

        var svAdapter = ViewAdapter(view.context,item)
        sv.adapter = svAdapter
        sv.layoutManager = GridLayoutManager(requireContext(),3)

        //스피너 설정
        val spinner = view.findViewById<Spinner>(R.id.viewSpn)
        ArrayAdapter.createFromResource(
            requireContext(),R.array.central_array,android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        //스피너 아이템 선택 리스너
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                val stringArea = parent?.getItemAtPosition(position).toString()
                val districts = getDistrictsFromArea(stringArea)
                svAdapter = ViewAdapter(view!!.context,districts.toMutableList())
                sv.adapter = svAdapter

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
        rvAdapter = GalleryViewAdapter(requireContext(), postDataList)
        rv.adapter = rvAdapter

        rv.layoutManager = LinearLayoutManager(context)

        getFBData()
        return view
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
}