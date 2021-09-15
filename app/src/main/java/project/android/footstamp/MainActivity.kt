package project.android.footstamp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import project.android.footstamp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mapFragment: Fragment
    private lateinit var galleryFragment: Fragment
    private lateinit var settingFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mapFragment = MapFragment();
        galleryFragment = GalleryFragment();
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
            R.id.setting -> settingFragment
            else -> mapFragment
        }
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}