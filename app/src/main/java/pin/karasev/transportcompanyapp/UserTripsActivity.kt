package pin.karasev.transportcompanyapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pin.karasev.transportcompanyapp.models.TicketDto

class UserTripsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_trips)

        val recyclerView: RecyclerView = findViewById(R.id.user_trips_recycler_view)
        val userId = intent.getIntExtra("USER_ID", -1)

        if (userId != -1) {
            val db = DbHelper(this, null)
            val tickets: List<TicketDto> = db.getUserTickets(userId)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = TicketsAdapter(tickets)
        } else {
            // Обработка случая, когда userId не передан
            Toast.makeText(this, "Не удалось загрузить данные пользователя", Toast.LENGTH_LONG).show()
            finish()
        }
    }
}
