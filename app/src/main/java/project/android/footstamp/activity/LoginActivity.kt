package project.android.footstamp.activity

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import project.android.footstamp.R
import project.android.footstamp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    val RC_SIGN_IN = 1
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    lateinit var mGoogleSignInClient: GoogleSignInClient
//    var GoJoin = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("591458789988-8q5k10cud95t39tpeml69vgkpamm41ho.apps.googleusercontent.com")
            .requestEmail()
            .build()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        auth = Firebase.auth
        setContentView(binding.root)
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
//        binding.passBtn.setOnClickListener{
//            val intent= Intent(baseContext, MainActivity::class.java)
//            startActivity(intent)
//        }

        auth.addAuthStateListener {
            if (it.currentUser !=null) {
                startActivity(Intent(applicationContext, MainActivity::class.java))
            }
        }

        binding.googleLogin.setOnClickListener {
//            var GoJoin = true
            signIn()
        }

        binding.btnTerms.setOnClickListener {
            val alert = AlertDialog.Builder(this).setTitle("???????????? ????????????")
            val scrollbar = ScrollView(this)
            val textView = TextView(this)
            textView.setText(resources.getString(R.string.privacy_terms_value))
            textView.setPadding(30, 30, 30, 30)
            val view = LinearLayout(this)
            view.orientation = LinearLayout.VERTICAL
            view.addView(textView)
            scrollbar.addView(view)
            alert.setView(scrollbar).setPositiveButton("??????", DialogInterface.OnClickListener { dialog, id ->

            }).create().show()
        }

//        ???????????? ??????
//        binding.JoinBtn.setOnClickListener{
//
//
//
//            val email = binding.EmailArea.text.toString()
//            val password = binding.PasswordArea.text.toString()
//
//            //??? ??????
//            if (email.isEmpty()){
//                Toast.makeText(this,"???????????? ??????????????????",Toast.LENGTH_SHORT).show()
//                GoJoin = false
//            }
//            if (password.isEmpty()){
//                Toast.makeText(this,"??????????????? ??????????????????",Toast.LENGTH_SHORT).show()
//                GoJoin = false
//            }
//            if (!email.contains("@")){
//                Toast.makeText(this,"????????? ????????? ???????????????",Toast.LENGTH_SHORT).show()
//                GoJoin = false
//            }
//            if (password.length < 6){
//                Toast.makeText(this,"??????????????? 6?????? ???????????? ??????????????????",Toast.LENGTH_SHORT).show()
//                GoJoin = false
//            }

//            ????????????

//        val email = binding.EmailArea.text.toString()
//        val password = binding.PasswordArea.text.toString()

//            if (GoJoin){
//            auth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this) { task ->
//                    if (task.isSuccessful) {
//                        // ????????? ?????????
//                        Toast.makeText(baseContext, "???????????? ??????",Toast.LENGTH_SHORT).show()
//
//                        val intent = Intent(this, MainActivity::class.java)
//                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//                        startActivity(intent)
//                    } else {
//                        // ????????? ?????????
//                        Toast.makeText(baseContext, "???????????? ??????", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }


////        ?????????
//        binding.LoginBtn.setOnClickListener {
//
//            val email = binding.EmailArea.text.toString()
//            val password = binding.PasswordArea.text.toString()
//            //????????? ??????
//            auth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this) { task ->
//                    if (task.isSuccessful) {
//                        // ????????? ??????
//                        Toast.makeText(this,"????????? ??????",Toast.LENGTH_SHORT).show()
//                        val intent = Intent(this, MainActivity::class.java)
//                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//                        startActivity(intent)
//                    } else {
//                        // ????????? ??????
//                        Toast.makeText(this,"????????? ??????",Toast.LENGTH_SHORT).show()
//                    }
//                }
//        }

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

//            var account: GoogleSignInAccount? = null
//            try {
//                account = task.getResult(ApiException::class.java)
//                firebaseAuthWithGoogle(account!!.idToken)
//            } catch (e: ApiException) {
//                Toast.makeText(this, "Failed Google Login", Toast.LENGTH_SHORT).show()
//            }
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

            firebaseAuthWithGoogle(account.idToken!!)

//            val intent = Intent(this,MainActivity::class.java)
//            startActivity(intent)
//            finish()
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("failed", "signInResult:failed code=" + e.statusCode)
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this,
                        OnCompleteListener<AuthResult?> { task ->
                        })
    }
}