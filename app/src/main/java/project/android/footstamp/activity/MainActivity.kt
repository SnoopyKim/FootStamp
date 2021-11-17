package project.android.footstamp.activity

import android.os.Bundle
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import project.android.footstamp.R
import project.android.footstamp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val QUIT_INTERVAL: Long = 2000

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController

    private var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - backPressedTime < QUIT_INTERVAL) finish()
        else {
            backPressedTime = currentTime
            Toast.makeText(this,  "2초 내 한번 더 누르시면 앱이 종료됩니다", Toast.LENGTH_SHORT).show()
        }
    }
}