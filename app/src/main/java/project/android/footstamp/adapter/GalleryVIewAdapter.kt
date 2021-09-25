package project.android.footstamp.adapter

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import project.android.footstamp.R

public class GalleryVIewAdapter(val List : MutableList<ContentsModel>) : RecyclerView.Adapter<GalleryVIewAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryVIewAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_stamp_list,parent,false)

        return ViewHolder(view)
    }

    interface ItemClick{
        fun onClick(view : View, Position: Int)
    }
    var itemClick : ItemClick? = null

    override fun onBindViewHolder(holder: GalleryVIewAdapter.ViewHolder, position: Int) {
        if (itemClick != null){
            holder?.itemView.setOnClickListener{ view->
                itemClick!!.onClick(view,position)
            }
        }
        holder.bindItems(List[position])
    }

    override fun getItemCount(): Int {
        return List.size
    }

    inner class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){

        fun bindItems(item : ContentsModel){

            val GalText = itemView.findViewById<TextView>(R.id.ItemStampText)
            val GalMemo = itemView.findViewById<TextView>(R.id.ItemStampMemo)
            val GalImg = itemView.findViewById<ImageView>(R.id.ItemStampImage)

            GalText.text = item.GalStampText
            GalMemo.text = item.GalStampMemo


        }
    }
}
