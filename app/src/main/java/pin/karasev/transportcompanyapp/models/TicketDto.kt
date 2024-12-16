package pin.karasev.transportcompanyapp.models

data class TicketDto(
    val tripId: Int,
    val userId: Int,
    val tripNumber: String,
    val seatNumber: Int,
    val price: Int,
    val departureLocation: String,
    val destinationLocation: String,
    val departureDatetime: String,
    val arrivalDatetime: String
)
