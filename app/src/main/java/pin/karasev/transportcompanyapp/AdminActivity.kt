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
    private lateinit var btnAddTrip: Button
    private lateinit var btnShowTrips: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        recyclerView = findViewById(R.id.recycler_view)
        btnViewUsers = findViewById(R.id.btn_view_users)
        btnViewTickets = findViewById(R.id.btn_view_tickets)
        btnShowTrips = findViewById(R.id.btn_view_trips)
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

        btnShowTrips.setOnClickListener {
            showTrips()
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

    private fun showTrips() {
        val db = DbHelper(this, null)
        val trips = db.getAllTrips()
        recyclerView.adapter = TripsAdminAdapter(trips, this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.visibility = RecyclerView.VISIBLE
    }


    private fun addTrip() {
        val intent = Intent(this, AddTripActivity::class.java)
        startActivity(intent)
    }

    fun showUserTickets(userLogin: String) {
        val db = DbHelper(this, null)
        val userId = db.getCurrentUserId(userLogin)
        val tickets = db.getUserTickets(userId)
        recyclerView.adapter = TicketsAdapter(tickets)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.visibility = RecyclerView.VISIBLE
    }

    fun updateUserRole(userId: Int, role: String) {
        val db = DbHelper(this, null)
        db.updateUserRoleById(userId, role)
        Toast.makeText(this, "Роль пользователя обновлена на $role", Toast.LENGTH_LONG).show()
        showUsers()
    }
}
