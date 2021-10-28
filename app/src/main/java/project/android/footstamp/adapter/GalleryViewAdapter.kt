package project.android.footstamp.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.icu.text.Transliterator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.contentValuesOf
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import project.android.footstamp.R
import project.android.footstamp.fragment.ViewFragment
import project.android.footstamp.model.Stamp
import project.android.footstamp.utils.PostModel

class  GalleryViewAdapter(val context : Context,
                          val List : MutableList<PostModel>)
    : RecyclerView.Adapter<GalleryViewAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int, ): GalleryViewAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_stamp_list,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: GalleryViewAdapter.ViewHolder, position: Int) {
        holder.bindItems(List[position])
    }

    override fun getItemCount(): Int {
        return List.size
    }
        inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
            fun bindItems(item : PostModel){
                val date = itemView.findViewById<TextView>(R.id.ItemStampText)
                val memo = itemView.findViewById<TextView>(R.id.ItemStampMemo)
                val image = itemView.findViewById<ImageView>(R.id.ItemStampImage)
                val key = item.key
                val storageReference = Firebase.storage.reference.child(key + ".png")

                date!!.setText(List[position].time)
                memo!!.setText(List[position].memo)


                storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
                    if (task.isSuccessful) {

                        Glide.with(context)
                            .load(task.result)
                            .into(image)
                    }
                })


                itemView.setOnClickListener{
                    Toast.makeText(context,"터치 확인",Toast.LENGTH_SHORT).show()
                }
            }
        }
}