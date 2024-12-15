package pin.karasev.transportcompanyapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pin.karasev.transportcompanyapp.models.Trip

class TripsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trips)

        val tripsList: RecyclerView = findViewById(R.id.tripList)
        val trips = arrayListOf<Trip>()

        trips.add(Trip(1, "logo", "A1", "Муром", "Малышево", 0, "12.12.2024 | 12:00", "12.12.2024 | 13:00", 150))
        trips.add(Trip(2, "logo", "A1", "Муром", "Малышево", 0, "12.12.2024 | 15:00", "12.12.2024 | 16:00", 150))
        trips.add(Trip(3, "logo", "A1", "Муром", "Малышево", 0, "12.12.2024 | 18:00", "12.12.2024 | 19:00", 150))

        tripsList.layoutManager = LinearLayoutManager(this)
        tripsList.adapter = TripsAdapter(trips, this)
    }
}