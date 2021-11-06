package project.android.footstamp.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import project.android.footstamp.adapter.ViewAdapter
import project.android.footstamp.databinding.FragmentViewBinding
import project.android.footstamp.utils.FBAuth
import project.android.footstamp.utils.FBRef
import project.android.footstamp.utils.PostModel
import project.android.footstamp.utils.getDistrictsFromArea

class ViewFragment : Fragment() {

   lateinit var binding: FragmentViewBinding
   private val postDataList = mutableListOf<PostModel>()
    private lateinit var rvAdapter : GalleryViewAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var currentArea :String
    private lateinit var currentDistrict :String
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        database = Firebase.database.reference
        binding = FragmentViewBinding.inflate(layoutInflater)


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
        sv.layoutManager = GridLayoutManager(view.context,3)

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
                svAdapter = ViewAdapter(view!!.context, districts.toMutableList())
                sv.adapter = svAdapter
                currentArea = stringArea
                Toast.makeText(context,currentArea,Toast.LENGTH_SHORT).show()

                //area선택에 따른 뷰 전환
                getFBData(currentArea)

                svAdapter.itemClick = object: ViewAdapter.ItemClick{
                    override fun onClick(view: View,position: Int){

                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
        rvAdapter = GalleryViewAdapter(view.context, postDataList)
        rv.adapter = rvAdapter

        rv.layoutManager = LinearLayoutManager(context)

        getFBData(currentArea)
        return view
    }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

    }

    private fun getFBData(currentArea:String){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                postDataList.clear()

                for (dataModel in dataSnapshot.children) {
                    val area = currentArea
                    val item = dataModel.getValue(PostModel::class.java)
                    val query = database.child(area).orderByChild("height")
                    postDataList.add(item!!)
                }
                //데이터 대입
                postDataList.reverse()
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