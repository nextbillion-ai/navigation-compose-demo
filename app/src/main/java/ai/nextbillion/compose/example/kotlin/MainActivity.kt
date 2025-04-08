package ai.nextbillion.compose.example.kotlin

import ai.nextbillion.compose.example.kotlin.navigation.NavGraph
import ai.nextbillion.compose.example.kotlin.ui.theme.NBNavigationKotlinDemoTheme
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.rememberNavController
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
class MainActivity : FragmentActivity() {
    private val requestCode = 200

    private var permissions =
        mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NBNavigationKotlinDemoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavGraph(navController = navController)
                }
            }
        }

        if (!hasPermissions()) {
            requestPermissions()
        }
    }

    private fun hasPermissions(): Boolean {
        return permissions.all { permission ->
            ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
        }
    }

    // Request permissions if not granted
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this, permissions.toTypedArray(), requestCode)
    }

    // Handle the result of the permission request
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // Check if all permissions are granted
        if (requestCode == this.requestCode) {
            val allPermissionsGranted = grantResults.all { it == PackageManager.PERMISSION_GRANTED }
            if (allPermissionsGranted) {
                // Permissions granted
                // You can perform any actions requiring these permissions here
            } else {
                Toast.makeText(baseContext,"Please grant the permissions", Toast.LENGTH_SHORT).show()
            }
        }
    }
} 