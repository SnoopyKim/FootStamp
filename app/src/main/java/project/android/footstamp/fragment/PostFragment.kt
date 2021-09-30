package project.android.footstamp.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import project.android.footstamp.R
import project.android.footstamp.StampApplication
import project.android.footstamp.databinding.FragmentPostBinding
import project.android.footstamp.viewmodel.StampViewModel
import project.android.footstamp.viewmodel.StampViewModelFactory
import java.util.*

class PostFragment : Fragment() {

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
        }
    }
    private lateinit var imageBuffer: ByteArray

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
            ivImageSearch.setOnClickListener{
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                pickImage.launch(intent)
            }
            btnPost.setOnClickListener {
                stampViewModel.insertItem(
                    etArea.text.toString(),
                    Date(),
                    imageBuffer,
                    etMemo.text.toString()
                )
                resetUI()
            }
        }
    }

    private fun resetUI() {
        imageBuffer = ByteArray(0)
        binding.apply {
            ivImageSearch.setImageResource(R.drawable.ic_image_search)
            etArea.text.clear()
            etMemo.text.clear()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {


    }
}