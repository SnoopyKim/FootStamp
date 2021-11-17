package project.android.footstamp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import project.android.footstamp.R
import project.android.footstamp.activity.BoardInsideActivity
import project.android.footstamp.utils.BoardModel
import project.android.footstamp.utils.FBAuth

class BoardAdapter(val context: Context, val List : MutableList<BoardModel>, val uid : String)
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
            fun bindItems(item:BoardModel){
                val date = itemView.findViewById<TextView>(R.id.dateArea)
                val uid = item.uid
                val Area = itemView.findViewById<TextView>(R.id.itemview_area)
                val image = itemView.findViewById<ImageView>(R.id.ItemViewImage)
                val district = itemView.findViewById<TextView>(R.id.itemview_district)
                val key = item.key
                val memo = item.memo
                val storageReference = Firebase.storage.reference.child(key + ".png")

                getItemViewType()

                storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
                    if (task.isSuccessful) {

                        Glide.with(context)
                            .load(task.result)
                            .into(image)
                    }
                })

                date!!.setText(item.time)
                Area!!.setText(item.area)
                district!!.setText(item.district)

                itemView.setOnClickListener {
                    val intent = Intent(context, BoardInsideActivity::class.java)
                    intent.putExtra("date",item.time.toString())
                    intent.putExtra("area",item.area.toString())
                    intent.putExtra("district",item.district.toString())
                    intent.putExtra("key",key.toString())
                    intent.putExtra("memo",memo.toString())
                    intent.putExtra("uid",uid.toString())
                    itemView.context.startActivity(intent)
                }


            }
        }
    override fun getItemViewType(position:Int): Int {
        return position;
    }
}
