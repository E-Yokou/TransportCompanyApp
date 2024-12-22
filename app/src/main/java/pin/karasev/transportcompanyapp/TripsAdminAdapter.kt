package pin.karasev.transportcompanyapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pin.karasev.transportcompanyapp.models.Trip

class TripsAdminAdapter(var trips: List<Trip>, var context: Context) : RecyclerView.Adapter<TripsAdminAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tripNumber: TextView = view.findViewById(R.id.trip_admin_item_number)
        val departureLocation: TextView = view.findViewById(R.id.trip_admin_item_departureLocation)
        val destinationLocation: TextView = view.findViewById(R.id.trip_admin_item_destinationLocation)
        val departureDatetime: TextView = view.findViewById(R.id.trip_admin_item_departureDatetime)
        val arrivalDatetime: TextView = view.findViewById(R.id.trip_admin_item_arrivalDatetime)
        val price: TextView = view.findViewById(R.id.trip_admin_item_price)
        val btnDetail: Button = view.findViewById(R.id.trip_admin_item_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.trip_admin_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return trips.count()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val trip = trips[position]
        holder.tripNumber.text =          "Номер маршрута: " + trip.tripNumber
        holder.departureLocation.text =   "Откуда:   " + trip.departureLocation
        holder.destinationLocation.text = "Куда:       " + trip.destinationLocation
        holder.departureDatetime.text =   "Отбытие:      " + trip.departureDatetime
        holder.arrivalDatetime.text =     "Прибытие: " + trip.arrivalDatetime
        holder.price.text = trip.price.toString() + "₽"

        holder.btnDetail.setOnClickListener {
            val intent = Intent(context, TripDetailsActivity::class.java)
            intent.putExtra("tripId", trip.id)
            context.startActivity(intent)
        }
    }
}