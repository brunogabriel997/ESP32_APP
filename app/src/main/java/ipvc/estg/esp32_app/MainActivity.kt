package ipvc.estg.esp32_app


import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "KotlinActivity"
        var values = String()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        
        //val barChart = findViewById<View>(R.id.barchart) as BarChart

        // data atual
        val formataData_ano = SimpleDateFormat("yyyy")
        val formataData_mes = SimpleDateFormat("MM")
        val formataData_dia = SimpleDateFormat("dd")
        // hora atual
        val formataData_hora = SimpleDateFormat("HH")

        val data = Date()
        val dataFormatada_hora = formataData_hora.format(data)
        val dataFormatada_dia = formataData_dia.format(data)
        val dataFormatada_mes = formataData_mes.format(data)
        val dataFormatada_ano = formataData_ano.format(data)



/*
        val num = 5

        val entries: ArrayList<BarEntry> = ArrayList()
        val labels = ArrayList<String>()
        val bardataset = BarDataSet(entries, "Cells")
        for (i in 6 downTo 0) {
            println(i)

            entries.add(BarEntry(num.toFloat(), i))

            labels.add(i.toString())

        }
        val data1 = BarData(labels, bardataset)
        barChart.data = data1 // set the data and list of labels into chart

        barChart.setDescription("Set Bar Chart Description Here") // set the description

        bardataset.setColors(ColorTemplate.COLORFUL_COLORS)
        barChart.animateY(3000)
*/


        val button_atual = findViewById<Button>(R.id.botao)
        button_atual.setOnClickListener {
            val dataFormatada = formataData_mes.format(data)
            //System.out.println("Data formatada " + dataFormatada );
            //Toast.makeText(this@MainActivity, "Data formatada " + dataFormatada_ano, Toast.LENGTH_LONG).show()



        }
        basicReadWrite(dataFormatada_hora, dataFormatada_dia.toInt(), dataFormatada_ano.toInt())


    }


    fun basicReadWrite(dataFormatada_hora: String, dataFormatada_dia: Int, dataFormatada_ano: Int){
        // [START write_message]
        // Write a message to the database
        val myRef = FirebaseDatabase.getInstance().getReference()
        val barChart = findViewById<View>(R.id.barchart) as BarChart


        // [START read_message]
        // Read from the database
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                /*
                val value =
                        dataSnapshot.child("ESP32_Version1/"+ dataFormatada_ano +"/janeiro/dia_"+dataFormatada_dia+"/" + dataFormatada_hora + "h").value.toString()
                */


                var value2 = 0
                val entries: ArrayList<BarEntry> = ArrayList()
                val labels = ArrayList<String>()
                val bardataset = BarDataSet(entries, "Cells")
                for (i in 23 downTo 0) {
                    val value =
                            dataSnapshot.child("ESP32_Version1/"+ dataFormatada_ano +"/janeiro/dia_7/" + i + "h").value.toString()

                    if(value == "null") {
                        value2 = 0
                    }
                    else {
                        value2 = value.toInt()
                    }
                    entries.add(BarEntry(value2.toFloat(), i))

                    labels.add(i.toString())

                }
                val data1 = BarData(labels, bardataset)
                barChart.data = data1 // set the data and list of labels into chart

                barChart.setDescription("Set Bar Chart Description Here") // set the description

                bardataset.setColors(ColorTemplate.COLORFUL_COLORS)
                barChart.animateY(3000)






                //Log.d(TAG, "Value1 is: $value")
                //Toast.makeText(this@MainActivity, value, Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })

    }
}