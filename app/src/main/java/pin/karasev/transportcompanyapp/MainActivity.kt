package pin.karasev.transportcompanyapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var sp = getSharedPreferences("PC", Context.MODE_PRIVATE)
        if (sp.getString("TY", "-9")!="-9") {
            startActivity(Intent(this, MainActivity2::class.java))
        }
        else {
            var signuptext:TextView = findViewById(R.id.signuptext)
            signuptext.setOnClickListener {
                var intent = Intent(this, signupActivity::class.java)
                startActivity(intent)
            }
            var email:TextView = findViewById(R.id.email)
            var password:TextView = findViewById(R.id.password)
            var button: ConstraintLayout = findViewById(R.id.button)


        }

    }
}