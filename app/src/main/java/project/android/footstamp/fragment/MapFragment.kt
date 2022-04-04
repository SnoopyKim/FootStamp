package project.android.footstamp.fragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.transition.Scene
import androidx.transition.TransitionManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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


class MapFragment : androidx.fragment.app.Fragment() {

    val myRef = FirebaseDatabase.getInstance().getReference("uid")
    lateinit var postsOrigin: List<PostModel>
    val posts: MutableLiveData<List<PostModel>> = MutableLiveData()

    private var _binding: FragmentMapBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    lateinit var sceneRoot: ViewGroup
    lateinit var sceneArea: Scene
    lateinit var sceneDistrict: Scene

    private var selectedArea: MutableLiveData<String> = MutableLiveData("전체")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        myRef.child(FirebaseAuth.getInstance().currentUser!!.uid).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("MapFragment", "list get!")
                postsOrigin = snapshot.children.map { it.getValue<PostModel>()!! }
                posts.value = postsOrigin
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("MapFragment", "Firebase onCancelled")
            }

        })
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        sceneRoot = binding.sceneRoot

//        binding.tvAll.background = resources.getDrawable(R.drawable.white_circle, resources.newTheme())
        binding.tvAll.setOnClickListener {
            if (selectedArea.value != "전체") {
                selectedArea.value = "전체"
            }
        }
        binding.tvEast.setOnClickListener {
            if (selectedArea.value != "동부") {
                selectedArea.value = "동부"
            }
        }
        binding.tvWest.setOnClickListener {
            if (selectedArea.value != "서부") {
                selectedArea.value = "서부"
            }
        }
        binding.tvSouth.setOnClickListener {
            if (selectedArea.value != "남부") {
                selectedArea.value = "남부"
            }
        }
        binding.tvNorth.setOnClickListener {
            if (selectedArea.value != "북부") {
                selectedArea.value = "북부"
            }
        }
        binding.tvCenter.setOnClickListener {
            if (selectedArea.value != "중부") {
                selectedArea.value = "중부"
            }
        }

        selectedArea.observe(viewLifecycleOwner, {
            when(it) {
                "전체" -> {
                    binding.tvEast.background = null
                    binding.tvWest.background = null
                    binding.tvSouth.background = null
                    binding.tvNorth.background = null
                    binding.tvCenter.background = null
                    binding.tvAll.background = resources.getDrawable(R.drawable.white_circle, resources.newTheme())
                    TransitionManager.go(sceneArea)
                    posts.value = if (::postsOrigin.isInitialized) postsOrigin else listOf()
                }
                "동부" -> {
                    binding.tvEast.background = resources.getDrawable(R.drawable.white_circle, resources.newTheme())
                    binding.tvWest.background = null
                    binding.tvSouth.background = null
                    binding.tvNorth.background = null
                    binding.tvCenter.background = null
                    binding.tvAll.background = null
                    TransitionManager.go(sceneDistrict)
                    posts.value = if (::postsOrigin.isInitialized) postsOrigin.filter { it.area == "동부" } else listOf()
                }
                "서부" -> {
                    binding.tvEast.background = null
                    binding.tvWest.background = resources.getDrawable(R.drawable.white_circle, resources.newTheme())
                    binding.tvSouth.background = null
                    binding.tvNorth.background = null
                    binding.tvCenter.background = null
                    binding.tvAll.background = null
                    TransitionManager.go(sceneDistrict)
                    posts.value = if (::postsOrigin.isInitialized) postsOrigin.filter { it.area == "서부" } else listOf()
                }
                "남부" -> {
                    binding.tvEast.background = null
                    binding.tvWest.background = null
                    binding.tvSouth.background = resources.getDrawable(R.drawable.white_circle, resources.newTheme())
                    binding.tvNorth.background = null
                    binding.tvCenter.background = null
                    binding.tvAll.background = null
                    TransitionManager.go(sceneDistrict)
                    posts.value = if (::postsOrigin.isInitialized) postsOrigin.filter { it.area == "남부" } else listOf()
                }
                "북부" -> {
                    binding.tvEast.background = null
                    binding.tvWest.background = null
                    binding.tvSouth.background = null
                    binding.tvNorth.background = resources.getDrawable(R.drawable.white_circle, resources.newTheme())
                    binding.tvCenter.background = null
                    binding.tvAll.background = null
                    TransitionManager.go(sceneDistrict)
                    posts.value = if (::postsOrigin.isInitialized) postsOrigin.filter { it.area == "북부" } else listOf()
                }
                "중부" -> {
                    binding.tvEast.background = null
                    binding.tvWest.background = null
                    binding.tvSouth.background = null
                    binding.tvNorth.background = null
                    binding.tvCenter.background = resources.getDrawable(R.drawable.white_circle, resources.newTheme())
                    binding.tvAll.background = null
                    TransitionManager.go(sceneDistrict)
                    posts.value = if (::postsOrigin.isInitialized) postsOrigin.filter { it.area == "중부" } else listOf()
                }
            }
            binding.rlLoading.visibility = VISIBLE
        })

        posts.observe(viewLifecycleOwner, { list ->
            val mapView : MapView = sceneRoot.findViewById(R.id.map_view)
            val isAll: Boolean = selectedArea.value == "전체"
            CoroutineScope(Dispatchers.IO).launch {
                val limit = arrayListOf<Pair<String, Int>>()
                val postsWithBitmap = list.filter {
                    val name = if (isAll) it.area else it.district
                    val item = limit.find { pair -> pair.first == name }
                    if (item == null) {
                        limit.add(Pair(name, 1));
                        true
                    } else {
                        val index = limit.indexOf(item)
                        val count = limit[index].second
                        if (count == 3) {
                            false
                        } else {
                            limit[index] = Pair(name, count + 1)
                            true
                        }
                    }
                }.map {
                    val randomSize = Random.nextInt(120, 160)
                    it.bitmap = Glide.with(requireContext())
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
                    if(::postsOrigin.isInitialized) {
                        binding.rlLoading.visibility = GONE
                    }
                }
            }
        })
        // 구역 선택 장면(Scene) 설정
        sceneArea = Scene.getSceneForLayout(sceneRoot, R.layout.scene_map_area, requireContext())
        sceneArea.setEnterAction {
            sceneArea.sceneRoot.apply {
                val mapView: MapView = findViewById(R.id.map_view)

//                posts.observe(viewLifecycleOwner, { list ->
//                    CoroutineScope(Dispatchers.IO).launch {
//                        var limit = arrayListOf<Pair<String, Int>>()
//                        val postsWithBitmap = list.filter {
//                            val item = limit.find { pair -> pair.first == it.area }
//                            if (item == null) {
//                                limit.add(Pair(it.area, 1));
//                                true
//                            } else {
//                                val index = limit.indexOf(item)
//                                val count = limit[index].second
//                                if (count == 3) {
//                                    false
//                                } else {
//                                    limit[index] = Pair(it.area, count + 1)
//                                    true
//                                }
//                            }
//                        }.map {
//                            val randomSize = Random.nextInt(120, 160)
//                            it.bitmap = Glide.with(context)
//                                .asBitmap()
//                                .load(it.url)
//                                .override(randomSize)
//                                    .apply(RequestOptions.circleCropTransform())
//                                .submit().get()
//                            it
//                        }
//                        Log.d("observe", "limit: $limit")
//                        withContext(Dispatchers.Main) {
//                            mapView.setPostList(postsWithBitmap)
//                        }
//                    }
//                })
                // 전체 뷰 안에 있는 뷰들에 클릭 리스너 설정
                mapView.setOnTouchListener { v, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        if(event.x > v.width/3*2 && v.height/3 < event.y && event.y < v.height/3*2) {
                            selectedArea.value = "동부";
                        } else if (event.x < v.width/3 && v.height/3 < event.y && event.y < v.height/3*2) {
                            selectedArea.value = "서부";
                        } else if (v.width/3 < event.x  && event.x < v.width/3*2 && event.y > v.height/3*2) {
                            selectedArea.value = "남부";
                        } else if (v.width/3 < event.x  && event.x < v.width/3*2 && event.y < v.height/3) {
                            selectedArea.value = "북부";
                        } else if (v.width/3 < event.x  && event.x < v.width/3*2 && v.height/3 < event.y && event.y < v.height/3*2) {
                            selectedArea.value = "중부";
                        }
                    }
                    true
                }
            }
        }

        // 자치구 선택 장면(Scene) 설정
        sceneDistrict = Scene.getSceneForLayout(sceneRoot, R.layout.scene_map_district, requireContext())
        sceneDistrict.setEnterAction {
            Log.d(javaClass.name,  "selectedArea: ${selectedArea.value}")

            sceneDistrict.sceneRoot.apply {
                findViewById<MapView>(R.id.map_view).setArea(selectedArea.value!!)
//                posts.observe(viewLifecycleOwner, {
//                    CoroutineScope(Dispatchers.IO).launch {
//                        val postsWithBitmap = it.filter { it.area == selectedArea }.map {
//                            val randomSize = Random.nextInt(120, 160)
//                            it.bitmap = Glide.with(context)
//                                .asBitmap()
//                                .load(it.url)
//                                .override(randomSize)
//                                .apply(RequestOptions.circleCropTransform())
//                                .submit().get()
//                            it
//                        }
//                        withContext(Dispatchers.Main) {
//
//                            findViewById<MapView>(R.id.map_view).setPostList(postsWithBitmap)
//                        }
//                    }
//                })
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