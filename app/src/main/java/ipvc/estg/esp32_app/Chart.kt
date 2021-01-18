package ipvc.estg.esp32_app


import android.os.Bundle
import android.view.View
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity


class Chart : AppCompatActivity()
{
    private var myWebView: WebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)

        myWebView = findViewById<View>(R.id.wb_webView) as WebView
        myWebView!!.loadUrl("https://ricardosantil.000webhostapp.com/index.html")

        val webSettings = myWebView!!.settings
        //Habilitando o JavaScript
        webSettings.javaScriptEnabled = true

    }
}