package project.android.footstamp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import project.android.footstamp.R

class SplashActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
        setContentView(R.layout.activity_splash)


        if (auth.currentUser?.uid == null){
            Handler().postDelayed({
                startActivity(Intent(this,LoginActivity::class.java))
                finish()
            },2500)
        } else{
            Handler().postDelayed({
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            },2500)
        }

    }
}