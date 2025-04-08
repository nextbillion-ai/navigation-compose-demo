package ai.nextbillion.compose.example.kotlin

import ai.nextbillion.kits.directions.models.DirectionsRoute
import ai.nextbillion.kits.geojson.Point
import ai.nextbillion.navigation.compose.NBNavigationView
import ai.nextbillion.navigation.core.navigator.NavProgress
import ai.nextbillion.navigation.ui.NavViewConfig
import ai.nextbillion.navigation.ui.listeners.NavigationListener
import ai.nextbillion.navigation.ui.listeners.RouteListener
import android.location.Location
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.MaterialTheme
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
class NavigationActivity : FragmentActivity(),NavigationListener,RouteListener {

    private lateinit var navViewConfig: NavViewConfig
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val routes = intent.getSerializableExtra("routes") as? List<DirectionsRoute> ?: emptyList()
        val simulation = intent.getBooleanExtra("simulation",false)
        navViewConfig = NavViewConfig.builder()
            .route(routes.first())
            .routes(routes)
            .shouldSimulateRoute(simulation)
            .navigationListener(this)
            .routeListener(this)
            .build()
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.background
            ) {
                NBNavigationView(
                    modifier = Modifier.fillMaxSize(),
                    initViewConfig = { _ ->
                        navViewConfig
                    })
            }
        }
    }

    override fun onCancelNavigation() {
        // The call back of on tap cancel button
        finish()
    }

    override fun onNavigationFinished() {
        // Callback when navigation finishes.
        // If not using simulate Navigation and the navigation finished, finish the activity as well
        if (!navViewConfig.shouldSimulateRoute()) {
            finish()
        }
    }

    override fun onNavigationRunning() {
        // Callback when navigation is running.
        // No action needed here.
    }

    override fun allowRerouteFrom(p0: Location?): Boolean {
        // Check whether need to reroute from the location , by default return true , if don't want to perform reroute , you can return false to interrupt it
       return true
    }

    override fun onOffRoute(p0: Point?) {
        // Callback when the user goes off route.
        // No action needed here.
    }

    override fun onRerouteAlong(p0: DirectionsRoute?) {
        // Callback when rerouting along a new route.
        // No action needed here.
    }

    override fun onFailedReroute(p0: String?) {
        // Callback when rerouting fails.
        // No action needed here.
    }

    override fun onArrival(p0: NavProgress?, p1: Int) {
        // Callback when the user arrives at their destination.
        // No action needed here.
    }

    override fun onUserInTunnel(p0: Boolean) {
        // Callback when the user is detected to be in a tunnel.
        // No action needed here.
    }

    override fun shouldShowArriveDialog(p0: NavProgress?, p1: Int): Boolean {
        // Check whether pop-up arrival dialog. If Dialog is not implemented in the customArriveDialog method, the default BottomSheetDialog is shown. If false is returned, Dialog is not displayed
      return false
    }

    override fun customArriveDialog(p0: NavProgress?, p1: Int): BottomSheetDialog? {
        // Your can custom the arrival dialog if needed.
      return null
    }
}