package project.android.footstamp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import project.android.footstamp.R
import project.android.footstamp.model.Stamp

class ViewAdapter (var items : MutableList<String>) : RecyclerView.Adapter<ViewAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.spinnerchoose,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewAdapter.ViewHolder, position: Int) {
        holder.bindItems(items[position]) }


    override fun getItemCount(): Int {
        return items.size
    }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            fun bindItems(item: String){

                val sv_text = itemView.findViewById<Button>(R.id.sv_button)
                sv_text.text = item
                //아이템 선택 리스너 지정필요
            }

        }
}

