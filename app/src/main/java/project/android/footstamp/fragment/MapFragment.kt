package project.android.footstamp.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.transition.Scene
import androidx.transition.TransitionManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import project.android.footstamp.R
import project.android.footstamp.databinding.FragmentMapBinding
import project.android.footstamp.utils.PostModel
import project.android.footstamp.view.MapView
import kotlin.random.Random


class MapFragment : Fragment() {

    val myRef = FirebaseDatabase.getInstance().getReference("uid")
    val posts: MutableLiveData<List<PostModel>> = MutableLiveData()

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

        myRef.child(FirebaseAuth.getInstance().currentUser!!.uid).get().addOnSuccessListener { list ->
            Log.d("MapFragment", "list get!")
            posts.value = list.children.map { it.getValue<PostModel>()!! }
        }
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
                val mapView: MapView = findViewById(R.id.map_view)

                posts.observe(viewLifecycleOwner, {
                    CoroutineScope(Dispatchers.IO).launch {
                        var limit = mutableMapOf<String, Int>(
                            "동부" to 0,
                            "서부" to 0,
                            "남부" to 0,
                            "북부" to 0,
                            "중부" to 0, )
                        val postsWithBitmap = it.filter {
                            val count = limit[it.area]!!
                            if (count == 3) {
                                false
                            } else {
                                limit[it.area] = count + 1
                                true
                            }
                        }.map {
                            val randomSize = Random.nextInt(100, 150)
                            it.bitmap = Glide.with(context)
                                .asBitmap()
                                .load(it.url)
                                .override(randomSize)
                                    .apply(RequestOptions.circleCropTransform())
                                .submit().get()
                            it
                        }
                        Log.d("observe", "limit: $limit")
                        withContext(Dispatchers.Main) {
                            mapView.setPostList(postsWithBitmap)
                        }
                    }
                })
                // 전체 뷰 안에 있는 뷰들에 클릭 리스너 설정
                mapView.setOnTouchListener { v, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        if(event.x > v.width/3*2 && v.height/3 < event.y && event.y < v.height/3*2) {
                            selectedArea = "동부";
                        } else if(event.x < v.width/3 && v.height/3 < event.y && event.y < v.height/3*2) {
                            selectedArea = "서부";
                        } else if(v.width/3 < event.x  && event.x < v.width/3*2 && event.y > v.height/3*2) {
                            selectedArea = "남부";
                        } else if(v.width/3 < event.x  && event.x < v.width/3*2 && event.y < v.height/3) {
                            selectedArea = "북부";
                        } else {
                            selectedArea = "중부";
                        }
                        TransitionManager.go(sceneDistrict)
                    }
                    true
                }
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
                findViewById<MapView>(R.id.map_view).setArea(selectedArea)
                posts.observe(viewLifecycleOwner, {
                    CoroutineScope(Dispatchers.IO).launch {
                        val postsWithBitmap = it.filter { it.area == selectedArea }.map {
                            val randomSize = Random.nextInt(100, 200)
                            it.bitmap = Glide.with(context)
                                .asBitmap()
                                .load(it.url)
                                .override(randomSize)
                                .submit().get()
                            it
                        }
                        withContext(Dispatchers.Main) {

                            findViewById<MapView>(R.id.map_view).setPostList(postsWithBitmap)
                        }
                    }
                })
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