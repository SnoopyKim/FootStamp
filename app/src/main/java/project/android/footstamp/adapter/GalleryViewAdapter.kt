package project.android.footstamp.adapter

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.BitmapDrawable
import android.icu.text.Transliterator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.contentValuesOf
import androidx.core.os.persistableBundleOf
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import project.android.footstamp.R
import project.android.footstamp.activity.GalleryActivity
import project.android.footstamp.fragment.ViewFragment
import project.android.footstamp.model.Stamp
import project.android.footstamp.utils.PostModel
import project.android.footstamp.activity.MainActivity

class  GalleryViewAdapter(
    val context: Context,
    val List: MutableList<PostModel>, var currentArea: String, var currentDistrict: String
)
    : RecyclerView.Adapter<GalleryViewAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewAdapter.ViewHolder {
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
                val area = itemView.findViewById<TextView>(R.id.ItemStampArea)
                val district = itemView.findViewById<TextView>(R.id.ItemStampDistrict)
                val image = itemView.findViewById<ImageView>(R.id.ItemStampImage)
                val key = item.key
                val storageReference = Firebase.storage.reference.child(key + ".png")
                val con = itemView.findViewById<ConstraintLayout>(R.id.Con)

                date!!.setText(item.time)
                memo!!.setText(item.memo)
                area!!.setText(item.area)
                district!!.setText(item.district)

                getItemViewType()

                storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
                    if (task.isSuccessful) {

                        Glide.with(context)
                            .load(task.result)
                            .into(image)
                    }
                })
                image.setColorFilter(Color.parseColor("#4D626262"),PorterDuff.Mode.DARKEN)


                itemView.setOnClickListener{
                    val intent = Intent(context,GalleryActivity::class.java)
                    intent.putExtra("date",date.text)
                    intent.putExtra("area",area.text)
                    intent.putExtra("district",district.text)
                    intent.putExtra("key",key)
                    intent.putExtra("memo",memo.text)
                    itemView.context.startActivity(intent)

                }
                if (currentArea.equals("전체")){
                        con.maxHeight = 1000
                } else{
                    if (!item.area.equals(currentArea)){
                        con.maxHeight = 0
                    } else {
                        con.maxHeight = 1000
                    }
                }
                if(currentDistrict.equals("전체")){
                } else{
                    if (!item.district.equals(currentDistrict)){
                        con.maxHeight = 0
                        Log.d("TAG",currentDistrict)
                    } else{
                        con.maxHeight = 1000
                    }
                }
            }
        }
    fun setcurrentarea(cua:String){
        currentArea = cua
        notifyDataSetChanged()
    }

    fun setcurrentdistrict(cud:String){
        currentDistrict = cud
        notifyDataSetChanged()
    }

    override fun getItemViewType(position:Int): Int {
        return position;
    }


}