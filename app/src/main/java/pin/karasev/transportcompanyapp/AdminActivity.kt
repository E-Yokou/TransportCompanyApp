package pin.karasev.transportcompanyapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pin.karasev.transportcompanyapp.models.TicketDto
import pin.karasev.transportcompanyapp.models.Trip
import pin.karasev.transportcompanyapp.models.User

class AdminActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnViewUsers: Button
    private lateinit var btnViewTickets: Button
    private lateinit var btnViewTrips: Button
    private lateinit var btnAddTrip: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        recyclerView = findViewById(R.id.recycler_view)
        btnViewUsers = findViewById(R.id.btn_view_users)
        btnViewTickets = findViewById(R.id.btn_view_tickets)
        btnAddTrip = findViewById(R.id.btn_add_trip)

        btnViewUsers.setOnClickListener {
            showUsers()
        }

        btnViewTickets.setOnClickListener {
            showTickets()
        }

        btnAddTrip.setOnClickListener {
            addTrip()
        }
    }

    private fun showUsers() {
        val db = DbHelper(this, null)
        val users = db.getAllUsers()
        recyclerView.adapter = UsersAdapter(users, this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.visibility = RecyclerView.VISIBLE
    }

    private fun showTickets() {
        val db = DbHelper(this, null)
        val tickets = db.getAllTickets()
        recyclerView.adapter = TicketsAdapter(tickets)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.visibility = RecyclerView.VISIBLE
    }

    private fun addTrip() {
        val intent = Intent(this, AddTripActivity::class.java)
        startActivity(intent)
    }

    fun updateUserRole(login: String, role: String) {
        val db = DbHelper(this, null)
        db.updateUserRole(login, role)
        Toast.makeText(this, "Роль пользователя $login обновлена на $role", Toast.LENGTH_LONG).show()
        showUsers() // Refresh the user list
    }
}
