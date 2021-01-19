package ipvc.estg.esp32_app


import android.app.DatePickerDialog
import android.app.Dialog
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
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val hour = c.get(Calendar.HOUR_OF_DAY)

        //Toast.makeText(this@PieChartActivity, "Data formatada " + day, Toast.LENGTH_LONG).show()

        basicReadWrite(hour, day, month, year)

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

                var esquerda_final = 0
                var direita_final = 0
                val entries: ArrayList<PieEntry> = ArrayList()

                val esquerda =
                    dataSnapshot.child("ESP32_Version1/"+ ano +"/janeiro/dia_"+ dia + "/"+ hora + "h/esquerda").value.toString()
                val direita =
                        dataSnapshot.child("ESP32_Version1/"+ ano +"/janeiro/dia_"+ dia + "/"+ hora + "h/direita").value.toString()

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
                pieChart.setCenterText(hora.toString() + " Horas")
                pieChart.animate()

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

                true
            }

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