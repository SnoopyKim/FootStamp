package project.android.footstamp.adapter

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import project.android.footstamp.R
import project.android.footstamp.model.Stamp
import project.android.footstamp.utils.PostModel

public class GalleryViewAdapter(val items : MutableList<PostModel>) : BaseAdapter(){
    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(p0: Int): Any {
        return items[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var view = p1

        view = LayoutInflater.from(p2?.context).inflate(R.layout.item_stamp_list,p2,false)

        val itemConLayoutView = view?.findViewById<ConstraintLayout>(R.id.Con)


        val Area = view?.findViewById<TextView>(R.id.ItemStampText)
        val District = view?.findViewById<TextView>(R.id.ItemStampText)
        val memo = view?.findViewById<TextView>(R.id.ItemStampMemo)
        val date = view?.findViewById<TextView>(R.id.ItemStampText)

        date!!.text = items[p0].time
        memo!!.text = items[p0].Memo

        return view
    }
}