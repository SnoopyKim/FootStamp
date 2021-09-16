package project.android.footstamp.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import project.android.footstamp.R
import project.android.footstamp.databinding.FragmentPostBinding
import project.android.footstamp.model.Stamp
import project.android.footstamp.viewmodel.StampViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class PostFragment : Fragment() {
    private var _binding: FragmentPostBinding? = null
    private val binding get() = _binding!!

    private val pickImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        val uri = result.data?.data
        if (uri != null) {
            imageBuffer = context?.contentResolver?.openInputStream(uri)?.buffered()?.use { it.readBytes() }!!
            binding.ivImageSearch.setImageURI(uri)
        }
    }

    private lateinit var stampViewModel: StampViewModel
    private var postCount: Int = 0
    private var imageBuffer: ByteArray = ByteArray(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        stampViewModel = StampViewModel(requireActivity().application)
        CoroutineScope(Dispatchers.Main).launch {
            stampViewModel.getAll().observe(viewLifecycleOwner, Observer { stamps -> postCount = stamps.size })
        }
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
        with(binding) {
            ivImageSearch.setOnClickListener{
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                pickImage.launch(intent)
            }
            btnPost.setOnClickListener {
                val area = etArea.text.toString(); val memo = etMemo.text.toString()
                val date = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) LocalDate.now().toString() else SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(
                    Date())

                val data = Stamp(
                    id = "$area-$date-$postCount",
                    area,
                    date,
                    imageBuffer,
                    memo
                )
                CoroutineScope(Dispatchers.IO).launch {
                    stampViewModel.insert(data)
                }
                ivImageSearch.setImageResource(R.drawable.ic_image_search)
                etArea.text.clear()
                etMemo.text.clear()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
//        if (requestCode == Companion.REQUEST_IMAGE_OPEN && resultCode == Activity.RESULT_OK) {
//            val fullPhotoUri: Uri = data.data
//            // Do work with full size photo saved at fullPhotoUri
//            ...
//        }
//    }

    companion object {
//        const val REQUEST_IMAGE_OPEN = 1

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PostFragment().apply {

            }


    }
}