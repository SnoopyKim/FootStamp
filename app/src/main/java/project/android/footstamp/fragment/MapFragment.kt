package project.android.footstamp.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.transition.ChangeBounds
import androidx.transition.Scene
import androidx.transition.TransitionManager
import com.google.android.material.button.MaterialButton
import project.android.footstamp.R
import project.android.footstamp.StampApplication
import project.android.footstamp.databinding.FragmentMapBinding
import project.android.footstamp.utils.getDistrictsFromArea
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

    lateinit var sceneRoot: ViewGroup
    lateinit var sceneArea: Scene
    lateinit var sceneDistrict: Scene

    private lateinit var selectedArea: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        sceneRoot = binding.sceneRoot
        sceneArea = Scene.getSceneForLayout(sceneRoot, R.layout.scene_map_area, requireContext())
        sceneArea.setEnterAction {
            sceneArea.sceneRoot.apply {
                findViewById<LinearLayout>(R.id.btn_east).setOnClickListener {
                    selectedArea = ((it as LinearLayout).getChildAt(0) as TextView).text.toString()
                    TransitionManager.go(sceneDistrict, ChangeBounds())
                }
                findViewById<Button>(R.id.btn_west).setOnClickListener {
                    selectedArea = (it as Button).text.toString()
                    TransitionManager.go(sceneDistrict) }
            }
        }
        sceneDistrict = Scene.getSceneForLayout(sceneRoot, R.layout.scene_map_district_east, requireContext())
        sceneDistrict.setEnterAction {
            Log.d(javaClass.name, "selectedArea: $selectedArea")
            val districts = getDistrictsFromArea(selectedArea)
            districts.forEach {
                val btn = MaterialButton(requireContext()).apply { text = it }
//                sceneDistrict.sceneRoot.findViewById<LinearLayout>(R.id.container_district).addView(btn)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sceneArea.enter()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}