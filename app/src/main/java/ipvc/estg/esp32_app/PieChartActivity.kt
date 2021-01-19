package ipvc.estg.esp32_app


import android.graphics.Color
import android.os.Bundle
import android.util.Log
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


class PieChartActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "KotlinActivity"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pie_chart)


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


        basicReadWrite(dataFormatada_hora.toInt(), dataFormatada_dia.toInt(), dataFormatada_ano.toInt())


    }


    fun basicReadWrite(dataFormatada_hora: Int, dataFormatada_dia: Int, dataFormatada_ano: Int){
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
                /*
                val value =
                        dataSnapshot.child("ESP32_Version1/"+ dataFormatada_ano +"/janeiro/dia_"+dataFormatada_dia+"/" + dataFormatada_hora + "h").value.toString()
                */


                var esquerda_final = 0
                var direita_final = 0
                val entries: ArrayList<PieEntry> = ArrayList()

                val esquerda =
                    dataSnapshot.child("ESP32_Version1/"+ dataFormatada_ano +"/janeiro/dia_"+ dataFormatada_dia + "/"+ dataFormatada_hora + "h/esquerda").value.toString()
                val direita =
                        dataSnapshot.child("ESP32_Version1/"+ dataFormatada_ano +"/janeiro/dia_"+ dataFormatada_dia + "/"+ dataFormatada_hora + "h/direita").value.toString()

                if(esquerda != "null") {
                    esquerda_final = esquerda.toInt()
                }

                if (direita != "null") {
                    direita_final = direita.toInt()
                }




                entries.add(PieEntry(esquerda_final.toFloat(), "esquerda"))
                entries.add(PieEntry(direita_final.toFloat(), "direita"))

                val piedataset = PieDataSet(entries, " -> Direções")
                piedataset.setColors(*ColorTemplate.COLORFUL_COLORS)
                piedataset.setValueTextColor(Color.BLACK)
                piedataset.setValueTextSize(16f)

                val pieData = PieData(piedataset) as PieData
                pieChart.setData(pieData)
                pieChart.getDescription().setEnabled(false)
                pieChart.setCenterText(dataFormatada_hora.toString() + " Horas")
                pieChart.animate()

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })

    }
}