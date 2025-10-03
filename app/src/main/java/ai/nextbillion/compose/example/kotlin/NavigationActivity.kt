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

/**
 * Navigation Activity that handles turn-by-turn navigation using NextBillion Navigation SDK.
 * This activity implements both NavigationListener and RouteListener to handle navigation
 * events and route-related callbacks during the navigation session.
 * 
 * Features:
 * - Displays navigation UI using Jetpack Compose
 * - Handles route simulation for testing purposes
 * - Manages navigation lifecycle events
 * - Provides callbacks for rerouting and arrival scenarios
 */
class NavigationActivity : FragmentActivity(),NavigationListener,RouteListener {

    /**
     * Navigation view configuration that contains all the settings and listeners
     * for the navigation session including routes, simulation mode, and event listeners
     */
    private lateinit var navViewConfig: NavViewConfig
    /**
     * Called when the activity is first created. Sets up the navigation view configuration
     * and initializes the Compose UI with the navigation view.
     * 
     * @param savedInstanceState Bundle containing the activity's previously frozen state, if any
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Extract the list of routes passed from the previous activity
        val routes = intent.getSerializableExtra("routes") as? List<DirectionsRoute> ?: emptyList()
        // Extract the simulation flag to determine if route should be simulated for testing
        val simulation = intent.getBooleanExtra("simulation",false)
        // Configure the navigation view with route data and listeners
        navViewConfig = NavViewConfig.builder()
            .route(routes.first()) // Set the primary route for navigation
            .routes(routes) // Set all available route alternatives
            .shouldSimulateRoute(simulation) // Enable/disable route simulation
            .navigationListener(this) // Set this activity as navigation event listener
            .dissolvedRouteEnabled(true) // Enable dissolved route visualization
            .routeListener(this) // Set this activity as route event listener
            .build()
        // Set up the Compose UI content
        setContent {
            // Create a surface that fills the entire screen
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.background
            ) {
                // Initialize the NextBillion Navigation View with full screen size
                NBNavigationView(
                    modifier = Modifier.fillMaxSize(),
                    // Provide the navigation configuration when the view is initialized
                    initViewConfig = { _ ->
                        navViewConfig
                    })
            }
        }
    }

    /**
     * Called when the user taps the cancel navigation button.
     * This callback allows handling of navigation cancellation events.
     */
    override fun onCancelNavigation() {
        // Finish the activity when navigation is cancelled by user
        finish()
    }

    /**
     * Called when the navigation session has finished successfully.
     * This typically happens when the user reaches their final destination.
     */
    override fun onNavigationFinished() {
        // Only finish the activity if not in simulation mode
        // In simulation mode, keep the activity open for testing purposes
        if (!navViewConfig.shouldSimulateRoute()) {
            finish()
        }
    }

    /**
     * Called when the navigation session is actively running.
     * This callback can be used to perform actions while navigation is in progress.
     */
    override fun onNavigationRunning() {
        // No specific action required when navigation is running
        // This can be extended to handle ongoing navigation state updates
    }

    /**
     * Determines whether rerouting should be allowed from the specified location.
     * This method is called when the system detects a need for rerouting.
     * 
     * @param p0 The current location from which rerouting is being considered
     * @return true to allow rerouting, false to prevent rerouting from this location
     */
    override fun allowRerouteFrom(p0: Location?): Boolean {
        // Allow rerouting by default for optimal navigation experience
        // Return false here if you want to prevent rerouting under specific conditions
        return true
    }

    /**
     * Called when the user has deviated from the planned route.
     * This callback is triggered before automatic rerouting begins.
     * 
     * @param p0 The geographic point where the user went off route
     */
    override fun onOffRoute(p0: Point?) {
        // Handle off-route scenarios here if needed
        // This could include showing notifications or logging events
    }

    /**
     * Called when a new route has been calculated and navigation is being rerouted.
     * This happens after the user goes off route and a new path is determined.
     * 
     * @param p0 The new DirectionsRoute that will be used for continued navigation
     */
    override fun onRerouteAlong(p0: DirectionsRoute?) {
        // Handle successful rerouting scenarios here if needed
        // This could include updating UI elements or logging the new route
    }

    /**
     * Called when rerouting fails and a new route cannot be calculated.
     * This could happen due to network issues or lack of available routes.
     * 
     * @param p0 Error message describing why the rerouting failed
     */
    override fun onFailedReroute(p0: String?) {
        // Handle rerouting failure scenarios here if needed
        // This could include showing error messages or attempting manual recovery
    }

    /**
     * Called when the user arrives at a destination (waypoint or final destination).
     * This callback is triggered when the user reaches within the arrival threshold.
     * 
     * @param p0 Current navigation progress information including location and route data
     * @param p1 The index of the destination/waypoint that was reached
     */
    override fun onArrival(p0: NavProgress?, p1: Int) {
        // Handle arrival scenarios here if needed
        // This could include showing arrival confirmations or logging completion
    }

    /**
     * Called when the system detects that the user has entered or exited a tunnel.
     * This can be used to adjust navigation behavior in tunnel scenarios.
     * 
     * @param p0 true if the user has entered a tunnel, false if they have exited
     */
    override fun onUserInTunnel(p0: Boolean) {
        // Handle tunnel detection scenarios here if needed
        // This could include adjusting GPS accuracy expectations or UI updates
    }

    /**
     * Determines whether an arrival dialog should be displayed when reaching a destination.
     * If this returns true and customArriveDialog returns null, a default dialog will be shown.
     * 
     * @param p0 Current navigation progress information
     * @param p1 The index of the destination that was reached
     * @return true to show arrival dialog, false to skip the dialog
     */
    override fun shouldShowArriveDialog(p0: NavProgress?, p1: Int): Boolean {
        // Disable arrival dialog display
        // Set to true if you want to show arrival confirmations to users
        return false
    }

    /**
     * Provides a custom arrival dialog to be displayed when reaching a destination.
     * This method is only called if shouldShowArriveDialog returns true.
     * 
     * @param p0 Current navigation progress information
     * @param p1 The index of the destination that was reached
     * @return Custom BottomSheetDialog to display, or null to use the default dialog
     */
    override fun customArriveDialog(p0: NavProgress?, p1: Int): BottomSheetDialog? {
        // Return null to use the default arrival dialog
        // Implement custom dialog creation here if needed
        return null
    }
}