package ipvc.estg.esp32_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast


class menu_incial : AppCompatActivity() {


    private var itemList = arrayOf("Item 1", "Item 2", "Item 3" )

    private var listView:ListView ? = null
    private var arrayAdapter:ArrayAdapter<String> ? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_incial)

        listView = findViewById(R.id.listview)
        arrayAdapter = ArrayAdapter(applicationContext, android.R.layout.simple_list_item_1, itemList)
        listView?.adapter = arrayAdapter
        registerForContextMenu(listView)

    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        menuInflater.inflate(R.menu.menu, menu)
        super.onCreateContextMenu(menu, v, menuInfo)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {

        return when(item.itemId) {
            R.id.menu_call -> {
                Toast.makeText(applicationContext, "Entrar em biblioteca", Toast.LENGTH_SHORT)
                    .show()
                return true
            }

            R.id.menu_message -> {
                Toast.makeText(applicationContext, "Entrar em escola", Toast.LENGTH_SHORT)
                    .show()
                return true
            }
            R.id.menu_phone -> {
                Toast.makeText(applicationContext, "Entrar em design", Toast.LENGTH_SHORT)
                    .show()
                return true
            }

            else -> return super.onContextItemSelected(item)
        }
    }

}