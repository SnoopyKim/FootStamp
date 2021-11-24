package project.android.footstamp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import project.android.footstamp.R
import project.android.footstamp.model.CommentModel
import project.android.footstamp.utils.BoardModel
import project.android.footstamp.utils.FBAuth
import project.android.footstamp.utils.FBRef
import project.android.footstamp.utils.NicknameModel

class CommentRVAdapter (val commentList : MutableList<CommentModel>, val uid : String)
    : RecyclerView.Adapter<CommentRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentRVAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.comment_model,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: CommentRVAdapter.ViewHolder, position: Int) {
        holder.bindItems(commentList[position])
    }

    override fun getItemCount(): Int {
        return commentList.size
    }
    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        fun bindItems(item: CommentModel) {
            getItemViewType()

            val comnickname = itemView.findViewById<TextView>(R.id.comnicknameArea)
            val comtime = itemView.findViewById<TextView>(R.id.comtimeArea)
            val comcontent = itemView.findViewById<TextView>(R.id.comcontentArea)
            val comMy = itemView.findViewById<ImageView>(R.id.comMyimage)
            val comWriter = itemView.findViewById<ImageView>(R.id.comWriter)

            val postListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Get Post object and use the values to update the UI
                    val nameModel = dataSnapshot.getValue(NicknameModel::class.java)
                    Log.d("cheese", nameModel.toString())
                    try {
                        comnickname.setText(nameModel?.nickname.toString())
                    } catch (e: Exception) {
                        comnickname.setText("익명")
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message

                }
            }
            FBRef.nicknameRef.child(item.commentuid).addValueEventListener(postListener)



            if (item.commentuid.equals(uid)){
                comWriter.isVisible = true
            }
            comtime!!.setText(item.commentCreatedTime)
            comcontent!!.setText(item.commentText)

            if (item.commentuid.equals(FBAuth.getUid())){
                comMy.isVisible = true
            }

        }
    }
    override fun getItemViewType(position:Int): Int {
        return position;
    }
}

