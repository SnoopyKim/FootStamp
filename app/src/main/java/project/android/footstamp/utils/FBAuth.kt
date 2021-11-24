package project.android.footstamp.utils

import android.provider.ContactsContract
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

class FBAuth {companion object{
    private lateinit var auth: FirebaseAuth

    fun getUid() : String{

        auth = FirebaseAuth.getInstance()

        return auth.currentUser?.uid.toString()
        }
    fun getNickname() : String{

        auth = FirebaseAuth.getInstance()

        return FBRef.nicknameRef.child(auth.currentUser?.uid.toString()).get().toString()
    }

    fun getTime() : String {

        val currentDateTime = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.KOREA).format(currentDateTime)
        return dateFormat
    }
    }
}