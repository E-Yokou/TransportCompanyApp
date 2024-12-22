package pin.karasev.transportcompanyapp.models

data class User(
    val id: Int? = null,
    val login: String,
    val email: String,
    val pass: String,
    val role: String
)
