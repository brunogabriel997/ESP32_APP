package ipvc.estg.esp32_app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity


class menuactivity : AppCompatActivity(), OnItemSelectedListener {
    var floors =
        arrayOf("","1 Andar", "2 Andar", "3 Andar")

    var floors2 =
            arrayOf("","1 Andar", "2 Andar")

    var floors3 =
            arrayOf("", "1 Andar", "2 Andar")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


            setContentView(R.layout.begininterface)
            //setSupportActionBar(findViewById(R.id.toolbar))


            findViewById<Button>(R.id.angry_btn).setOnClickListener { view ->
                setContentView(R.layout.activity_menuactivity)


                val spin = findViewById<View>(R.id.spinner1) as Spinner
                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, floors)
                adapter.setDropDownViewResource(R.layout.spinner_dropdown)
                spin.adapter = adapter
                spin.onItemSelectedListener = this


                val spin2 = findViewById<View>(R.id.spinner2) as Spinner
                val adapter2 = ArrayAdapter(this, android.R.layout.simple_spinner_item, floors2)
                adapter2.setDropDownViewResource(R.layout.spinner_dropdown)
                spin2.adapter = adapter2
                spin2.onItemSelectedListener = this


                val spin3 = findViewById<View>(R.id.spinner3) as Spinner
                val adapter3 = ArrayAdapter(this, android.R.layout.simple_spinner_item, floors3)
                adapter3.setDropDownViewResource(R.layout.spinner_dropdown)
                spin3.adapter = adapter3
                spin3.onItemSelectedListener = this

                findViewById<Button>(R.id.button2).setOnClickListener { view ->
                    val intent = Intent(this@menuactivity, menuactivity::class.java)
                    finish()
                    startActivity(intent)
                }
        }
    }



    override fun onItemSelected(arg0: AdapterView<*>?, arg1: View, position: Int, id: Long) {
        //Toast.makeText(applicationContext, "Selected User: " + floors[position], Toast.LENGTH_SHORT).show()

            if(floors[position] == "1 Andar" ){
                val intent = Intent(this@menuactivity, Planta1::class.java)
                startActivity(intent)
            }

    }

    override fun onRestart() {
        super.onRestart()
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        //Do what you want on the refresh procedure here
        val intent = Intent(this@menuactivity, menuactivity::class.java)
        startActivity(intent)
    }




    override fun onNothingSelected(arg0: AdapterView<*>?) {
        // TODO - Custom Code
    }
}
