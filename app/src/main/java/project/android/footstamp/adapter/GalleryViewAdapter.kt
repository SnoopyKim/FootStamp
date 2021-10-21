package project.android.footstamp.adapter

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import project.android.footstamp.R
import project.android.footstamp.model.Stamp

public class GalleryViewAdapter(val items : ArrayList<String>) : RecyclerView.Adapter<GalleryViewAdapter.Viewholder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewAdapter.Viewholder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_stamp_list,parent,false)
        return Viewholder(v)
    }

    override fun onBindViewHolder(holder: GalleryViewAdapter.Viewholder, position: Int) {
        holder.bindItems(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class  Viewholder(itemView : View) : RecyclerView.ViewHolder(itemView){

        fun bindItems(item : String){

        }
    }
}