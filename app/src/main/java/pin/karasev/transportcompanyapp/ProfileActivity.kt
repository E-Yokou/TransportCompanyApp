package pin.karasev.transportcompanyapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val userLoginTextView: TextView = findViewById(R.id.user_login)
        val currentUserLogin = intent.getStringExtra("USER_LOGIN") ?: ""

        val btnBack: Button = findViewById(R.id.btn_back)

        // Отображаем логин пользователя
        userLoginTextView.text = "Логин: $currentUserLogin"

        val tabLayout: TabLayout = findViewById(R.id.tabLayout)
        val viewPager: ViewPager2 = findViewById(R.id.viewPager)

        val adapter = ViewPagerAdapter(this, currentUserLogin)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Последний билет"
                1 -> "Билеты на сегодня"
                else -> "Остальные билеты"
            }
        }.attach()

        btnBack.setOnClickListener {
            finish()
        }
    }
}