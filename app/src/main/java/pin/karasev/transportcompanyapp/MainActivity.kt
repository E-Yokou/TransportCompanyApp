package pin.karasev.transportcompanyapp

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var signuptext: TextView = findViewById(R.id.signuptext)
        signuptext.setOnClickListener {
            var intent = Intent(this, signupActivity::class.java)
            startActivity(intent)
        }
    }
}