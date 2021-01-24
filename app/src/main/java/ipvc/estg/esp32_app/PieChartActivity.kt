package ipvc.estg.esp32_app


import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import android.widget.Button
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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


class PieChartActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "KotlinActivity"
        var data_grafico = 1
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pie_chart)

        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        var year1 = c.get(Calendar.YEAR)
        var month1 = c.get(Calendar.MONTH)
        var day1 = c.get(Calendar.DAY_OF_MONTH)
        var hour1 = c.get(Calendar.HOUR_OF_DAY)

        val button_pie_data = findViewById<Button>(R.id.button_pie_data)
        button_pie_data.setOnClickListener {
            val datePicker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, day ->
                //Toast.makeText(this@PieChartActivity, "Data formatada " + day + " "+ month + " " + year , Toast.LENGTH_LONG).show()
                year1 = year
                month1 = month
                day1 = day
            },
                    c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH))
           datePicker.show()

            val timePicker = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                hour1 = hourOfDay
            },
                    c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE),true)
            timePicker.show()

            Toast.makeText(this@PieChartActivity, "Data formatada " + day1 + " "+ month1 + " " + year1 + " " + hour1 , Toast.LENGTH_LONG).show()
            basicReadWrite(hour1, day1, month1, year1)      // Utiliza a data atual para falar com  a firebase
        }


        //Toast.makeText(this@PieChartActivity, "Data formatada " + day1 + " "+ month1 + " " + year1 + " " + hour1 , Toast.LENGTH_LONG).show()

        basicReadWrite(hour1, day1, month1, year1)      // Utiliza a data atual para falar com  a firebase

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

    fun basicReadWrite(hora: Int, dia: Int, mes: Int, ano: Int){
        // [START write_message]
        // Write a message to the database
        val myRef = FirebaseDatabase.getInstance().getReference()

        val pieChart = findViewById<View>(R.id.piechart) as PieChart


        // [START read_message]
        // Read from the database
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.


                val entries: ArrayList<PieEntry> = ArrayList()

                if(data_grafico == 1) {
                    var esquerda_final = 0
                    var direita_final = 0
                    var esquerda_final2 = 0.toBigDecimal()
                    var direita_final2 = 0.toBigDecimal()
                    val esquerda =
                        dataSnapshot.child("ESP32_Version1/" + ano + "/" + meses_ano(mes)+ "/dia_" + dia + "/" + hora + "h/esquerda").value.toString()
                    val direita =
                        dataSnapshot.child("ESP32_Version1/" + ano + "/" + meses_ano(mes) + "/dia_" + dia + "/" + hora + "h/direita").value.toString()

                    if (esquerda != "null") {
                        esquerda_final = esquerda.toInt()
                    }

                    if (direita != "null") {
                        direita_final = direita.toInt()
                    }

                    //Converter para percentagem

                    var total =  esquerda_final + direita_final

                    //Toast.makeText(applicationContext, total.toString(), Toast.LENGTH_LONG).show()

                    if(total!=0)
                    {
                        esquerda_final2 = ((esquerda_final * 100) / total).toBigDecimal()
                        direita_final2 = ((direita_final * 100) / total).toBigDecimal()
                    }
                    


                    //Toast.makeText(applicationContext, direita_final.toString() + " " + esquerda_final.toString(), Toast.LENGTH_LONG).show()

                    entries.add(PieEntry(esquerda_final.toFloat(), "Esquerda: "+ esquerda_final2 +"%"))
                    entries.add(PieEntry(direita_final.toFloat(), "Direita: "+ direita_final2 +"%"))


                    /////////////////////////////////////////////////////////////////////////
                    val piedataset = PieDataSet(entries, " -> Direções")
                    piedataset.setColors(*ColorTemplate.COLORFUL_COLORS)
                    piedataset.setValueTextColor(Color.BLACK)
                    piedataset.setValueTextSize(16f)

                    val pieData = PieData(piedataset) as PieData
                    pieChart.setData(pieData)
                    pieChart.getDescription().setEnabled(false)
                    pieChart.setCenterText("Hora Atual")
                    pieChart.animate()
                    /////////////////////////////////////////////////////////////////////////

                }


                if(data_grafico == 2) {

                    var aux_esquerda = 0
                    var aux_direita = 0
                    var aux_direita2 = 0
                    var aux_esquerda2 = 0

                    for (i in 0..23) {

                        // Recebe os valores da Firebase ///////////////////////////////////////////////
                        val esquerda =
                            dataSnapshot.child("ESP32_Version1/"+ ano +"/"+ meses_ano(mes)+"/dia_"+ dia + "/"+ i + "h/esquerda").value.toString()
                        val direita =
                            dataSnapshot.child("ESP32_Version1/"+ ano +"/"+ meses_ano(mes)+"/dia_"+ dia + "/"+ i + "h/direita").value.toString()
                        ////////////////////////////////////////////////////////////////////////////////

                        // Soma os valores de pessoas que passarm em cada hora (descriminando o sentido)///
                        if(esquerda != "null") {
                            aux_esquerda += esquerda.toInt()
                        }

                        if (direita != "null") {
                            aux_direita += direita.toInt()
                        }
                        ///////////////////////////////////////////////////////////////////////////////////

                    }
                    var total =  aux_direita + aux_esquerda

                    if(total!=0)    //Caso esse dia não tenha nenhum valor, para a conversão para percentagem nao seja divida por zero!
                    {
                        aux_direita2 = ((aux_direita * 100)/total)
                        aux_esquerda2 = ((aux_esquerda*100)/total)
                    }

                    entries.add(PieEntry(aux_esquerda.toFloat(), "Esquerda: "+ aux_esquerda2 +"%"))
                    entries.add(PieEntry(aux_direita.toFloat(), "Direita: "+ aux_direita2 +"%"))

                    /////////////////////////////////////////////////////////////////////////
                    val piedataset = PieDataSet(entries, " -> Direções")
                    piedataset.setColors(*ColorTemplate.COLORFUL_COLORS)
                    piedataset.setValueTextColor(Color.BLACK)
                    piedataset.setValueTextSize(16f)

                    val pieData = PieData(piedataset) as PieData
                    pieChart.setData(pieData)
                    pieChart.getDescription().setEnabled(false)
                    pieChart.setCenterText(" Dia " + dia.toString())
                    pieChart.animate()
                    /////////////////////////////////////////////////////////////////////////
                }


                if (data_grafico == 3) {

                    var aux_esquerda = 0
                    var aux_direita = 0
                    var aux_direita2 = 0
                    var aux_esquerda2 = 0

                    //Toast.makeText(applicationContext, meses_ano(mes) + "MES", Toast.LENGTH_SHORT).show()
                    for (j in 1..31) {
                        for (i in 0..23) {

                            // Recebe os valores da Firebase ///////////////////////////////////////////////
                            val esquerda =
                                dataSnapshot.child("ESP32_Version1/" + ano + "/" + meses_ano(mes) + "/dia_" + j + "/" + i + "h/esquerda").value.toString()
                            val direita =
                                dataSnapshot.child("ESP32_Version1/" + ano + "/" + meses_ano(mes) + "/dia_" + j + "/" + i + "h/direita").value.toString()
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

                    val total = aux_direita + aux_esquerda

                    if(total!=0)    //Caso esse dia não tenha nenhum valor, para a conversão para percentagem nao seja divida por zero!
                    {
                         aux_direita2 = ((aux_direita * 100) / total)
                         aux_esquerda2 = ((aux_esquerda * 100) / total)
                    }
                        entries.add(PieEntry(aux_esquerda.toFloat(), "Esquerda: "+ aux_esquerda2 +"%"))
                        entries.add(PieEntry(aux_direita.toFloat(), "Direita: "+ aux_direita2 +"%"))

                        /////////////////////////////////////////////////////////////////////////
                        val piedataset = PieDataSet(entries, " -> Direções")
                        piedataset.setColors(*ColorTemplate.COLORFUL_COLORS)
                        piedataset.setValueTextColor(Color.BLACK)
                        piedataset.setValueTextSize(16f)

                        val pieData = PieData(piedataset) as PieData
                        pieChart.setData(pieData)
                        pieChart.getDescription().setEnabled(false)
                        pieChart.setCenterText(meses_ano(mes))
                        pieChart.animate()
                        /////////////////////////////////////////////////////////////////////////

                    }


                if (data_grafico == 4) {

                    var aux_esquerda = 0
                    var aux_direita = 0
                    var aux_direita2 = 0
                    var aux_esquerda2 = 0

                 for( k in 0..11) {
                     for (j in 1..31) {
                         for (i in 0..23) {

                             // Recebe os valores da Firebase ///////////////////////////////////////////////
                             val esquerda =
                                 dataSnapshot.child("ESP32_Version1/" + ano + "/" + meses_ano(k) + "/dia_" + j + "/" + i + "h/esquerda").value.toString()
                             val direita =
                                 dataSnapshot.child("ESP32_Version1/" + ano + "/" + meses_ano(k) + "/dia_" + j + "/" + i + "h/direita").value.toString()
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
                 }

                    val total = aux_direita + aux_esquerda

                    if(total!=0)    //Caso esse dia não tenha nenhum valor, para a conversão para percentagem nao seja divida por zero!
                    {
                         aux_direita2 = ((aux_direita * 100) / total)
                         aux_esquerda2 = ((aux_esquerda * 100) / total)
                    }
                    entries.add(PieEntry(aux_esquerda.toFloat(), "Esquerda: "+ aux_esquerda2 +"%"))
                    entries.add(PieEntry(aux_direita.toFloat(), "Direita: "+ aux_direita2 +"%"))


                    /////////////////////////////////////////////////////////////////////////
                    val piedataset = PieDataSet(entries, " -> Direções")
                    piedataset.setColors(*ColorTemplate.COLORFUL_COLORS)
                    piedataset.setValueTextColor(Color.BLACK)
                    piedataset.setValueTextSize(16f)

                    val pieData = PieData(piedataset) as PieData
                    pieChart.setData(pieData)
                    //pieChart.getDescription().setEnabled(true)
                    pieChart.getDescription().setText("Direção do movimento predominante no ano atual");
                    pieChart.setCenterTextSize(40F)
                    pieChart.setCenterText(ano.toString())
                    pieChart.animate()
                    /////////////////////////////////////////////////////////////////////////

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
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {

            R.id.hora -> {
                data_grafico = 1

                val intent = Intent(this@PieChartActivity, PieChartActivity::class.java)
                finish()
                startActivity(intent)
                true
            }

            R.id.dia -> {
                data_grafico = 2

                val intent = Intent(this@PieChartActivity, PieChartActivity::class.java)
                finish()
                startActivity(intent)
                //Toast.makeText(applicationContext, "Funciona ", Toast.LENGTH_SHORT).show()
                true
            }

            R.id.mes -> {
                data_grafico = 3

                val intent = Intent(this@PieChartActivity, PieChartActivity::class.java)
                finish()
                startActivity(intent)
                true
            }

            R.id.ano -> {
                data_grafico = 4

                val intent = Intent(this@PieChartActivity, PieChartActivity::class.java)
                finish()
                startActivity(intent)
                true
            }


            else -> super.onOptionsItemSelected(item)
        }
    }



}