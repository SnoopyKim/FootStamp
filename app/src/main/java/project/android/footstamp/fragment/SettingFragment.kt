package project.android.footstamp.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import project.android.footstamp.R
import project.android.footstamp.activity.LoginActivity
import project.android.footstamp.activity.MainActivity
import project.android.footstamp.databinding.FragmentSettingBinding

private lateinit var binding: FragmentSettingBinding
private lateinit var auth: FirebaseAuth
class SettingFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = FragmentSettingBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_setting, container, false)

        //로그아웃 구현
        val logoutBtn = view.findViewById<Button>(R.id.logOutBtn)
        logoutBtn.setOnClickListener{
            auth.signOut()
            val intent = Intent(context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
        return view
    }


        fun newInstance(param1: String, param2: String) =
            SettingFragment().apply {
                // TODO: 일단 냅둬

            }
    }