package project.android.footstamp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import project.android.footstamp.R
import project.android.footstamp.databinding.SpinnerLayoutBinding
import project.android.footstamp.utils.SpinnerModel

class SpinnerAdapter(
        context: Context,
        @LayoutRes private val resId: Int,
        private val values: MutableList<SpinnerModel>
) : ArrayAdapter<SpinnerModel>(context, resId, values) {

    override fun getCount() = values.size


    override fun getItem(position: Int) = values[position]

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = SpinnerLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val model = values[position]
        try {
            binding.spnImage.setColorFilter(ContextCompat.getColor(context, R.color.white))
            binding.spnTxtName.text = model.area.toString()
            binding.spnTxtName.setTextColor(ContextCompat.getColor(context, R.color.white))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return binding.root
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = SpinnerLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val model = values[position]
        try {
            binding.spnTxtName.text = model.area.toString()

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return binding.root
    }

}