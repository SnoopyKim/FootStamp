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

public class GalleryViewAdapter(private var list : MutableList<Stamp>) : RecyclerView.Adapter<GalleryViewAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_stamp_list,parent,false)

        return ViewHolder(view)
    }

    interface ItemClick{
        fun onClick(view : View, Position: Int)
    }
    var itemClick : ItemClick? = null

    override fun onBindViewHolder(holder: GalleryViewAdapter.ViewHolder, position: Int) {
        if (itemClick != null){
            holder?.itemView.setOnClickListener{ view->
                itemClick!!.onClick(view,position)
            }
        }

        Log.d("GalleryViewAdapter", list[position].id)
        holder.bindItems(list[position])
    }

    fun setList(newList: List<Stamp>) {

        list = newList as MutableList<Stamp>
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){

        fun bindItems(item : Stamp){

            val GalText = itemView.findViewById<TextView>(R.id.ItemStampText)
            val GalMemo = itemView.findViewById<TextView>(R.id.ItemStampMemo)
            val GalImg = itemView.findViewById<ImageView>(R.id.ItemStampImage)

            GalText.text = "${item.area} ${item.date}"
            GalMemo.text = item.memo

            val bitmap = item.image?.size?.let { BitmapFactory.decodeByteArray(item.image, 0, it) }
            GalImg.setImageBitmap(bitmap)
        }
    }
}
