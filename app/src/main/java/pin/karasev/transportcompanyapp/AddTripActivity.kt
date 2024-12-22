package pin.karasev.transportcompanyapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pin.karasev.transportcompanyapp.models.Trip
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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

        // Устанавливаем обработчики нажатий для полей даты и времени
        departureDatetime.setOnClickListener { showDateTimePickerDialog(departureDatetime) }
        arrivalDatetime.setOnClickListener { showDateTimePickerDialog(arrivalDatetime) }

        btnAddTrip.setOnClickListener {
            val departureDateTimeFormatted = formatDateTime(departureDatetime.text.toString())
            val arrivalDateTimeFormatted = formatDateTime(arrivalDatetime.text.toString())

            if (departureDateTimeFormatted.isNotEmpty() && arrivalDateTimeFormatted.isNotEmpty()) {
                val departureDateTime = parseDateTime(departureDateTimeFormatted)
                val arrivalDateTime = parseDateTime(arrivalDateTimeFormatted)

                if (departureDateTime.before(arrivalDateTime)) {
                    val trip = Trip(
                        id = 0,
                        image = "logo",
                        tripNumber = tripNumber.text.toString(),
                        departureLocation = departureLocation.text.toString(),
                        destinationLocation = destinationLocation.text.toString(),
                        occupiedSeats = 0,
                        departureDatetime = departureDateTimeFormatted,
                        arrivalDatetime = arrivalDateTimeFormatted,
                        price = price.text.toString().toInt()
                    )

                    val db = DbHelper(this, null)
                    db.addTrip(trip)

                    Toast.makeText(this, "Поездка добавлена", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    departureDatetime.text.clear()
                    arrivalDatetime.text.clear()
                    Toast.makeText(this, "Время отправления не может быть позже или равно времени прибытия", Toast.LENGTH_LONG).show()
                }
            } else {
                departureDatetime.text.clear()
                arrivalDatetime.text.clear()
                Toast.makeText(this, "Неверный формат даты и времени", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showDateTimePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val timePickerDialog = TimePickerDialog(
                    this,
                    { _, hourOfDay, minute ->
                        val selectedDateTime = Calendar.getInstance()
                        selectedDateTime.set(year, month, dayOfMonth, hourOfDay, minute)
                        val formattedDateTime = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(selectedDateTime.time)
                        editText.setText(formattedDateTime)
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                )
                timePickerDialog.show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun formatDateTime(datetime: String): String {
        val inputFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd.MM.yyyy | HH:mm", Locale.getDefault())
        return try {
            val date = inputFormat.parse(datetime)
            outputFormat.format(date)
        } catch (e: Exception) {
            ""
        }
    }

    private fun parseDateTime(datetime: String): Calendar {
        val inputFormat = SimpleDateFormat("dd.MM.yyyy | HH:mm", Locale.getDefault())
        val calendar = Calendar.getInstance()
        try {
            val date = inputFormat.parse(datetime)
            calendar.time = date
        } catch (e: Exception) {

        }
        return calendar
    }
}
