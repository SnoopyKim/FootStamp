package project.android.footstamp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import project.android.footstamp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    final val RC_SIGN_IN = 1
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        auth = Firebase.auth
        setContentView(binding.root)
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        binding.passBtn.setOnClickListener{
            val intent= Intent(baseContext, MainActivity::class.java)
            startActivity(intent)
        }

            auth.addAuthStateListener {
                if (it.currentUser != null) {
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                }
            }


        binding.googleLogin.setOnClickListener {
            signIn()
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
    private fun signIn() {
        var signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val email = account?.email.toString()
            val familyName = account?.familyName.toString()
            val givenName = account?.givenName.toString()
            val displayName = account?.displayName.toString()

            Log.d("account", email)
            Log.d("account", familyName)
            Log.d("account", givenName)
            Log.d("account", displayName)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("failed", "signInResult:failed code=" + e.statusCode)
        }
    }
}