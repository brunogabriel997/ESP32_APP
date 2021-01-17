package ipvc.estg.esp32_app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity

class Planta1 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_planta1)
        //setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<Button>(R.id.button).setOnClickListener { view ->
            val intent = Intent(this@Planta1, Chart::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.button2).setOnClickListener { view ->
            val intent = Intent(this@Planta1, menuactivity::class.java)
            startActivity(intent)
        }



    }
}