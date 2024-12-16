package pin.karasev.transportcompanyapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import pin.karasev.transportcompanyapp.models.Ticket
import pin.karasev.transportcompanyapp.models.Trip

class TripsAdapter(var trips: List<Trip>, var context: Context, var currentUserLogin: String) : RecyclerView.Adapter<TripsAdapter.MyViewHolder>() {

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.trip_list_image)
        val numberTrip: TextView = view.findViewById(R.id.trip_list_numberRoute)
        val departureLocation: TextView = view.findViewById(R.id.trip_list_departureLocation)
        val destinationLocation: TextView = view.findViewById(R.id.trip_list_destinationLocation)
        val departureDatetime: TextView = view.findViewById(R.id.trip_list_departureDatetime)
        val arrivalDatetime: TextView = view.findViewById(R.id.trip_list_arrivalDatetime)
        val price: TextView = view.findViewById(R.id.trip_list_price)

        val btn: Button = view.findViewById(R.id.trip_list_button)
        val buyTicketBtn: Button = view.findViewById(R.id.trip_list_buy_ticket)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.trip_in_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return trips.count()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.numberTrip.text = "Номер маршрута: " + trips[position].tripNumber
        holder.departureLocation.text = "Откуда: " + trips[position].departureLocation
        holder.destinationLocation.text = "Куда: " + trips[position].destinationLocation
        holder.departureDatetime.text = "Отбытие: " + trips[position].departureDatetime
        holder.arrivalDatetime.text = "Прибытие: " + trips[position].arrivalDatetime
        holder.price.text = trips[position].price.toString() + "₽"

        var imageId = context.resources.getIdentifier(
            trips[position].image,
            "drawable",
            context.packageName
        )

        holder.image.setImageResource(imageId)

        holder.btn.setOnClickListener {
            var intent = Intent(context, TripActivity::class.java)

            intent.putExtra("tripNumberTrip", "Номер маршрута: " + trips[position].tripNumber)
            intent.putExtra("tripDepartureLocation", "Откуда: " + trips[position].departureLocation)
            intent.putExtra("tripDestinationLocation", "Откуда: " + trips[position].destinationLocation)
            intent.putExtra("tripOccupiedSeats", "Место: " + trips[position].occupiedSeats)
            intent.putExtra("tripDepartureDatetime", "Отбытие: " + trips[position].departureDatetime)
            intent.putExtra("tripArrivalDatetime", "Прибытие: " + trips[position].arrivalDatetime)
            intent.putExtra("tripPrice", trips[position].price.toString() + "₽")

            context.startActivity(intent)
        }

        if (trips[position].occupiedSeats >= 50) {
            holder.buyTicketBtn.isEnabled = false
            holder.buyTicketBtn.text = "Билеты закончились"
        } else {
            holder.buyTicketBtn.setOnClickListener {
                val db = DbHelper(context, null)
                val userId = db.getCurrentUserId(currentUserLogin)
                if (userId == -1) {
                    Toast.makeText(context, "Пользователь не найден!", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }

                val ticket = Ticket(
                    tripId = trips[position].id,
                    userId = userId,
                    seatNumber = trips[position].occupiedSeats + 1,
                    price = trips[position].price
                )
                db.addTicket(ticket)
                db.updateOccupiedSeats(trips[position].id)
                Toast.makeText(context, "Билет куплен!", Toast.LENGTH_LONG).show()
            }
        }
    }
}