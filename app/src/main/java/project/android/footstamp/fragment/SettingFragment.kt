package project.android.footstamp.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import android.widget.EditText
import com.google.android.gms.auth.api.signin.GoogleSignIn
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import project.android.footstamp.R
import project.android.footstamp.activity.LoginActivity
import project.android.footstamp.activity.MainActivity
import project.android.footstamp.databinding.FragmentSettingBinding
import project.android.footstamp.utils.BoardModel
import project.android.footstamp.utils.FBAuth
import project.android.footstamp.utils.FBRef
import project.android.footstamp.utils.NicknameModel

private lateinit var binding: FragmentSettingBinding
private lateinit var auth: FirebaseAuth
private lateinit var database: DatabaseReference
class SettingFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = FragmentSettingBinding.inflate(layoutInflater)
        val database = Firebase.database
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding.editNicknameBtn.setOnClickListener{
            showDialog()
        }
        val uid = FBAuth.getUid()

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val nameModel = dataSnapshot.getValue(NicknameModel::class.java)
                if (nameModel?.nickname!=null) {
                    binding.nicknameText.setText(nameModel?.nickname.toString())
                } else{
                    binding.nicknameText.setText("닉네임을 입력해 주세요")
                }
                if (nameModel?.memo!=null) {
                    if (nameModel?.memo.toString() == "") {
                        binding.setMemo.setText("한 줄 소개를 해주세요")
                    } else {
                        binding.setMemo.setText(nameModel?.memo.toString())
                    }
                } else{
                    binding.setMemo.setText("한 줄 소개를 해주세요")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message

            }
        }
       FBRef.nicknameRef.child(uid).addValueEventListener(postListener)


        //로그아웃 구현

        binding.logOutBtn.setOnClickListener{
            Logout()
            val intent = Intent(context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
        //회원탈퇴
        binding.logDelBtn.setOnClickListener {
            showDialog2()
        }
        return binding.root
    }


        fun newInstance(param1: String, param2: String) =
            SettingFragment().apply {
                // TODO: 일단 냅둬

            }
    private fun showDialog() {
        val mDialogView = LayoutInflater.from(context).inflate(R.layout.custom_dialog3, null)
        val mBuilder = AlertDialog.Builder(requireContext())
            .setView(mDialogView)

        val alertDialog = mBuilder.show()
        alertDialog.findViewById<EditText>(R.id.dNickEdit)?.setText(binding.nicknameText.text)
        alertDialog.findViewById<EditText>(R.id.dMemoEdit)?.setText(binding.setMemo.text)

        alertDialog.findViewById<Button>(R.id.dok)?.setOnClickListener {

            val uid = FBAuth.getUid()
            val name = alertDialog.findViewById<EditText>(R.id.dNickEdit)?.text.toString()
            val memo = alertDialog.findViewById<EditText>(R.id.dMemoEdit)?.text.toString()
            var namerule = true


            if (name.isEmpty()) {
                Toast.makeText(requireContext(), "닉네임을 입력해주세요", Toast.LENGTH_SHORT).show()
                namerule = false
            }

            if (name.length > 12) {
                Toast.makeText(requireContext(), "닉네임은 12자이하로 설정해야 해요", Toast.LENGTH_SHORT).show()
                namerule = false
            }

            if (name.length < 2) {
                Toast.makeText(requireContext(), "닉네임은 2자이상으로 설정해야 해요", Toast.LENGTH_SHORT).show()
                namerule = false
            }
            if (memo.length > 20) {
                Toast.makeText(requireContext(), "소개는 20자이하로 작성해야 해요", Toast.LENGTH_SHORT).show()
                namerule = false
            }

            if (namerule) {
                FBRef.nicknameRef
                        .child(uid)
                        .setValue(NicknameModel(name, memo))
                alertDialog.dismiss()
                Toast.makeText(context, "프로필 변경", Toast.LENGTH_SHORT).show()
            }
        }
        alertDialog.findViewById<Button>(R.id.dcancel)?.setOnClickListener {
            alertDialog.dismiss()
        }
    }
    private fun showDialog2() {
        val mDialogView = LayoutInflater.from(context).inflate(R.layout.custom_dialog2, null)
        val mBuilder = AlertDialog.Builder(requireContext())
                .setView(mDialogView)
                .setTitle("회원을 탈퇴하면 되돌릴 수 없어요 괜찮나요?")

        val alertDialog = mBuilder.show()
        alertDialog.findViewById<Button>(R.id.dyes)?.setOnClickListener {
            val user = Firebase.auth.currentUser!!
            val uid = FBAuth.getUid()
            Logout()
            //회원아이디 삭제
            user.delete()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(requireContext(),"탈퇴되었습니다",Toast.LENGTH_SHORT).show()
                            alertDialog.dismiss()
                            val intent = Intent(context, LoginActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
                    }
            //데이터삭제
            FBRef.uidRef.child(uid).removeValue()
            FBRef.nicknameRef.child(uid).removeValue()

//            FBRef.boardRef.
//            FBRef.commentRef.child(uid).removeValue()
        }
        alertDialog.findViewById<Button>(R.id.dno)?.setOnClickListener {
            alertDialog.dismiss()
        }
    }
    private fun Logout(){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("591458789988-8q5k10cud95t39tpeml69vgkpamm41ho.apps.googleusercontent.com")
            .requestEmail()
            .build()
        auth.signOut()
        GoogleSignIn.getClient(context,gso).signOut()
    }

}