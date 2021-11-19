package project.android.footstamp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import project.android.footstamp.R
import project.android.footstamp.model.CommentModel
import project.android.footstamp.utils.BoardModel
import project.android.footstamp.utils.FBAuth

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

            val comuid = itemView.findViewById<TextView>(R.id.comuidArea)
            val comtime = itemView.findViewById<TextView>(R.id.comtimeArea)
            val comcontent = itemView.findViewById<TextView>(R.id.comcontentArea)


            if (item.commentuid.equals(uid)){
                comuid!!.setText("글쓴이")
            } else{
                comuid!!.setText("익명")
            }
            comtime!!.setText(item.commentCreatedTime)
            comcontent!!.setText(item.commentText)

        }
    }
    override fun getItemViewType(position:Int): Int {
        return position;
    }
}

