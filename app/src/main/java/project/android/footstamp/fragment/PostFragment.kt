package project.android.footstamp.fragment

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import project.android.footstamp.R
import project.android.footstamp.StampApplication
import project.android.footstamp.databinding.FragmentPostBinding
import project.android.footstamp.model.Stamp
import project.android.footstamp.utils.*
import project.android.footstamp.viewmodel.StampViewModel
import project.android.footstamp.viewmodel.StampViewModelFactory
import java.io.ByteArrayOutputStream
import java.sql.Time
import java.util.*

class PostFragment : Fragment() {
    var dateSelect = false
    var imagePick = false
    val database = Firebase.database
    val FBPost = database.getReference("")
    private val stampViewModel: StampViewModel by activityViewModels {
        StampViewModelFactory(
            (activity?.application as StampApplication).repository
        )
    }

    private var _binding: FragmentPostBinding? = null
    private val binding get() = _binding!!

    private val pickImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        val uri = result.data?.data
        if (uri != null) {
            imageBuffer = context?.contentResolver?.openInputStream(uri)?.buffered()?.use { it.readBytes() }!!
            binding.ivImageSearch.setImageURI(uri)
            imagePick = true
        }
    }
    private lateinit var imageBuffer: ByteArray

    private var area = getAreas()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        resetUI()
        binding.apply {

            binding.DateBtn.setOnClickListener {

                val today = GregorianCalendar()
                val year = today.get(Calendar.YEAR)
                val month = today.get(Calendar.MONTH)
                val day = today.get(Calendar.DATE)

                val dlg = DatePickerDialog(requireContext(),object : DatePickerDialog.OnDateSetListener{
                    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                        binding.DateBtn.text = "${year}년 ${month+1}월 ${dayOfMonth}일"
                        dateSelect = true
                    }

                },year,month,day)

                dlg.show()
            }

            ivImageSearch.setOnClickListener{
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                pickImage.launch(intent)
            }

            spnArea.adapter = ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, area)
            spnArea.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    spnDistrict.adapter = ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, getDistrictsFromArea(area[position]))

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }
            spnDistrict.adapter = ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, getDistrictsFromArea(spnArea.selectedItem.toString()))

            //파이어베이스 업로드 먼저 구현 /db구현임
            btnPost.setOnClickListener {
                if (imagePick == true) {
                    if (dateSelect == true) {
                        val Area = binding.spnArea.selectedItem.toString()
                        val District = binding.spnDistrict.selectedItem.toString()
                        val uid = FBAuth.getUid()
                        val time = binding.DateBtn.text.toString()
                        val memo = binding.etMemo.text.toString()
                        val key = FBRef.uidRef.push().key.toString()

                        FBRef.uidRef
                            .child(uid).child(key)
                            .setValue(PostModel(Area, District, time, memo, key))

                        imageUpload(key)

                        stampViewModel.insertItem(
                            Area,
                            District,
                            binding.DateBtn.text.toString(),
                            imageBuffer,
                            memo
                        )
                        binding.DateBtn.text = "날짜 선택하기"
                        resetUI()
                        Toast.makeText(context,"사진이 게시되었습니다",Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "날짜를 선택해주세요", Toast.LENGTH_SHORT).show()
                    }
                } else  {
                    Toast.makeText(context,"사진을 선택해주세요",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun resetUI() {
        imageBuffer = ByteArray(0)
        binding.apply {
            ivImageSearch.setImageResource(R.drawable.ic_image_search)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {


    }
}