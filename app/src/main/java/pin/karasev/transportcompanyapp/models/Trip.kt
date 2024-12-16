package pin.karasev.transportcompanyapp.models

class Trip(val id: Int,
           val image: String,
           val tripNumber: String,
           val departureLocation: String,
           val destinationLocation: String,
           val occupiedSeats: Int,
           val departureDatetime: String,
           val arrivalDatetime: String,
           val price: Int)