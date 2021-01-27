package ipvc.estg.esp32_app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class chartSelection: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart_selection)

        val button_bar = findViewById<Button>(R.id.enter_bar_chart)
        button_bar.setOnClickListener {
            val intent = Intent(this@chartSelection, BarChartActivity::class.java)
            startActivity(intent)
        }

        val button_pie = findViewById<Button>(R.id.enter_pie_chart)
        button_pie.setOnClickListener {
            val intent = Intent(this@chartSelection, PieChartActivity::class.java)
            startActivity(intent)
        }
        val button_back = findViewById<Button>(R.id.button_back)
        button_back.setOnClickListener {
            finish()
        }
    }
}