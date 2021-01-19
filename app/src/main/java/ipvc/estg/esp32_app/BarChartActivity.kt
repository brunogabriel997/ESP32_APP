package ipvc.estg.esp32_app


import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*


class BarChartActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "KotlinActivity"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bar_chart)


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


                var aux = 0
                var total = 0
                val entries: ArrayList<BarEntry> = ArrayList()      // Entradas dos valores para a Bar Chart
                for (i in 0..23) {

                    // Recebe os valores da Firebase ///////////////////////////////////////////////
                    val esquerda =
                            dataSnapshot.child("ESP32_Version1/"+ dataFormatada_ano +"/janeiro/dia_"+ dataFormatada_dia + "/"+ i + "h/esquerda").value.toString()
                    val direita =
                            dataSnapshot.child("ESP32_Version1/"+ dataFormatada_ano +"/janeiro/dia_"+ dataFormatada_dia + "/"+ i + "h/direita").value.toString()
                    ////////////////////////////////////////////////////////////////////////////////

                    // Soma os valores de pessoas que passarm em cada hora (descriminando o sentido)///
                    if(esquerda != "null") {
                        aux = esquerda.toInt()
                    }
                    else {
                        aux = 0
                    }

                    if (direita != "null") {
                        total = direita.toInt() + aux
                    }
                    else {
                        total = 0 + aux

                    }
                    ///////////////////////////////////////////////////////////////////////////////////

                    entries.add(BarEntry(i.toFloat(), total.toFloat()))     // Adiciona o valor na Bar Chart


                }

                val barDataSet = BarDataSet(entries, " -> Pessoas")
                barDataSet.setColors(*ColorTemplate.COLORFUL_COLORS)    // Fornece as barras cores aleatorias
                barDataSet.setValueTextColor(Color.BLACK)               // O texto é escrito em preto
                barDataSet.setValueTextSize(10f)

                val barData = BarData(barDataSet)

                barChart.setFitBars(true)
                barChart.setData(barData)
                barChart.getDescription().setText("Número de Pessoas");
                barChart.animateY(0)

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {


            R.id.dia -> {


                true
            }

            R.id.mes -> {


                true
            }

            R.id.ano -> {


                true
            }

            else -> super.onOptionsItemSelected(item)
        }


    }

}