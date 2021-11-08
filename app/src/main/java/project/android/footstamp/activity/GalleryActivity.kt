package project.android.footstamp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import project.android.footstamp.R
import project.android.footstamp.databinding.ActivityGalleryBinding

class GalleryActivity : AppCompatActivity() {
    lateinit var binding: ActivityGalleryBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityGalleryBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val date =intent.getStringExtra("date")
        val area =intent.getStringExtra("area")
        val district =intent.getStringExtra("district")
        val key =intent.getStringExtra("key")
        val memo = intent.getStringExtra("memo")

        binding.GDateText.setText(date + "에 ")
        binding.Garea.setText(area)
        binding.Gdistrict.setText(district + " 에서")
        binding.Gmemo.setText(memo)
        val storageReference = Firebase.storage.reference.child(key + ".png")

        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {

                Glide.with(this)
                    .load(task.result)
                    .into(binding.Gimage)
            }
        })
    }
}