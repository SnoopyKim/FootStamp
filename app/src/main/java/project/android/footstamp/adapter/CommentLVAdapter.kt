package project.android.footstamp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import project.android.footstamp.R
import project.android.footstamp.model.CommentModel
import project.android.footstamp.utils.FBAuth

class CommentLVAdapter (val commentList : MutableList<CommentModel>,val uid : String) : BaseAdapter() {
    override fun getCount(): Int {
        return commentList.size
    }

    override fun getItem(p0: Int): Any {
        return commentList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var view = p1

        if (view == null) {
            view = LayoutInflater.from(p2?.context).inflate(R.layout.comment_model, p2, false)
        }
        val comuid = FBAuth.getUid()
        val uidarea = view?.findViewById<TextView>(R.id.comuidArea)
        val textarea = view?.findViewById<TextView>(R.id.comcontentArea)
        val timearea = view?.findViewById<TextView>(R.id.comtimeArea)

        textarea!!.text = commentList[p0].commentText
        timearea!!.text = commentList[p0].commentCreatedTime

//        if (comuid.equals(uid)){
//            uidarea!!.text = "글쓴이"
//        }

        return view!!
    }
}