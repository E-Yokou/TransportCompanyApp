package pin.karasev.transportcompanyapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class signupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        var sp = getSharedPreferences("PC", Context.MODE_PRIVATE).edit()

        var email:TextView = findViewById(R.id.email)
        var password:TextView = findViewById(R.id.password)
        var button:ConstraintLayout = findViewById(R.id.button)
        button.setOnClickListener {
            if(email.text.isEmpty() || email.text.contains("@") == false){
                Toast.makeText(this, "Проверьте поле email!", Toast.LENGTH_LONG).show()
            }
            else if(password.text.isEmpty() || password.text.length < 6){
                Toast.makeText(this, "пароль должен быть больше 6 символов", Toast.LENGTH_LONG).show()
            }
            else {
                var db = Firebase.firestore
                val user = hashMapOf(
                    "email" to email.text.toString(),
                    "password" to password.text.toString()
                )

                db.collection("users")
                    .add(user)
                    .addOnSuccessListener { documentReference ->
                        sp.putString("Email", email.text.toString()).commit()
                        startActivity(Intent(this, MainActivity2::class.java))
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Ошибка! Попробуйте позже!", Toast.LENGTH_LONG).show()
                    }
            }
        }
    }
}