package project.android.footstamp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import project.android.footstamp.R
import project.android.footstamp.utils.PostModel

class BoardAdapter(val context: Context, val List : MutableList<PostModel>,val uid : String)
    :RecyclerView.Adapter<BoardAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_view_list,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: BoardAdapter.ViewHolder, position: Int) {
        holder.bindItems(List[position])
    }

    override fun getItemCount(): Int {
        return List.size
    }
        inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
            fun bindItems(item:PostModel){

            }
        }
}
