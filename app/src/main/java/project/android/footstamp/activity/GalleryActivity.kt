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
import project.android.footstamp.utils.BoardModel
import project.android.footstamp.utils.FBAuth
import project.android.footstamp.utils.FBRef
import android.view.Gravity

import android.widget.TextView

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
        val url = intent.getStringExtra("url")

        binding.GDateText.setText(date)
        binding.Garea.setText(area)
        binding.Gdistrict.setText(district)
        binding.Gmemo.setText(memo)
        val storageReference = Firebase.storage.reference.child(key + ".png")

        Glide.with(this)
            .load(url)
            .into(binding.Gimage)

        binding.Gedit.setOnClickListener {
            showDialog()


        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        finish()
//    }

    private fun showDialog(){

        val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog,null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)

        val alertDialog = mBuilder.show()
        alertDialog.findViewById<Button>(R.id.dEdit)?.setOnClickListener {
            val date =intent.getStringExtra("date")
            val area =intent.getStringExtra("area")
            val district =intent.getStringExtra("district")
            val key =intent.getStringExtra("key")
            val memo = intent.getStringExtra("memo")
            val url = intent.getStringExtra("url")
            val intent = Intent(this,EditActivity::class.java)

            intent.putExtra("date",date.toString())
            intent.putExtra("area",area.toString())
            intent.putExtra("district",district.toString())
            intent.putExtra("key",key.toString())
            intent.putExtra("memo",memo.toString())
            intent.putExtra("url",url.toString())
            alertDialog.dismiss()
            finish()
            startActivity(intent)
        }
        alertDialog.findViewById<Button>(R.id.dDel)?.setOnClickListener {
            alertDialog.dismiss()
            showDialog3()

        }
        alertDialog.findViewById<Button>(R.id.dBoard)?.setOnClickListener {
            alertDialog.dismiss()
            showDialog2()
        }

        }
    private fun showDialog2() {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog2, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("????????? ???????????? ??????????????? ??? ??? ????????? ?????????????")

        val alertDialog = mBuilder.show()
        alertDialog.findViewById<Button>(R.id.dyes)?.setOnClickListener {
            val time =intent.getStringExtra("date").toString()
            val Area =intent.getStringExtra("area").toString()
            val District =intent.getStringExtra("district").toString()
            val key =intent.getStringExtra("key").toString()
            val memo = intent.getStringExtra("memo").toString()
            val uid = FBAuth.getUid()


            FBRef.boardRef
                .child(key)
                .setValue(BoardModel(Area, District, time, memo, key, uid))
            alertDialog.dismiss()
            Toast.makeText(applicationContext,"??????????????????",Toast.LENGTH_SHORT).show()
        }
        alertDialog.findViewById<Button>(R.id.dno)?.setOnClickListener {
            alertDialog.dismiss()
            showDialog()
        }
    }
    private fun showDialog3() {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog2, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("????????? ???????????? ?????????????????? ???????????? ?????????????")

        val alertDialog = mBuilder.show()
        alertDialog.findViewById<Button>(R.id.dyes)?.setOnClickListener {
            val uid = FBAuth.getUid()
            val key = intent.getStringExtra("key")
            FBRef.uidRef.child(uid).child(key.toString()).removeValue()
            FBRef.boardRef.child(key.toString()).removeValue()
            Toast.makeText(applicationContext, "?????????????????????", Toast.LENGTH_SHORT).show()
            alertDialog.dismiss()
            finish()
        }

            alertDialog.findViewById<Button>(R.id.dno)?.setOnClickListener {
                alertDialog.dismiss()
                showDialog()
            }
    }
}


