package project.android.footstamp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import project.android.footstamp.fragment.GalleryFragment
import project.android.footstamp.fragment.ViewFragment
import project.android.footstamp.fragment.ViewFragment2

class PagerAdapter(fa:FragmentActivity) : FragmentStateAdapter(fa){
    var fragments = listOf<Fragment>()
    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments.get(position)
    }

}