package ai.nextbillion.compose.example.kotlin

import ai.nextbillion.maps.Nextbillion
import android.content.Context
import androidx.startup.Initializer

class DemoApplication : Initializer<Boolean> {
    override fun create(context: Context): Boolean {
        Nextbillion.getInstance(context, "YOUR-API-KEY")
        return true
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}