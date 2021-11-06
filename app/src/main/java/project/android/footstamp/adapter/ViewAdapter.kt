package project.android.footstamp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import project.android.footstamp.R

class ViewAdapter(
    val context: Context,
    var items: MutableList<String>) : RecyclerView.Adapter<ViewAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.spinnerchoose,parent,false)

        return ViewHolder(view)
    }

    interface ItemClick
    {
        fun onClick(view: View, position: Int)
    }
    var itemClick : ItemClick? = null

    override fun onBindViewHolder(holder: ViewAdapter.ViewHolder, position: Int) {
        if (itemClick!= null){
            holder?.itemView.setOnClickListener { v->
                itemClick!!.onClick(v,position)
            }
        }
        holder.bindItems(items[position])

        }

    override fun getItemCount(): Int {
        return items.size
    }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            fun bindItems(item: String){

                val sv_text = itemView.findViewById<Button>(R.id.sv_button)
                sv_text.text = item

                }
            }

        }

