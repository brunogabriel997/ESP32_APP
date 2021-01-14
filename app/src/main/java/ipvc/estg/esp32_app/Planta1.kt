package ipvc.estg.esp32_app

import android.os.Bundle
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity

class Planta1 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_planta1)
        setSupportActionBar(findViewById(R.id.toolbar))

        Toast.makeText(this@Planta1, "You did it " , Toast.LENGTH_SHORT).show()
    }
}