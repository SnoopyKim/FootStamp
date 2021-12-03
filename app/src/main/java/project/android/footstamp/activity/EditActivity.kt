package project.android.footstamp.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.Keep
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import project.android.footstamp.R
import project.android.footstamp.databinding.ActivityEditBinding
import project.android.footstamp.utils.*
import java.io.ByteArrayOutputStream
import java.util.*

class EditActivity : AppCompatActivity() {
    lateinit var binding: ActivityEditBinding
    private lateinit var imageBuffer: ByteArray
    private var postList: List<PostModel> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityEditBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val pickImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val uri = result.data?.data
            if (uri != null) {
                imageBuffer = this.contentResolver?.openInputStream(uri)?.buffered()?.use { it.readBytes() }!!
                binding.Eimage.setImageURI(uri)
            }
        }
        val date =intent.getStringExtra("date").toString()
        val area =intent.getStringExtra("area").toString()
        val district =intent.getStringExtra("district").toString()
        val key =intent.getStringExtra("key").toString()
        val memo = intent.getStringExtra("memo").toString()
        val url = intent.getStringExtra("url").toString()
        val storageReference = Firebase.storage.reference.child(key + ".png")
        val contexta = this
        var earea = getAreas()

        var isfirstsel = true

        binding.Eimage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            pickImage.launch(intent)
        }
        binding.Ememo.setText(memo)

        binding.EDateText.setText(date.toString())


        binding.EspnArea.adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, earea)
        binding.EspnArea.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                if (isfirstsel){
                    isfirstsel = false
                }
                else{
                    binding.EspnDistrict.adapter = ArrayAdapter(contexta, R.layout.support_simple_spinner_dropdown_item, getDistrictsFromArea(earea[position]))
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {

                Glide.with(this)
                    .load(task.result)
                    .into(binding.Eimage)
            }
        })
        binding.EspnArea.setSelection(earea.indexOf(area))

        binding.EspnDistrict.adapter = ArrayAdapter(contexta, R.layout.support_simple_spinner_dropdown_item, getDistrictsFromArea(binding.EspnArea.selectedItem.toString()))

        binding.EspnDistrict.setSelection(getDistrictsFromArea(area).indexOf(district))

        binding.EDateText.setOnClickListener {

            val today = GregorianCalendar()
            val year = today.get(Calendar.YEAR)
            val month = today.get(Calendar.MONTH)
            val day = today.get(Calendar.DATE)

            val dlg = DatePickerDialog(this,object : DatePickerDialog.OnDateSetListener{
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    binding.EDateText.text = "${year}년 ${month+1}월 ${dayOfMonth}일"
                }

            },year,month,day)

            dlg.show()
        }
        binding.Epost.setOnClickListener {
            binding.edLoading.visibility = View.VISIBLE;
            editBoardData(key)
            imageUpload(key)
        }

    }
    private fun editBoardData(key: String){

        var Area = binding.EspnArea.selectedItem.toString()
        var District = binding.EspnDistrict.selectedItem.toString()
        var Date = binding.EDateText.text.toString()
        var Memo = binding.Ememo.text.toString()

        val uid = FBAuth.getUid()
        FBRef.uidRef
            .child(uid).child(key)
            .setValue(
                PostModel(Area,District,Date,Memo,key
                )
        )
        Toast.makeText(this,"수정 완료", Toast.LENGTH_SHORT).show()

        finish()
    }
    private fun imageUpload (key : String){

        val storage = Firebase.storage
        val storageRef =storage.reference
        val mountainsRef = storageRef.child(key + ".png")
        val uid = FBAuth.getUid()

        val imageView = binding.Eimage
        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = mountainsRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
            mountainsRef.downloadUrl.addOnSuccessListener {
                FBRef.uidRef
                        .child(uid).child(key).child("url").setValue(it.toString()).addOnSuccessListener {
                            Toast.makeText(this, "사진이 수정되었습니다", Toast.LENGTH_SHORT).show()
                            binding.edLoading.visibility = View.GONE;
                            finish()
                        }.addOnFailureListener {
                            binding.edLoading.visibility = View.GONE;
                            Toast.makeText(this, "서버에 문제가 생겼습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                        }

            }
        }
    }
}