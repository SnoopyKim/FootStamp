package project.android.footstamp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import project.android.footstamp.R
import project.android.footstamp.databinding.ActivityBoardInsideBinding
import project.android.footstamp.utils.FBAuth

class BoardInsideActivity : AppCompatActivity() {
    lateinit var binding: ActivityBoardInsideBinding

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
        binding.indMemo.setText(memo)
        binding.insDistrict.setText(area+" "+district)

        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {

                Glide.with(this)
                    .load(task.result)
                    .into(binding.insImage)
            }
        })

        if (uid.equals(myid)){
            binding.indBtn.isVisible = true
        }else {
            binding.indBtn.isVisible = false
        }
    }
}