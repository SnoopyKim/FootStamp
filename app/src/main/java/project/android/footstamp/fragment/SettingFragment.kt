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
                    binding.nicknameText.setText("???????????? ????????? ?????????")
                }
                if (nameModel?.memo!=null) {
                    if (nameModel?.memo.toString() == "") {
                        binding.setMemo.setText("??? ??? ????????? ????????????")
                    } else {
                        binding.setMemo.setText(nameModel?.memo.toString())
                    }
                } else{
                    binding.setMemo.setText("??? ??? ????????? ????????????")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message

            }
        }
       FBRef.nicknameRef.child(uid).addValueEventListener(postListener)


        //???????????? ??????

        binding.logOutBtn.setOnClickListener{
            Logout()
            val intent = Intent(context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
        //????????????
        binding.logDelBtn.setOnClickListener {
            showDialog2()
        }
        return binding.root
    }


        fun newInstance(param1: String, param2: String) =
            SettingFragment().apply {
                // TODO: ?????? ??????

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
                Toast.makeText(requireContext(), "???????????? ??????????????????", Toast.LENGTH_SHORT).show()
                namerule = false
            }

            if (name.length > 12) {
                Toast.makeText(requireContext(), "???????????? 12???????????? ???????????? ??????", Toast.LENGTH_SHORT).show()
                namerule = false
            }

            if (name.length < 2) {
                Toast.makeText(requireContext(), "???????????? 2??????????????? ???????????? ??????", Toast.LENGTH_SHORT).show()
                namerule = false
            }
            if (memo.length > 20) {
                Toast.makeText(requireContext(), "????????? 20???????????? ???????????? ??????", Toast.LENGTH_SHORT).show()
                namerule = false
            }

            if (namerule) {
                FBRef.nicknameRef
                        .child(uid)
                        .setValue(NicknameModel(name, memo))
                alertDialog.dismiss()
                Toast.makeText(context, "????????? ??????", Toast.LENGTH_SHORT).show()
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
                .setTitle("????????? ???????????? ????????? ??? ????????? ?????????????")

        val alertDialog = mBuilder.show()
        alertDialog.findViewById<Button>(R.id.dyes)?.setOnClickListener {
            val user = Firebase.auth.currentUser!!
            val uid = FBAuth.getUid()
            Logout()
            //??????????????? ??????
            user.delete()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(requireContext(),"?????????????????????",Toast.LENGTH_SHORT).show()
                            alertDialog.dismiss()
                            val intent = Intent(context, LoginActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
                    }
            //???????????????
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