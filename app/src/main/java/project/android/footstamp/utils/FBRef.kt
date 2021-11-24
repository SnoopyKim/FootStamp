package project.android.footstamp.utils

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FBRef {


    companion object{

        private val database = Firebase.database

        val uidRef = database.getReference("uid")
        val boardRef = database.getReference("board")
        val commentRef = database.getReference("comment")
        val nicknameRef = database.getReference("nickname")

    }
}