package ai.nextbillion.compose.example.kotlin.screens

import ai.nextbillion.kits.directions.models.DirectionsRoute
import ai.nextbillion.kits.geojson.Point
import ai.nextbillion.navigation.compose.NBNavigationView
import ai.nextbillion.navigation.core.navigator.NavProgress
import ai.nextbillion.navigation.ui.NavViewConfig
import ai.nextbillion.navigation.ui.listeners.NavigationListener
import ai.nextbillion.navigation.ui.listeners.RouteListener
import android.location.Location
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.google.android.material.bottomsheet.BottomSheetDialog

@Composable
fun ComposeNavigationScreen(
    navController: NavController,
    routes: List<DirectionsRoute>? = null,
) {
    val safeRoutes = routes ?: emptyList()

    val navViewConfig = remember(safeRoutes) {
        NavViewConfig.builder()
            .route(safeRoutes.firstOrNull())
            .routes(safeRoutes)
            .navigationListener(object : NavigationListener {
                // Triggered when user cancels navigation (e.g. presses cancel )
                override fun onCancelNavigation() {
                    navController.navigateUp()
                }

                // Triggered when navigation is finished (reaches destination)
                override fun onNavigationFinished() {
//                    navController.navigateUp()
                }

                // Triggered when navigation starts running
                override fun onNavigationRunning() {
                    // No action needed
                }

            })
            .routeListener(object : RouteListener {
                // Determines whether rerouting is allowed when off-route
                override fun allowRerouteFrom(p0: Location?): Boolean {
                    return true // Allow rerouting from any location
                }

                // Called when user goes off the planned route
                override fun onOffRoute(point: Point?) {
                    // No specific logic here, can add logs if needed
                }

                // Called when rerouting is successfully completed
                override fun onRerouteAlong(route: DirectionsRoute?) {
                    // Optional: update UI or notify user of new route
                }

                // Called when rerouting fails
                override fun onFailedReroute(error: String?) {
                    // Optional: show error or fallback
                }

                // Called when user arrives at a waypoint
                override fun onArrival(p0: NavProgress?, p1: Int) {
                    // No arrival behavior defined
                }

                // Called when system detects user is inside a tunnel
                override fun onUserInTunnel(p0: Boolean) {

                }

                // Determines whether to show default arrive dialog
                override fun shouldShowArriveDialog(p0: NavProgress?, p1: Int): Boolean {
                    return false // Disable built-in arrival dialog
                }

                // Optionally return a custom BottomSheetDialog for arrival
                override fun customArriveDialog(p0: NavProgress?, p1: Int): BottomSheetDialog? {
                    return null // Not using a custom dialog
                }
            })
            .build()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        NBNavigationView(
            modifier = Modifier.fillMaxSize(),
            initViewConfig = { _ -> navViewConfig }

        )
    }
} 