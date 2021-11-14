package project.android.footstamp.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import project.android.footstamp.adapter.BoardAdapter
import project.android.footstamp.databinding.FragmentPostBinding
import project.android.footstamp.utils.FBAuth
import project.android.footstamp.utils.FBRef
import project.android.footstamp.utils.PostModel


class PostFragment : Fragment() {
    private val boardDataList = mutableListOf<PostModel>()
    lateinit var binding: FragmentPostBinding
    private lateinit var rvAdapter : BoardAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private val uid = FBAuth.getUid()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = FragmentPostBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        database = Firebase.database.reference
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val rv = binding.PostRV

        rvAdapter = BoardAdapter(requireContext(),boardDataList,uid)
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

                boardDataList.clear()

                for (dataModel in dataSnapshot.children) {
                    val item = dataModel.getValue(PostModel::class.java)
                    boardDataList.add(item!!)
                }
                //데이터 대입
                boardDataList.reverse()
                rvAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("ContentsListActivity", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.boardRef.addValueEventListener(postListener)
    }
}
