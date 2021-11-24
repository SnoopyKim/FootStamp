package project.android.footstamp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import project.android.footstamp.R
import project.android.footstamp.databinding.ActivityLoginBinding
import project.android.footstamp.databinding.FragmentGalleryBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var googleSignInClient: GoogleSignInClient? = null
    private lateinit var auth: FirebaseAuth
    private var GOOGLE_LOGIN_CODE = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        auth = Firebase.auth
        setContentView(binding.root)

        binding.passBtn.setOnClickListener{
            val intent= Intent(baseContext, MainActivity::class.java)
            startActivity(intent)
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("591458789988-8q5k10cud95t39tpeml69vgkpamm41ho.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.googleLogin.setOnClickListener {
            googleLogin()
        }


        //회원가입 버튼
        binding.JoinBtn.setOnClickListener{

            var GoJoin = true

            val email = binding.EmailArea.text.toString()
            val password = binding.PasswordArea.text.toString()

            //값 확인
            if (email.isEmpty()){
                Toast.makeText(this,"이메일을 입력해주세요",Toast.LENGTH_SHORT).show()
                GoJoin = false
            }
            if (password.isEmpty()){
                Toast.makeText(this,"비밀번호를 입력해주세요",Toast.LENGTH_SHORT).show()
                GoJoin = false
            }
            if (!email.contains("@")){
                Toast.makeText(this,"이메일 양식을 확인하세요",Toast.LENGTH_SHORT).show()
                GoJoin = false
            }
            if (password.length < 6){
                Toast.makeText(this,"비밀번호를 6자리 이상으로 설정해주세요",Toast.LENGTH_SHORT).show()
                GoJoin = false
            }

            //회원가입
            if (GoJoin){
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // 로그인 성공시
                        Toast.makeText(baseContext, "회원가입 성공",Toast.LENGTH_SHORT).show()

                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    } else {
                        // 로그인 실패시
                        Toast.makeText(baseContext, "회원가입 실패", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        //로그인
        binding.LoginBtn.setOnClickListener {

            val email = binding.EmailArea.text.toString()
            val password = binding.PasswordArea.text.toString()
            //로그인 구현
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // 로그인 성공
                        Toast.makeText(this,"로그인 성공",Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    } else {
                        // 로그인 실패
                        Toast.makeText(this,"로그인 실패",Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }
    fun googleLogin(){
        var signInIntent = googleSignInClient?.signInIntent
        startActivityForResult(signInIntent,GOOGLE_LOGIN_CODE)
    }
    fun firebaseAuthWithGoogle(account : GoogleSignInAccount?){
        var credential = GoogleAuthProvider.getCredential(account?.idToken,null)
        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener{
                    task ->
                if(task.isSuccessful){
                    // 아이디, 비밀번호 맞을 때
                    moveMainPage(task.result?.user)
                }else{
                    // 틀렸을 때
                    Toast.makeText(this,task.exception?.message,Toast.LENGTH_SHORT).show()
                }
            }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == GOOGLE_LOGIN_CODE){
            var result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)!!
            // 구글API가 넘겨주는 값 받아옴

            if(result.isSuccess) {
                var accout = result.signInAccount
                firebaseAuthWithGoogle(accout)
                Toast.makeText(this,"성공",Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this,"실패",Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun moveMainPage(user: FirebaseUser?){
        if( user!= null){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }
}