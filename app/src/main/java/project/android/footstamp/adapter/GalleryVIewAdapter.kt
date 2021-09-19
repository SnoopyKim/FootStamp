package project.android.footstamp.adapter

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import project.android.footstamp.R

public class GalleryVIewAdapter(val items: MutableList<String>) : RecyclerView.Adapter<GalleryVIewAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryVIewAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_stamp_list,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: GalleryVIewAdapter.ViewHolder, position: Int) {
        holder.bindItems(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
        fun bindItems(item : String){

            val Gallery_text = itemView.findViewById<TextView>(R.id.ItemStampText)
            Gallery_text.text = item

        }
    }
}
