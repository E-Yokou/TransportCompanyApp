package pin.karasev.transportcompanyapp

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val userLogin: EditText = findViewById(R.id.user_login)
        val userPassword: EditText = findViewById(R.id.user_password)
        val button: ConstraintLayout = findViewById(R.id.button_register)
        val linkToReg: TextView = findViewById(R.id.link_to_register)

        linkToReg.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        button.setOnClickListener {
            val login = userLogin.text.toString().trim()
            val pass = userPassword.text.toString().trim()

            if (login.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Не все поля заполнены!", Toast.LENGTH_LONG).show()
            } else {
                val db = DbHelper(this, null)
                val isAuth = db.getUser(login, pass)

                if (isAuth) {
                    val role = db.getUserRole(login) // Get the user's role
                    Toast.makeText(this, "Пользователь $login авторизован", Toast.LENGTH_LONG).show()
                    userLogin.text.clear()
                    userPassword.text.clear()

                    val intent = if (role == "admin") {
                        Intent(this, AdminActivity::class.java)
                    } else {
                        Intent(this, TripsActivity::class.java)
                    }
                    intent.putExtra("USER_LOGIN", login)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Пользователь $login не авторизован!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
