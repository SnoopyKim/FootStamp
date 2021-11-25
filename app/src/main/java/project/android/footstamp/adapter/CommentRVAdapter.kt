package project.android.footstamp.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
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
import java.text.SimpleDateFormat
import java.util.*

class CommentRVAdapter (val context : Context, val commentList : MutableList<CommentModel>,val key: String, val uid : String)
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
            val comDelBtn = itemView.findViewById<ImageButton>(R.id.comDelBtn)
            val comkey = item.commentkey.toString()

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
            val currentDateTime = Calendar.getInstance().time
            val dateFormat = SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.KOREA).format(currentDateTime)
            val nowcalendar = Calendar.getInstance()
            val comcalendar = Calendar.getInstance()

            var time = dateFormat.toString().split("."," ",":")
            var year = time[0].toInt()
            var month = time[1].toInt()
            var day = time[2].toInt()
            var hour = time[3].toInt()
            var minute = time[4].toInt()

            var commtime = item.commentCreatedTime.split("."," ",":")
            var comyear = commtime[0].toInt()
            var commonth = commtime[1].toInt()
            var comday = commtime[2].toInt()
            var comhour = commtime[3].toInt()
            var comminute = commtime[4].toInt()

            nowcalendar.set(year, month, day, hour, minute)
            comcalendar.set(comyear,commonth,comday,comhour,comminute)
            val final_date= (nowcalendar.timeInMillis/1000/60-comcalendar.timeInMillis/1000/60).toInt()
            if(final_date<60){
                comtime!!.setText(final_date.toString()+"분 전")
            }else {
                comtime!!.setText(item.commentCreatedTime)
            }
            comcontent!!.setText(item.commentText)

            if (item.commentuid.equals(FBAuth.getUid())){
                comMy.isVisible = true
            }
            if (item.commentuid.equals(FBAuth.getUid())){
                comDelBtn.isVisible = true
            }
            comDelBtn.setOnClickListener {
                showDialog(key,comkey)
            }
        }
    }
    override fun getItemViewType(position:Int): Int {
        return position;
    }
    private fun showDialog(key : String,comkey : String) {
        val mDialogView = LayoutInflater.from(context).inflate(R.layout.custom_dialog2, null)
        val mBuilder = AlertDialog.Builder(context)
            .setView(mDialogView)
            .setTitle("정말 댓글을 삭제할래요?")

        val alertDialog = mBuilder.show()

        alertDialog.findViewById<Button>(R.id.dyes)?.setOnClickListener {
            FBRef.commentRef.child(key).child(comkey).removeValue()
            alertDialog.dismiss()
        }
        alertDialog.findViewById<Button>(R.id.dno)?.setOnClickListener {
            alertDialog.dismiss()

        }
    }
}

