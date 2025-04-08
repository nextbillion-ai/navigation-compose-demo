package ai.nextbillion.compose.example.kotlin.navigation

import ai.nextbillion.compose.example.kotlin.screens.ComposeMapsScreen
import ai.nextbillion.compose.example.kotlin.screens.ComposeNavigationScreen
import ai.nextbillion.compose.example.kotlin.screens.HomeScreen
import ai.nextbillion.compose.example.kotlin.screens.RouteOptionScreen
import ai.nextbillion.compose.example.kotlin.screens.SettingsScreen
import ai.nextbillion.kits.directions.models.DirectionsRoute
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.json.JSONArray
import java.net.URLDecoder

sealed class DemoScreen(val route: String) {
    data object ComposeMap : DemoScreen("compose_map")
    data object ComposeNavigation : DemoScreen("compose_map/navigation")
    data object ComposeRouteOptions : DemoScreen("compose_map/route_options")

    data object ComposeSettings : DemoScreen("compose_map/settings")
}
@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(navController)
        }

        composable(route = DemoScreen.ComposeMap.route) {
            ComposeMapsScreen(navController)
        }
        composable(
            route = "${DemoScreen.ComposeNavigation.route}?routes={routes}",
            arguments = listOf(
                navArgument("routes") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val directionsRoutes = mutableListOf<DirectionsRoute>()
            val encodedRoutes = backStackEntry.arguments?.getString("routes")

            encodedRoutes?.let {
                val decodedJson = URLDecoder.decode(it, "UTF-8")
                val jsonArray = JSONArray(decodedJson)

                for (i in 0 until jsonArray.length()) {
                    val routeJsonString = jsonArray.getString(i) // ← FIXED: Use getString
                    val route = DirectionsRoute.fromJson(routeJsonString)
                    directionsRoutes.add(route)
                }
            }

            ComposeNavigationScreen(
                navController = navController,
                routes = directionsRoutes,
            )
        }

        composable(
            route = DemoScreen.ComposeRouteOptions.route
        ) {
            RouteOptionScreen(navController = navController)
        }
        composable(
            route = DemoScreen.ComposeSettings.route
        ){
            SettingsScreen(navController = navController)
        }
    }
} 