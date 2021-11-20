package project.android.footstamp.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import project.android.footstamp.R
import project.android.footstamp.adapter.CommentRVAdapter
import project.android.footstamp.databinding.ActivityBoardInsideBinding
import project.android.footstamp.model.CommentModel
import project.android.footstamp.utils.FBAuth
import project.android.footstamp.utils.FBRef

class BoardInsideActivity : AppCompatActivity() {
    lateinit var binding: ActivityBoardInsideBinding
    private val commentDataList = mutableListOf<CommentModel>()
    private lateinit var commentAdapter : CommentRVAdapter
    private lateinit var key : String

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityBoardInsideBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val date = intent.getStringExtra("date").toString()
        val area = intent.getStringExtra("area").toString()
        val district = intent.getStringExtra("district").toString()
        val key = intent.getStringExtra("key").toString()
        val memo = intent.getStringExtra("memo").toString()
        val uid = intent.getStringExtra("uid").toString()
        val myid = FBAuth.getUid().toString()
        val storageReference = Firebase.storage.reference.child(key + ".png")

        Log.d("TAG",myid)
        Log.d("TAG",uid)

        binding.insDate.setText(date)
        binding.insMemo.setText(memo)
        binding.insDistrict.setText(area+" "+district)

        commentAdapter = CommentRVAdapter(commentDataList,uid)
        binding.insRV.adapter = commentAdapter
        binding.insRV.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        getCommentData(key)

        binding.inscomBtn.setOnClickListener{
            insertComment(key)
            softkeyboardHide()
        }
        binding.indBtn.setOnClickListener{
            showDialog()
        }

        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {

                Glide.with(this)
                    .load(task.result)
                    .into(binding.insImage)
            }
        })

        if (uid.equals(myid)){
            binding.indBtn.isVisible = true
        } else {
            binding.indBtn.isVisible = false
        }
    }
    fun insertComment(key : String){
        //comment
        //  -BoardKey
        //      -CommentKey
        //          -CommentData
        //          -CommentData
        //          -CommentData
        FBRef
            .commentRef
            .child(key)
            .push()
            .setValue(
                CommentModel(binding.insComarea.text.toString(),FBAuth.getTime(),FBAuth.getUid())
            )

        Toast.makeText(this, "댓글이 작성되었습니다", Toast.LENGTH_SHORT).show()
        binding.insComarea.setText("")
    }

    fun getCommentData(key: String){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                commentDataList.clear()
                for (dataModel in dataSnapshot.children) {
                    val item = dataModel.getValue(CommentModel::class.java)
                    commentDataList.add(item!!)
                }
                commentAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("ContentsListActivity", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.commentRef.child(key).addValueEventListener(postListener)
    }
    fun softkeyboardHide() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.insComarea.windowToken, 0)
    }
    private fun showDialog() {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog2, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("일기를 삭제하면 게시판에서만 사라져요 괜찮나요?")

        val alertDialog = mBuilder.show()

        alertDialog.findViewById<Button>(R.id.dyes)?.setOnClickListener {
            val key = intent.getStringExtra("key")
            FBRef.boardRef.child(key.toString()).removeValue()
            finish()
            Toast.makeText(applicationContext, "삭제되었습니다", Toast.LENGTH_SHORT).show()
            alertDialog.dismiss()
        }
            alertDialog.findViewById<Button>(R.id.dno)?.setOnClickListener {
                alertDialog.dismiss()
                showDialog()

        }
    }
}