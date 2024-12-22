package pin.karasev.transportcompanyapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Base64
import java.security.MessageDigest
import pin.karasev.transportcompanyapp.models.Ticket
import pin.karasev.transportcompanyapp.models.TicketDto
import pin.karasev.transportcompanyapp.models.Trip
import pin.karasev.transportcompanyapp.models.User
import java.text.SimpleDateFormat
import java.util.Locale

class DbHelper(val context: Context, factory: SQLiteDatabase.CursorFactory?) : SQLiteOpenHelper(context, "mobileDB", factory, 11) {

    override fun onCreate(db: SQLiteDatabase?) {
        val queryUsers =
            "CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, login TEXT, email TEXT, pass TEXT, role TEXT)"
        val queryTickets =
            "CREATE TABLE tickets (id INTEGER PRIMARY KEY AUTOINCREMENT, tripId INT, userId INT, seatNumber INT, price INT)"
        val queryTrips =
            "CREATE TABLE trips (id INTEGER PRIMARY KEY AUTOINCREMENT, image TEXT, tripNumber TEXT, departureLocation TEXT, destinationLocation TEXT, occupiedSeats INT, departureDatetime TEXT, arrivalDatetime TEXT, price INT)"
        db!!.execSQL(queryUsers)
        db.execSQL(queryTickets)
        db.execSQL(queryTrips)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS users")
        db.execSQL("DROP TABLE IF EXISTS tickets")
        db.execSQL("DROP TABLE IF EXISTS trips")
        onCreate(db)
    }

    fun addUser(user: User) {
        val values = ContentValues()
        values.put("login", user.login)
        values.put("email", user.email)
        values.put("pass", hashPassword(user.pass))
        values.put("role", user.role)

        val db = this.writableDatabase
        db.insert("users", null, values)

        db.close()
    }


    fun getUser(login: String, pass: String): Boolean {
        val db = this.readableDatabase
        val hashedPass = hashPassword(pass)
        val result =
            db.rawQuery("SELECT * FROM users WHERE login = '$login' AND pass = '$hashedPass'", null)
        return result.moveToFirst()
    }

    fun addTicket(ticket: Ticket) {
        val values = ContentValues()
        values.put("tripId", ticket.tripId)
        values.put("userId", ticket.userId)
        values.put("seatNumber", ticket.seatNumber)
        values.put("price", ticket.price)

        val db = this.writableDatabase
        db.insert("tickets", null, values)

        db.close()
    }

