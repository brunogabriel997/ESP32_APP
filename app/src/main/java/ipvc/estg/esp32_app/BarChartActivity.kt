package ipvc.estg.esp32_app


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
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
        var data_grafico = 1
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bar_chart)


        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        //var year = c.get(Calendar.YEAR)
        //var month = c.get(Calendar.MONTH)
        //var day = c.get(Calendar.DAY_OF_MONTH)


        val button_bar_data = findViewById<Button>(R.id.button_bar_data)
        button_bar_data.setOnClickListener {
            val datePicker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, day ->
                //Toast.makeText(this@PieChartActivity, "Data formatada " + day + " "+ month + " " + year , Toast.LENGTH_LONG).show()
                basicReadWrite(day, month, year)      // Utiliza a data atual para falar com  a firebase
            },
                    c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH))
            datePicker.show()
        }

        //basicReadWrite(hour, day, month, year)

    }


    fun basicReadWrite(dia: Int,mes: Int, ano: Int){
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



                val entries: ArrayList<BarEntry> = ArrayList()      // Entradas dos valores para a Bar Chart



                if(data_grafico == 1) {
                    var aux = 0
                    var total = 0
                    for (i in 0..23) {

                        // Recebe os valores da Firebase ///////////////////////////////////////////////
                        val esquerda =
                            dataSnapshot.child("ESP32_Version1/" + ano + "/"+meses_ano(mes)+"/dia_" + dia + "/" + i + "h/esquerda").value.toString()
                        val direita =
                            dataSnapshot.child("ESP32_Version1/" + ano + "/"+meses_ano(mes)+"/dia_" + dia + "/" + i + "h/direita").value.toString()
                        ////////////////////////////////////////////////////////////////////////////////

                        // Soma os valores de pessoas que passarm em cada hora (descriminando o sentido)///
                        if (esquerda != "null") {
                            aux = esquerda.toInt()
                        } else {
                            aux = 0
                        }

                        if (direita != "null") {
                            total = direita.toInt() + aux
                        } else {
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
                    barChart.getDescription().setText("Horas / Nº Pessoas");
                    barChart.animateY(0)
                }




                if(data_grafico == 2){
                var aux_esquerda = 0
                var aux_direita = 0
                var total = 0
                for(j in 1..31){
                    for (i in 0..23) {

                        // Recebe os valores da Firebase ///////////////////////////////////////////////
                        val esquerda =
                            dataSnapshot.child("ESP32_Version1/" + ano + "/" + meses_ano(mes)+ "/dia_" + j + "/" + i + "h/esquerda").value.toString()
                        val direita =
                            dataSnapshot.child("ESP32_Version1/" + ano + "/" + meses_ano(mes) + "/dia" + j + "/" + i + "h/direita").value.toString()
                        ////////////////////////////////////////////////////////////////////////////////

                        // Soma os valores de pessoas que passarm em cada hora (descriminando o sentido)///
                        if (esquerda != "null") {
                            aux_esquerda += esquerda.toInt()
                        }

                        if (direita != "null") {
                            aux_direita += direita.toInt()
                        }
                        ///////////////////////////////////////////////////////////////////////////////////

                     }
                    total = aux_direita + aux_esquerda
                    entries.add(BarEntry(j.toFloat(), total.toFloat()))     // Adiciona o valor na Bar Chart
                    aux_direita = 0
                    aux_esquerda = 0

                    }

                    val barDataSet = BarDataSet(entries, " -> Pessoas")
                    barDataSet.setColors(*ColorTemplate.COLORFUL_COLORS)    // Fornece as barras cores aleatorias
                    barDataSet.setValueTextColor(Color.BLACK)               // O texto é escrito em preto
                    barDataSet.setValueTextSize(10f)

                    val barData = BarData(barDataSet)

                    barChart.setFitBars(true)
                    barChart.setData(barData)
                    barChart.getDescription().setText("Dia / Nº Pessoas");
                    barChart.animateY(0)

                }

                if(data_grafico == 3)
                {
                    var aux_esquerda = 0
                    var aux_direita = 0
                    var total = 0
                    for (k in 0..11) {    //Ciclo para o ano
                        for (j in 1..31) {  //Clico para um mês
                            for (i in 0..23) {  //Ciclo para um dia

                                // Recebe os valores da Firebase ///////////////////////////////////////////////
                                val esquerda =
                                    dataSnapshot.child("ESP32_Version1/" + ano + "/"+meses_ano(k)+"/dia_" + j + "/" + i + "h/esquerda").value.toString()
                                val direita =
                                    dataSnapshot.child("ESP32_Version1/" + ano + "/"+meses_ano(k)+"/dia_" + j + "/" + i + "h/direita").value.toString()
                                ////////////////////////////////////////////////////////////////////////////////

                                // Soma os valores de pessoas que passarm em cada hora (descriminando o sentido)///
                                if (esquerda != "null") {
                                    aux_esquerda += esquerda.toInt()
                                }

                                if (direita != "null") {
                                    aux_direita += direita.toInt()
                                }
                                ///////////////////////////////////////////////////////////////////////////////////

                            }
                        }
                        total = aux_direita + aux_esquerda
                        entries.add(BarEntry(k.toFloat(), total.toFloat()))     // Adiciona o valor na Bar Chart
                        aux_direita = 0
                        aux_esquerda = 0
                    }

                        val barDataSet = BarDataSet(entries, " -> Pessoas")
                        barDataSet.setColors(*ColorTemplate.COLORFUL_COLORS)    // Fornece as barras cores aleatorias
                        barDataSet.setValueTextColor(Color.BLACK)               // O texto é escrito em preto
                        barDataSet.setValueTextSize(10f)

                        val barData = BarData(barDataSet)

                        barChart.setFitBars(true)
                        barChart.setData(barData)
                        barChart.getDescription().setText("Meses / Nº Pessoas");
                        barChart.animateY(0)

                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu2, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {


            R.id.dia -> {
                data_grafico = 1
                val intent = Intent(this@BarChartActivity, BarChartActivity::class.java)
                finish()
                startActivity(intent)
                true
            }

            R.id.mes -> {
                data_grafico = 2
                val intent = Intent(this@BarChartActivity, BarChartActivity::class.java)
                finish()
                startActivity(intent)
                true
            }

            R.id.ano -> {
                data_grafico = 3
                val intent = Intent(this@BarChartActivity, BarChartActivity::class.java)
                finish()
                startActivity(intent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }


    }

    fun meses_ano(meses: Int): String
    {
        if (meses == 0) return  "janeiro"
        if (meses == 1) return  "fevereiro"
        if (meses == 2) return  "março"
        if (meses == 3) return  "abril"
        if (meses == 4) return  "maio"
        if (meses == 5) return  "junho"
        if (meses == 6) return  "julho"
        if (meses == 7) return  "agosto"
        if (meses == 8) return  "setembro"
        if (meses == 9) return  "outubro"
        if (meses == 10) return  "novembro"
        if (meses == 11) return  "dezembro"
        else return "null"

    }
}