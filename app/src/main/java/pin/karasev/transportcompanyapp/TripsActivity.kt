package pin.karasev.transportcompanyapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pin.karasev.transportcompanyapp.models.Trip

class TripsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trips)

        val tripsList: RecyclerView = findViewById(R.id.tripList)
        val searchDeparture: EditText = findViewById(R.id.search_departure)
        val searchDestination: EditText = findViewById(R.id.search_destination)
        val searchButton: Button = findViewById(R.id.search_button)
        val btnProfile: Button = findViewById(R.id.btn_profile)
        val currentUserLogin = intent.getStringExtra("USER_LOGIN") ?: ""

//        val trips = arrayListOf<Trip>()
//
//        trips.add(Trip(1, "logo", "A1", "Муром", "Ковардицы", 0, "23.12.2024 | 12:00", "23.12.2024 | 13:00", 150))
//        trips.add(Trip(2, "logo", "A1", "Муром", "Ковардицы", 0, "23.12.2024 | 15:00", "23.12.2024 | 16:00", 150))
//        trips.add(Trip(3, "logo", "A1", "Муром", "Ковардицы", 0, "23.12.2024 | 18:00", "23.12.2024 | 19:00", 150))
//        trips.add(Trip(4, "logo", "A1", "Муром", "Малышево", 0, "24.12.2024 | 12:00", "24.12.2024 | 14:00", 150))
//        trips.add(Trip(5, "logo", "A1", "Муром", "Малышево", 0, "24.12.2024 | 15:00", "24.12.2024 | 17:00", 150))
//        trips.add(Trip(6, "logo", "A1", "Муром", "Малышево", 0, "24.12.2024 | 18:00", "24.12.2024 | 20:00", 150))
//        trips.add(Trip(7, "logo", "A1", "Муром", "Судогда", 0, "23.12.2024 | 12:00", "23.12.2024 | 15:00", 150))
//        trips.add(Trip(8, "logo", "A1", "Муром", "Судогда", 0, "23.12.2024 | 15:00", "23.12.2024 | 18:00", 150))
//        trips.add(Trip(9, "logo", "A1", "Муром", "Судогда", 0, "23.12.2024 | 18:00", "23.12.2024 | 21:00", 150))
//
//        // Вставляем данные о поездках в базу данных
//        val db = DbHelper(this, null)
//        for (trip in trips) {
//            db.addTrip(trip)
//        }

        // Загружаем данные о поездках из базы данных
        val db = DbHelper(this, null)
        var trips = db.getAllTrips()

        tripsList.layoutManager = LinearLayoutManager(this)
        tripsList.adapter = TripsAdapter(trips, this, currentUserLogin, isAdmin = false)

        searchButton.setOnClickListener {
            val departure = searchDeparture.text.toString().trim()
            val destination = searchDestination.text.toString().trim()
            trips = if (departure.isEmpty() && destination.isEmpty()) {
                db.getAllTrips()
            } else {
                db.searchTrips(departure, destination)
            }
            tripsList.adapter = TripsAdapter(trips, this, currentUserLogin, isAdmin = false)
        }

        btnProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("USER_LOGIN", currentUserLogin)
            startActivity(intent)
        }
    }
}