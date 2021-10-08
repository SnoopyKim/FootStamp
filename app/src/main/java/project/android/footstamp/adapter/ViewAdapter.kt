package project.android.footstamp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import project.android.footstamp.R
import project.android.footstamp.model.Stamp

public class ViewAdapter (private var list : MutableList<Stamp>) : RecyclerView.Adapter<ViewAdapter.ViewHolder>(){
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindItems(item: Stamp){

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view_list,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewAdapter.ViewHolder, position: Int) {
        holder?.bindItems(list[position]) }


    override fun getItemCount(): Int {
        return list.size
    }
    
}

