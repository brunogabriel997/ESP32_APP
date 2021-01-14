package ipvc.estg.esp32_app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class menuactivity : AppCompatActivity(), OnItemSelectedListener {
    var floors =
        arrayOf(" ","1 Andar", "2 Andar", "3 Andar")

    var floors2 =
            arrayOf(" ", "1 Andar", "2 Andar")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menuactivity)
        setSupportActionBar(findViewById(R.id.toolbar))

        val spin = findViewById<View>(R.id.spinner1) as Spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, floors)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spin.adapter = adapter
        spin.onItemSelectedListener = this


        val spin2 = findViewById<View>(R.id.spinner2) as Spinner
        val adapter2 = ArrayAdapter(this, android.R.layout.simple_spinner_item, floors2)
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spin2.adapter = adapter2
        spin2.onItemSelectedListener = this

    }

    override fun onItemSelected(arg0: AdapterView<*>?, arg1: View, position: Int, id: Long) {
        Toast.makeText(applicationContext, "Selected User: " + floors[position], Toast.LENGTH_SHORT)
            .show()

            if(floors[position] == "1 Andar"){
                val intent = Intent(this@menuactivity, Planta1::class.java)
                startActivity(intent)
            }

    }


    override fun onNothingSelected(arg0: AdapterView<*>?) {
        // TODO - Custom Code
    }
}
