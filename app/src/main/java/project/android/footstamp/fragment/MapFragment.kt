package project.android.footstamp.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.transition.Scene
import androidx.transition.TransitionManager
import com.google.android.material.button.MaterialButton
import project.android.footstamp.R
import project.android.footstamp.StampApplication
import project.android.footstamp.databinding.FragmentMapBinding
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
    private var selectedId: Int = 0

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

        // 구역 선택 장면(Scene) 설정
        sceneArea = Scene.getSceneForLayout(sceneRoot, R.layout.scene_map_area, requireContext())
        sceneArea.setEnterAction {
            sceneArea.sceneRoot.apply {
                // 전체 뷰 안에 있는 뷰들에 클릭 리스너 설정
                findViewById<ConstraintLayout>(R.id.west).setOnClickListener {
                    selectedArea = "서부";
                    TransitionManager.go(sceneDistrict)
                }
//                findViewById<ConstraintLayout>(R.id.container_area).children.forEach { button ->
//                    button.setOnClickListener {
//                        selectedId = it.id
//                        selectedArea = ((it as ConstraintLayout).getChildAt(0) as TextView).text.toString()
//                        TransitionManager.go(sceneDistrict)
//                    }
//                }
            }
        }

        // 자치구 선택 장면(Scene) 설정
        sceneDistrict = Scene.getSceneForLayout(sceneRoot, R.layout.scene_map_district, requireContext())
        sceneDistrict.setEnterAction {
            Log.d(javaClass.name, "selectedId: $selectedId | selectedArea: $selectedArea")

            sceneDistrict.sceneRoot.apply {
                // 뒤로가기 버튼 설정
                findViewById<ImageButton>(R.id.btn_back).setOnClickListener {
                    TransitionManager.go(sceneArea)
                }
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

    fun addButtonOnLinearLayout(layout: LinearLayout, title: String, params: LinearLayout.LayoutParams) {
        val btn = MaterialButton(requireContext()).apply {
            id = View.generateViewId()
            text = title
            layoutParams = params
        }
        layout.addView(btn)
    }
}