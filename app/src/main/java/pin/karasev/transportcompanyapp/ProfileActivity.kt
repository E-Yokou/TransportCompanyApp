package pin.karasev.transportcompanyapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pin.karasev.transportcompanyapp.models.TicketDto

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val ticketsList: RecyclerView = findViewById(R.id.ticketsList)
        val userLoginTextView: TextView = findViewById(R.id.user_login)
        val currentUserLogin = intent.getStringExtra("USER_LOGIN") ?: ""

        val btnBack: Button = findViewById(R.id.btn_back)

        // Отображаем логин пользователя
        userLoginTextView.text = "Логин: $currentUserLogin"

        // Загружаем данные о купленных билетах из базы данных
        val db = DbHelper(this, null)
        val userId = db.getCurrentUserId(currentUserLogin)
        val tickets = db.getUserTickets(userId)

        ticketsList.layoutManager = LinearLayoutManager(this)
        ticketsList.adapter = TicketsAdapter(tickets)

        btnBack.setOnClickListener {
            finish()
        }
    }
}
