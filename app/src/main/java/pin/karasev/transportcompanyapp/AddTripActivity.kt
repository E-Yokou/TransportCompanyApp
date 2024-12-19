package pin.karasev.transportcompanyapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pin.karasev.transportcompanyapp.models.Trip

class AddTripActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_trip)

        val tripNumber: EditText = findViewById(R.id.trip_number)
        val departureLocation: EditText = findViewById(R.id.departure_location)
        val destinationLocation: EditText = findViewById(R.id.destination_location)
        val departureDatetime: EditText = findViewById(R.id.departure_datetime)
        val arrivalDatetime: EditText = findViewById(R.id.arrival_datetime)
        val price: EditText = findViewById(R.id.price)

        val btnAddTrip: Button = findViewById(R.id.btn_add_trip)

        btnAddTrip.setOnClickListener {
            val trip = Trip(
                id = 0,
                image = "logo",
                tripNumber = tripNumber.text.toString(),
                departureLocation = departureLocation.text.toString(),
                destinationLocation = destinationLocation.text.toString(),
                occupiedSeats = 0,
                departureDatetime = departureDatetime.text.toString(),
                arrivalDatetime = arrivalDatetime.text.toString(),
                price = price.text.toString().toInt()
            )

            val db = DbHelper(this, null)
            db.addTrip(trip)

            Toast.makeText(this, "Поездка добавлена", Toast.LENGTH_LONG).show()
            finish()
        }
    }
}