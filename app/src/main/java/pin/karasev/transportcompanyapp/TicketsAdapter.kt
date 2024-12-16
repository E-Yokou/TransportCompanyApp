package pin.karasev.transportcompanyapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pin.karasev.transportcompanyapp.models.TicketDto

class TicketsAdapter(var tickets: List<TicketDto>) : RecyclerView.Adapter<TicketsAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tripNumber: TextView = view.findViewById(R.id.ticket_trip_number)
        val seatNumber: TextView = view.findViewById(R.id.ticket_seat_number)
        val price: TextView = view.findViewById(R.id.ticket_price)
        val departureLocation: TextView = view.findViewById(R.id.ticket_departure_location)
        val destinationLocation: TextView = view.findViewById(R.id.ticket_destination_location)
        val departureDatetime: TextView = view.findViewById(R.id.ticket_departure_datetime)
        val arrivalDatetime: TextView = view.findViewById(R.id.ticket_arrival_datetime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ticket_in_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tickets.count()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tripNumber.text = "Маршрут: " + tickets[position].tripNumber
        holder.seatNumber.text = "Место: " + tickets[position].seatNumber.toString()
        holder.price.text = "Цена: " + tickets[position].price.toString() + "₽"
        holder.departureLocation.text = "Откуда: " + tickets[position].departureLocation
        holder.destinationLocation.text = "Куда: " + tickets[position].destinationLocation
        holder.departureDatetime.text = "Отбытие: " + tickets[position].departureDatetime
        holder.arrivalDatetime.text = "Прибытие: " + tickets[position].arrivalDatetime
    }
}
