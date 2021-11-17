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
import androidx.fragment.app.activityViewModels
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import project.android.footstamp.R
import androidx.fragment.app.activityViewModels
import com.google.firebase.storage.ktx.storage
import project.android.footstamp.StampApplication
import project.android.footstamp.databinding.ActivityPostBinding
import project.android.footstamp.databinding.FragmentPostBinding
import project.android.footstamp.utils.*
import project.android.footstamp.viewmodel.StampViewModel
import project.android.footstamp.viewmodel.StampViewModelFactory
import java.io.ByteArrayOutputStream
import java.util.*

class PostActivity : AppCompatActivity() {
    lateinit var binding: ActivityPostBinding
    var dateSelect = false
    var imagePick = false
    val database = Firebase.database
    val context = this
    val FBPost = database.getReference("")
    val storage = Firebase.storage
    val gsReference = storage.getReferenceFromUrl("gs://bucket/images/stars.jpg")

    private val pickImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        val uri = result.data?.data
        if (uri != null) {
            imageBuffer = this.contentResolver?.openInputStream(uri)?.buffered()?.use { it.readBytes() }!!
            binding.ivImageSearch.setImageURI(uri)
            imagePick = true
        }
    }
    private lateinit var imageBuffer: ByteArray

    private var area = getAreas()


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityPostBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)

        binding.DateText.setOnClickListener {

            val today = GregorianCalendar()
            val year = today.get(Calendar.YEAR)
            val month = today.get(Calendar.MONTH)
            val day = today.get(Calendar.DATE)

            val dlg = DatePickerDialog(this,object : DatePickerDialog.OnDateSetListener{
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    binding.DateText.text = "${year}년 ${month+1}월 ${dayOfMonth}일"
                    dateSelect = true
                }

            },year,month,day)

            dlg.show()
        }

        binding.ivImageSearch.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            pickImage.launch(intent)
        }

            binding.spnArea.adapter = ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, area)
            binding.spnArea.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                binding.spnDistrict.adapter = ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, getDistrictsFromArea(area[position]))

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
                binding.spnDistrict.adapter = ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, getDistrictsFromArea(binding.spnArea.selectedItem.toString()))

                    binding.btnPost.setOnClickListener {
            if (imagePick == true) {
                if (dateSelect == true) {
                    val Area = binding.spnArea.selectedItem.toString()
                    val District = binding.spnDistrict.selectedItem.toString()
                    val uid = FBAuth.getUid()
                    val time = binding.DateText.text.toString()
                    val memo = binding.etMemo.text.toString()
                    val key = FBRef.uidRef.push().key.toString()
                    val url = 

                    FBRef.uidRef
                        .child(uid).child(key)
                        .setValue(PostModel(Area, District, time, memo, key))

                    imageUpload(key)
                    //stampmodel 삭제
                    binding.DateText.text = "날짜 선택하기"
                    resetUI()
                    Toast.makeText(context,"사진이 게시되었습니다", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(context, "날짜를 선택해주세요", Toast.LENGTH_SHORT).show()
                }
            } else  {
                Toast.makeText(context,"사진을 선택해주세요", Toast.LENGTH_SHORT).show()
            }
        }

        setContentView(binding.root)
    }
    private fun resetUI() {
        imageBuffer = ByteArray(0)
        binding.apply {
            ivImageSearch.setImageResource(R.drawable.foot_logo)
            spnArea.setSelection(0)
            spnDistrict.setSelection(0)
            etMemo.text.clear()
        }
    }

    private fun imageUpload (key : String){

        val storage = Firebase.storage
        val storageRef =storage.reference
        val mountainsRef = storageRef.child(key + ".png")

        val imageView = binding.ivImageSearch
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
        }
    }

}