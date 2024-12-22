package pin.karasev.transportcompanyapp

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import pin.karasev.transportcompanyapp.models.User

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val userLogin: EditText = findViewById(R.id.user_login)
        val userEmail: EditText = findViewById(R.id.user_email)
        val userPassword: EditText = findViewById(R.id.user_password)
        val button: ConstraintLayout = findViewById(R.id.user_button_register)
        val linkToLogin: TextView = findViewById(R.id.link_to_login)

        linkToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        button.setOnClickListener {
            val login = userLogin.text.toString().trim()
            val email = userEmail.text.toString().trim()
            val pass = userPassword.text.toString().trim()

            if (login.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Не все поля заполнены!", Toast.LENGTH_LONG).show()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Некорректный формат email!", Toast.LENGTH_LONG).show()
            } else {
                val db = DbHelper(this, null)
                if (db.isEmailExists(email)) {
                    Toast.makeText(this, "Email уже используется другим пользователем!", Toast.LENGTH_LONG).show()
                } else {
                    // Устанавливаем роль "admin" для определенного email, иначе "user"
                    val role = if (email.equals("admin@gmail.com", ignoreCase = true)) "admin" else "user"

                    val user = User(
                        login = login,
                        email = email,
                        pass = pass,
                        role = role
                    )
                    db.addUser(user)
                    Toast.makeText(this, "Пользователь добавлен как $role", Toast.LENGTH_LONG).show()

                    userLogin.text.clear()
                    userEmail.text.clear()
                    userPassword.text.clear()

                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}