    fun getCurrentUserId(login: String): Int {
        val db = this.readableDatabase
        val query = "SELECT id FROM users WHERE login = ?"
        val cursor = db.rawQuery(query, arrayOf(login))
        var userId = -1
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
        }
        cursor.close()
        db.close()
        return userId
    }

    fun updateOccupiedSeats(tripId: Int) {
        val db = this.writableDatabase
        val query = "UPDATE trips SET occupiedSeats = occupiedSeats + 1 WHERE id = ?"
        db.execSQL(query, arrayOf(tripId.toString()))
        db.close()
    }

    fun addTrip(trip: Trip) {
        val values = ContentValues()
        values.put("image", trip.image)
        values.put("tripNumber", trip.tripNumber)
        values.put("departureLocation", trip.departureLocation)
        values.put("destinationLocation", trip.destinationLocation)
        values.put("occupiedSeats", trip.occupiedSeats)
        values.put("departureDatetime", trip.departureDatetime)
        values.put("arrivalDatetime", trip.arrivalDatetime)
        values.put("price", trip.price)

        val db = this.writableDatabase
        db.insert("trips", null, values)

        db.close()
    }

    fun getAllTrips(): List<Trip> {
        val trips = mutableListOf<Trip>()
        val db = this.readableDatabase
        val query = "SELECT * FROM trips"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val trip = Trip(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    image = cursor.getString(cursor.getColumnIndexOrThrow("image")),
                    tripNumber = cursor.getString(cursor.getColumnIndexOrThrow("tripNumber")),
                    departureLocation = cursor.getString(cursor.getColumnIndexOrThrow("departureLocation")),
                    destinationLocation = cursor.getString(cursor.getColumnIndexOrThrow("destinationLocation")),
                    occupiedSeats = cursor.getInt(cursor.getColumnIndexOrThrow("occupiedSeats")),
                    departureDatetime = cursor.getString(cursor.getColumnIndexOrThrow("departureDatetime")),
                    arrivalDatetime = cursor.getString(cursor.getColumnIndexOrThrow("arrivalDatetime")),
                    price = cursor.getInt(cursor.getColumnIndexOrThrow("price"))
                )
                trips.add(trip)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return trips
    }

    fun searchTrips(departure: String, destination: String): List<Trip> {
        val trips = mutableListOf<Trip>()
        val db = this.readableDatabase
        val query =
            "SELECT * FROM trips WHERE departureLocation LIKE ? AND destinationLocation LIKE ?"
        val cursor = db.rawQuery(query, arrayOf("%$departure%", "%$destination%"))

        if (cursor.moveToFirst()) {
            do {
                val trip = Trip(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    image = cursor.getString(cursor.getColumnIndexOrThrow("image")),
                    tripNumber = cursor.getString(cursor.getColumnIndexOrThrow("tripNumber")),
                    departureLocation = cursor.getString(cursor.getColumnIndexOrThrow("departureLocation")),
                    destinationLocation = cursor.getString(cursor.getColumnIndexOrThrow("destinationLocation")),
                    occupiedSeats = cursor.getInt(cursor.getColumnIndexOrThrow("occupiedSeats")),
                    departureDatetime = cursor.getString(cursor.getColumnIndexOrThrow("departureDatetime")),
                    arrivalDatetime = cursor.getString(cursor.getColumnIndexOrThrow("arrivalDatetime")),
                    price = cursor.getInt(cursor.getColumnIndexOrThrow("price"))
                )
                trips.add(trip)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return trips
    }

    fun getUserTickets(userId: Int): List<TicketDto> {
        val tickets = mutableListOf<TicketDto>()
        val db = this.readableDatabase
        val query = """
            SELECT tickets.*, trips.tripNumber, trips.departureLocation, trips.destinationLocation, trips.departureDatetime, trips.arrivalDatetime
            FROM tickets
            JOIN trips ON tickets.tripId = trips.id
            WHERE tickets.userId = ?
        """
        val cursor = db.rawQuery(query, arrayOf(userId.toString()))

        if (cursor.moveToFirst()) {
            do {
                val ticket = TicketDto(
                    tripId = cursor.getInt(cursor.getColumnIndexOrThrow("tripId")),
                    tripNumber = cursor.getString(cursor.getColumnIndexOrThrow("tripNumber")),
                    userId = cursor.getInt(cursor.getColumnIndexOrThrow("userId")),
                    seatNumber = cursor.getInt(cursor.getColumnIndexOrThrow("seatNumber")),
                    price = cursor.getInt(cursor.getColumnIndexOrThrow("price")),
                    departureLocation = cursor.getString(cursor.getColumnIndexOrThrow("departureLocation")),
                    destinationLocation = cursor.getString(cursor.getColumnIndexOrThrow("destinationLocation")),
                    departureDatetime = cursor.getString(cursor.getColumnIndexOrThrow("departureDatetime")),
                    arrivalDatetime = cursor.getString(cursor.getColumnIndexOrThrow("arrivalDatetime"))
                )
                tickets.add(ticket)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return tickets
    }

    fun getUserRole(login: String): String {
        val db = this.readableDatabase
        val query = "SELECT role FROM users WHERE login = ?"
        val cursor = db.rawQuery(query, arrayOf(login))
        var role = ""
        if (cursor.moveToFirst()) {
            role = cursor.getString(cursor.getColumnIndexOrThrow("role"))
        }
        cursor.close()
        db.close()
        return role
    }

    fun getAllUsers(): List<User> {
        val users = mutableListOf<User>()
        val db = this.readableDatabase
        val query = "SELECT * FROM users"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val user = User(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    login = cursor.getString(cursor.getColumnIndexOrThrow("login")),
                    email = cursor.getString(cursor.getColumnIndexOrThrow("email")),
                    pass = cursor.getString(cursor.getColumnIndexOrThrow("pass")),
                    role = cursor.getString(cursor.getColumnIndexOrThrow("role"))
                )
                users.add(user)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return users
    }

    fun updateUserRoleById(userId: Int, role: String) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("role", role)
        db.update("users", values, "id = ?", arrayOf(userId.toString()))
        db.close()
    }

    fun getAllTickets(): List<TicketDto> {
        val tickets = mutableListOf<TicketDto>()
        val db = this.readableDatabase
        val query = """
        SELECT tickets.*, trips.tripNumber, trips.departureLocation, trips.destinationLocation, trips.departureDatetime, trips.arrivalDatetime
        FROM tickets
        JOIN trips ON tickets.tripId = trips.id
    """
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val ticket = TicketDto(
                    tripId = cursor.getInt(cursor.getColumnIndexOrThrow("tripId")),
                    tripNumber = cursor.getString(cursor.getColumnIndexOrThrow("tripNumber")),
                    userId = cursor.getInt(cursor.getColumnIndexOrThrow("userId")),
                    seatNumber = cursor.getInt(cursor.getColumnIndexOrThrow("seatNumber")),
                    price = cursor.getInt(cursor.getColumnIndexOrThrow("price")),
                    departureLocation = cursor.getString(cursor.getColumnIndexOrThrow("departureLocation")),
                    destinationLocation = cursor.getString(cursor.getColumnIndexOrThrow("destinationLocation")),
                    departureDatetime = cursor.getString(cursor.getColumnIndexOrThrow("departureDatetime")),
                    arrivalDatetime = cursor.getString(cursor.getColumnIndexOrThrow("arrivalDatetime"))
                )
                tickets.add(ticket)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return tickets
    }

    fun updateUserRole(login: String, role: String) {
        val db = this.writableDatabase
        val query = "UPDATE users SET role = ? WHERE login = ?"
        db.execSQL(query, arrayOf(role, login))
        db.close()
    }

    fun getTripById(tripId: Int): Trip {
        val db = this.readableDatabase
        val query = "SELECT * FROM trips WHERE id = ?"
        val cursor = db.rawQuery(query, arrayOf(tripId.toString()))
        var trip = Trip(0, "", "", "", "", 0, "", "", 0)
        if (cursor.moveToFirst()) {
            trip = Trip(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                image = cursor.getString(cursor.getColumnIndexOrThrow("image")),
                tripNumber = cursor.getString(cursor.getColumnIndexOrThrow("tripNumber")),
                departureLocation = cursor.getString(cursor.getColumnIndexOrThrow("departureLocation")),
                destinationLocation = cursor.getString(cursor.getColumnIndexOrThrow("destinationLocation")),
                occupiedSeats = cursor.getInt(cursor.getColumnIndexOrThrow("occupiedSeats")),
                departureDatetime = cursor.getString(cursor.getColumnIndexOrThrow("departureDatetime")),
                arrivalDatetime = cursor.getString(cursor.getColumnIndexOrThrow("arrivalDatetime")),
                price = cursor.getInt(cursor.getColumnIndexOrThrow("price"))
            )
        }
        cursor.close()
        db.close()
        return trip
    }

    fun updateTrip(trip: Trip) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("image", trip.image)
        values.put("tripNumber", trip.tripNumber)
        values.put("departureLocation", trip.departureLocation)
        values.put("destinationLocation", trip.destinationLocation)
        values.put("occupiedSeats", trip.occupiedSeats)
        values.put("departureDatetime", trip.departureDatetime)
        values.put("arrivalDatetime", trip.arrivalDatetime)
        values.put("price", trip.price)

        db.update("trips", values, "id = ?", arrayOf(trip.id.toString()))
        db.close()
    }

    fun getUserTicketsForToday(userId: Int): List<TicketDto> {
        val tickets = mutableListOf<TicketDto>()
        val db = this.readableDatabase
        val today =
            SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(System.currentTimeMillis())
        val query = """
            SELECT tickets.*, trips.tripNumber, trips.departureLocation, trips.destinationLocation, trips.departureDatetime, trips.arrivalDatetime
            FROM tickets
            JOIN trips ON tickets.tripId = trips.id
            WHERE tickets.userId = ? AND trips.departureDatetime LIKE ?
        """
        val cursor = db.rawQuery(query, arrayOf(userId.toString(), "$today%"))

        if (cursor.moveToFirst()) {
            do {
                val ticket = TicketDto(
                    tripId = cursor.getInt(cursor.getColumnIndexOrThrow("tripId")),
                    tripNumber = cursor.getString(cursor.getColumnIndexOrThrow("tripNumber")),
                    userId = cursor.getInt(cursor.getColumnIndexOrThrow("userId")),
                    seatNumber = cursor.getInt(cursor.getColumnIndexOrThrow("seatNumber")),
                    price = cursor.getInt(cursor.getColumnIndexOrThrow("price")),
                    departureLocation = cursor.getString(cursor.getColumnIndexOrThrow("departureLocation")),
                    destinationLocation = cursor.getString(cursor.getColumnIndexOrThrow("destinationLocation")),
                    departureDatetime = cursor.getString(cursor.getColumnIndexOrThrow("departureDatetime")),
                    arrivalDatetime = cursor.getString(cursor.getColumnIndexOrThrow("arrivalDatetime"))
                )
                tickets.add(ticket)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return tickets
    }

    fun getLastPurchasedTicket(userId: Int): TicketDto? {
        val db = this.readableDatabase
        val query = """
            SELECT tickets.*, trips.tripNumber, trips.departureLocation, trips.destinationLocation, trips.departureDatetime, trips.arrivalDatetime
            FROM tickets
            JOIN trips ON tickets.tripId = trips.id
            WHERE tickets.userId = ?
            ORDER BY tickets.id DESC
            LIMIT 1
        """
        val cursor = db.rawQuery(query, arrayOf(userId.toString()))

        var ticket: TicketDto? = null
        if (cursor.moveToFirst()) {
            ticket = TicketDto(
                tripId = cursor.getInt(cursor.getColumnIndexOrThrow("tripId")),
                tripNumber = cursor.getString(cursor.getColumnIndexOrThrow("tripNumber")),
                userId = cursor.getInt(cursor.getColumnIndexOrThrow("userId")),
                seatNumber = cursor.getInt(cursor.getColumnIndexOrThrow("seatNumber")),
                price = cursor.getInt(cursor.getColumnIndexOrThrow("price")),
                departureLocation = cursor.getString(cursor.getColumnIndexOrThrow("departureLocation")),
                destinationLocation = cursor.getString(cursor.getColumnIndexOrThrow("destinationLocation")),
                departureDatetime = cursor.getString(cursor.getColumnIndexOrThrow("departureDatetime")),
                arrivalDatetime = cursor.getString(cursor.getColumnIndexOrThrow("arrivalDatetime"))
            )
        }

        cursor.close()
        db.close()
        return ticket
    }

    fun getOtherPurchasedTickets(userId: Int): List<TicketDto> {
        val tickets = mutableListOf<TicketDto>()
        val db = this.readableDatabase
        val query = """
            SELECT tickets.*, trips.tripNumber, trips.departureLocation, trips.destinationLocation, trips.departureDatetime, trips.arrivalDatetime
            FROM tickets
            JOIN trips ON tickets.tripId = trips.id
            WHERE tickets.userId = ?
            ORDER BY tickets.id DESC
            LIMIT 1, 1000
        """
        val cursor = db.rawQuery(query, arrayOf(userId.toString()))

        if (cursor.moveToFirst()) {
            do {
                val ticket = TicketDto(
                    tripId = cursor.getInt(cursor.getColumnIndexOrThrow("tripId")),
                    tripNumber = cursor.getString(cursor.getColumnIndexOrThrow("tripNumber")),
                    userId = cursor.getInt(cursor.getColumnIndexOrThrow("userId")),
                    seatNumber = cursor.getInt(cursor.getColumnIndexOrThrow("seatNumber")),
                    price = cursor.getInt(cursor.getColumnIndexOrThrow("price")),
                    departureLocation = cursor.getString(cursor.getColumnIndexOrThrow("departureLocation")),
                    destinationLocation = cursor.getString(cursor.getColumnIndexOrThrow("destinationLocation")),
                    departureDatetime = cursor.getString(cursor.getColumnIndexOrThrow("departureDatetime")),
                    arrivalDatetime = cursor.getString(cursor.getColumnIndexOrThrow("arrivalDatetime"))
                )
                tickets.add(ticket)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return tickets
    }

    fun getTicketsForTrip(tripId: Int): List<Ticket> {
        val tickets = mutableListOf<Ticket>()
        val db = this.readableDatabase
        val query = "SELECT * FROM tickets WHERE tripId = ?"
        val cursor = db.rawQuery(query, arrayOf(tripId.toString()))

        if (cursor.moveToFirst()) {
            do {
                val ticket = Ticket(
                    tripId = cursor.getInt(cursor.getColumnIndexOrThrow("tripId")),
                    userId = cursor.getInt(cursor.getColumnIndexOrThrow("userId")),
                    seatNumber = cursor.getInt(cursor.getColumnIndexOrThrow("seatNumber")),
                    price = cursor.getInt(cursor.getColumnIndexOrThrow("price"))
                )
                tickets.add(ticket)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return tickets
    }

    fun isEmailExists(email: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM users WHERE email = ?", arrayOf(email))
        val exists = cursor.moveToFirst()
        cursor.close()
        db.close()
        return exists
    }

    private fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(password.toByteArray())
        return Base64.encodeToString(hash, Base64.DEFAULT)
    }
}