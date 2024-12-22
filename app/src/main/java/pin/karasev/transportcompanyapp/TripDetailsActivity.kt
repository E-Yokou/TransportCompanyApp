package pin.karasev.transportcompanyapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import pin.karasev.transportcompanyapp.models.Trip

class TripDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_details)

        val tripId = intent.getIntExtra("tripId", -1)
        val db = DbHelper(this, null)
        val trip = db.getTripById(tripId)
        val tickets = db.getTicketsForTrip(tripId)

        val tripNumber: TextView = findViewById(R.id.trip_details_number)
        val departureLocation: TextView = findViewById(R.id.trip_details_departureLocation)
        val destinationLocation: TextView = findViewById(R.id.trip_details_destinationLocation)
        val occupiedSeats: TextView = findViewById(R.id.trip_details_occupiedSeats)
        val totalPrice: TextView = findViewById(R.id.trip_details_totalPrice)
        val btnBack: Button = findViewById(R.id.btn_back)

        tripNumber.text = "Номер маршрута: " + trip.tripNumber
        departureLocation.text = "Откуда: " + trip.departureLocation
        destinationLocation.text = "Куда: " + trip.destinationLocation
        occupiedSeats.text = "Занятых мест: " + tickets.size
        totalPrice.text = "Сумма всех купленных мест: " + (tickets.size * trip.price) + "₽"

        btnBack.setOnClickListener {
            finish()
        }
    }
}
