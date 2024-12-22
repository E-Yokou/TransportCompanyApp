package pin.karasev.transportcompanyapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(activity: FragmentActivity, private val userLogin: String) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        val fragment = when (position) {
            0 -> LastTicketFragment()
            1 -> TodayTicketsFragment()
            else -> OtherTicketsFragment()
        }
        fragment.arguments = Bundle().apply {
            putString("USER_LOGIN", userLogin)
        }
        return fragment
    }
}