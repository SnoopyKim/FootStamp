package project.android.footstamp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.transition.Fade
import androidx.transition.TransitionManager
import project.android.footstamp.R
import project.android.footstamp.StampApplication
import project.android.footstamp.databinding.FragmentMapBinding
import project.android.footstamp.utils.getAreas
import project.android.footstamp.viewmodel.StampViewModel
import project.android.footstamp.viewmodel.StampViewModelFactory


class MapFragment : Fragment() {

    private val stampViewModel: StampViewModel by activityViewModels {
        StampViewModelFactory(
            (activity?.application as StampApplication).repository
        )
    }

    private var _binding: FragmentMapBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private val areaList = getAreas()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val labelText = TextView(context).apply {
            text = "Label"
            id = R.id.text
        }
        binding.animLayout.addView(labelText)
        binding.animLayout.setOnClickListener {
            if(labelText.visibility == View.VISIBLE){
                TransitionManager.beginDelayedTransition(binding.animLayout, Fade(Fade.OUT))
                labelText.visibility = View.GONE;
            } else {
                TransitionManager.beginDelayedTransition(binding.animLayout, Fade(Fade.IN))
                labelText.visibility = View.VISIBLE;
            }
        }

        binding.areas.text = areaList.toString()

        stampViewModel.allStamps.observe(viewLifecycleOwner, Observer { stamps ->
            binding.textview.text = "Data size: ${stamps.size}"
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MapFragment().apply {
                // TODO: 일단 냅둬

            }
    }
}