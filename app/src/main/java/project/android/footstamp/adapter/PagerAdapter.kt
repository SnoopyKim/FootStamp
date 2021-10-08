package project.android.footstamp.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import project.android.footstamp.fragment.GalleryFragment
import project.android.footstamp.fragment.ViewFragment
import project.android.footstamp.fragment.ViewFragment2

class PagerAdapter(fragmentActivity: GalleryFragment) : FragmentStateAdapter(fragmentActivity) {

    companion object{
        private const val NUM_PAGES = 2
    }

    override fun getItemCount(): Int {
        return NUM_PAGES
    }

    override fun createFragment(position: Int): Fragment {
        if (position == 0) {
            return ViewFragment()
        }else {
            return ViewFragment2()
        }
    }
}