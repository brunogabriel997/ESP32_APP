package ipvc.estg.esp32_app

import android.os.Bundle
import android.webkit.WebView

import androidx.appcompat.app.AppCompatActivity

class Chart : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)

        val myWebView: WebView = findViewById(R.id.webview)
        myWebView.loadUrl("https://ricardosantil.000webhostapp.com/index.html")



    }
}git