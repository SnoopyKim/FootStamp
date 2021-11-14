package project.android.footstamp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import project.android.footstamp.R
import project.android.footstamp.databinding.ActivityGalleryBinding
import project.android.footstamp.utils.FBAuth
import project.android.footstamp.utils.FBRef
import project.android.footstamp.utils.PostModel

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

        binding.Gedit.setOnClickListener {
            showDialog()


        }
    }

    private fun showDialog(){
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog,null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("게시글 편집")

        val alertDialog = mBuilder.show()
        alertDialog.findViewById<Button>(R.id.dEdit)?.setOnClickListener {
            val date =intent.getStringExtra("date")
            val area =intent.getStringExtra("area")
            val district =intent.getStringExtra("district")
            val key =intent.getStringExtra("key")
            val memo = intent.getStringExtra("memo")
            val intent = Intent(this,EditActivity::class.java)
            intent.putExtra("date",date.toString())
            intent.putExtra("area",area.toString())
            intent.putExtra("district",district.toString())
            intent.putExtra("key",key.toString())
            intent.putExtra("memo",memo.toString())
            alertDialog.dismiss()

            startActivity(intent)
        }
        alertDialog.findViewById<Button>(R.id.dDel)?.setOnClickListener {
            alertDialog.dismiss()

        }
        alertDialog.findViewById<Button>(R.id.dBoard)?.setOnClickListener {
            val time =intent.getStringExtra("date").toString()
            val Area =intent.getStringExtra("area").toString()
            val District =intent.getStringExtra("district").toString()
            val key =intent.getStringExtra("key").toString()
            val memo = intent.getStringExtra("memo").toString()
            val uid = FBAuth.getUid()

            FBRef.boardRef
                .child(key)
                .setValue(PostModel(Area, District, time, memo, key),uid)
            alertDialog.dismiss()
            Toast.makeText(applicationContext,"게시완료",Toast.LENGTH_SHORT).show()
        }

        }
    }

