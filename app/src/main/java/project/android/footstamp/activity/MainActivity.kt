package project.android.footstamp.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import project.android.footstamp.R
import project.android.footstamp.databinding.ActivityMainBinding
import project.android.footstamp.fragment.GalleryFragment
import project.android.footstamp.fragment.MapFragment
import project.android.footstamp.fragment.PostFragment
import project.android.footstamp.fragment.SettingFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var mapFragment: Fragment
    private lateinit var galleryFragment: Fragment
    private lateinit var postFragment: Fragment
    private lateinit var settingFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mapFragment = MapFragment();
        galleryFragment = GalleryFragment();
        postFragment = PostFragment();
        settingFragment = SettingFragment();
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fragment_container, mapFragment)
        transaction.commit()

        with(binding) {
            bottomNavigationView.setOnItemSelectedListener {
                switchFragment(it.itemId)
                Toast.makeText(applicationContext, it.title, Toast.LENGTH_SHORT).show()
                true
            }
        }
    }

    private fun switchFragment(id: Int) {
        val fragment = when(id) {
            R.id.map -> mapFragment
            R.id.gallery -> galleryFragment
            R.id.post -> postFragment
            R.id.setting -> settingFragment
            else -> mapFragment
        }
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}