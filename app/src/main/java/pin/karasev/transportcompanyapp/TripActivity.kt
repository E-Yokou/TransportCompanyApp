package pin.karasev.transportcompanyapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TripActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip)

        val numberRoute: TextView = findViewById(R.id.trip_list_details_numberRoute)
        val departureLocation: TextView = findViewById(R.id.trip_list_details_departureLocation)
        val destinationLocation: TextView = findViewById(R.id.trip_list_details_destinationLocation)
        val occupiedSeats: TextView = findViewById(R.id.trip_list_details_occupiedSeats)
        val departureDatetime: TextView = findViewById(R.id.trip_list_details_departureDatetime)
        val arrivalDatetime: TextView = findViewById(R.id.trip_list_details_arrivalDatetime)
        val price: TextView = findViewById(R.id.trip_list_details_price)

        val btnBack: Button = findViewById(R.id.btn_back)

        numberRoute.text = intent.getStringExtra("tripNumberTrip")
        departureLocation.text = intent.getStringExtra("tripDepartureLocation")
        destinationLocation.text = intent.getStringExtra("tripDestinationLocation")
        occupiedSeats.text = intent.getStringExtra("tripOccupiedSeats")
        departureDatetime.text = intent.getStringExtra("tripDepartureDatetime")
        arrivalDatetime.text = intent.getStringExtra("tripArrivalDatetime")
        price.text = intent.getStringExtra("tripPrice")

        btnBack.setOnClickListener {
            finish()
        }
    }
}