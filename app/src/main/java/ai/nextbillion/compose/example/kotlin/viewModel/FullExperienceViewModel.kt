package ai.nextbillion.compose.example.kotlin.viewModel

import ai.nextbillion.compose.example.R
import ai.nextbillion.compose.example.kotlin.Constants
import ai.nextbillion.compose.example.kotlin.contract.FullExperienceContract
import ai.nextbillion.compose.example.kotlin.model.TrafficModel
import ai.nextbillion.compose.example.kotlin.model.Waypoint
import ai.nextbillion.compose.example.kotlin.utils.ErrorUtils
import ai.nextbillion.kits.directions.models.DirectionsResponse
import ai.nextbillion.kits.directions.models.RouteRequestParams
import ai.nextbillion.kits.geojson.Point
import ai.nextbillion.maps.Nextbillion.getApplicationContext
import ai.nextbillion.maps.core.NextbillionMap
import ai.nextbillion.maps.core.Style
import ai.nextbillion.maps.geometry.LatLng
import ai.nextbillion.maps.location.LocationComponentActivationOptions
import ai.nextbillion.maps.location.LocationComponentOptions
import ai.nextbillion.maps.location.modes.CameraMode
import ai.nextbillion.maps.location.modes.RenderMode
import ai.nextbillion.navigation.core.routefetcher.RequestParamConsts
import ai.nextbillion.navigation.ui.NBNavigation
import ai.nextbillion.navigation.ui.NavLauncherConfig
import ai.nextbillion.navigation.ui.map.NavNextbillionMap
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

enum class HomePagePanelState {
    empty, singleDes, addStops
}

open class FullExperienceViewModel :
    BaseViewModel<FullExperienceContract.Event, FullExperienceContract.State, FullExperienceContract.Effect>() {
    var nextbillionMap: NextbillionMap? = null
    var navNextbillionMap: NavNextbillionMap? = null
    var tabs: MutableList<TrafficModel> = mutableListOf(
        TrafficModel("Car", R.drawable.ic_directions_car, RequestParamConsts.MODE_CAR),
        TrafficModel("Truck", R.drawable.ic_truck_disabled, RequestParamConsts.MODE_TRUCK)
    )
    var currentWaypoints: MutableList<Waypoint> = mutableListOf()

    private var currentTravelMode: String = RequestParamConsts.MODE_CAR

    fun updateTravelMode(context: Context, mode: String) {
        currentTravelMode = mode
        fetchRouteWithWaypoints(context, currentWaypoints)
    }

    fun lastKnowLocation(): Location? {
        return nextbillionMap?.locationComponent?.lastKnownLocation
    }

    override fun createInitialState(): FullExperienceContract.State {
        return FullExperienceContract.State(
            locationLatLng = null,
            routes = mutableListOf(),
            isLoading = false
        )
    }

    override fun handleEvents(event: FullExperienceContract.Event) {
    }

    fun onMapLoaded(style: Style) {
        style.isFullyLoaded
    }

    fun fetchRoute(context: Context, des: LatLng): Boolean {
        fetchRouteWithLegPoint(
            context,
            Waypoint(point = Point.fromLngLat(des.longitude, des.latitude))
        )
        return true
    }

    private fun fetchRouteWithLegPoint(context: Context, des: Waypoint) {
        currentWaypoints.clear()
        val currentLocation = nextbillionMap?.locationComponent?.lastKnownLocation ?: return
        currentWaypoints.add(
            Waypoint(
                point = Point.fromLngLat(
                    currentLocation.longitude,
                    currentLocation.latitude
                )
            )
        )
        currentWaypoints.add(des)
        fetchRouteWithWaypoints(context, currentWaypoints)
    }

    private fun buildRequest(context: Context): RouteRequestParams {

        val sharedPreferences =
            context.getSharedPreferences(Constants.KEY_SHARE_PREFERENCE, Context.MODE_PRIVATE)

        // If the mode is RequestParamConsts.MODE_TRUCK, you must enable the flexible option.
        val flexible = sharedPreferences.getBoolean(Constants.NavigationSettings.FLEXIBLE, true)

        val highWay = sharedPreferences.getBoolean(Constants.AvoidOptions.HIGHWAY, false)
        val tolls = sharedPreferences.getBoolean(Constants.AvoidOptions.TOLLS, false)
        val countryBorder =
            sharedPreferences.getBoolean(Constants.AvoidOptions.COUNTRY_BOARDER, false)
        val sharpTurn = sharedPreferences.getBoolean(Constants.AvoidOptions.SHARP_TURN, false)
        val ferries = sharedPreferences.getBoolean(Constants.AvoidOptions.FERRIES, false)
        val uTurn = sharedPreferences.getBoolean(Constants.AvoidOptions.U_TURN, false)
        val service = sharedPreferences.getBoolean(Constants.AvoidOptions.SERVICE_ROAD, false)

        val hazmatType = sharedPreferences.getString(Constants.AvoidOptions.HAZMAT_TYPE, "")

        val hazmatTypes: MutableList<String> = mutableListOf()
        if (hazmatType?.contains(RequestParamConsts.HAZMAT_GENERAL) == true) {
            hazmatTypes.add(RequestParamConsts.HAZMAT_GENERAL)
        }
        if (hazmatType?.contains(RequestParamConsts.HAZMAT_CIRCUMSTANTIAL) == true) {
            hazmatTypes.add(RequestParamConsts.HAZMAT_CIRCUMSTANTIAL)
        }
        if (hazmatType?.contains(RequestParamConsts.HAZMAT_EXPLOSIVE) == true) {
            hazmatTypes.add(RequestParamConsts.HAZMAT_EXPLOSIVE)
        }
        if (hazmatType?.contains(RequestParamConsts.HAZMAT_HARMFUL_TO_WATER) == true) {
            hazmatTypes.add(RequestParamConsts.HAZMAT_HARMFUL_TO_WATER)
        }

        val requestParams: RouteRequestParams

        val distanceUnit =
            sharedPreferences.getString(
                Constants.NavigationSettings.DISTANCE_UNIT,
                Constants.NavigationSettings.DistanceUnit.METRIC
            )
        val unit = if (distanceUnit == Constants.NavigationSettings.DistanceUnit.METRIC) {
            RequestParamConsts.METRIC
        } else {
            RequestParamConsts.IMPERIAL
        }
        if (flexible) {
            // These avoids parameters only available when option set as RequestParamConsts.FLEXIBLE
            val avoids = listOfNotNull(
                if (highWay) RequestParamConsts.AVOID_HIGHWAY else null,
                if (tolls) RequestParamConsts.AVOID_TOLL else null,
                if (ferries) RequestParamConsts.AVOID_FERRY else null,
                if (uTurn) RequestParamConsts.AVOID_UTURN else null,
                if (sharpTurn) RequestParamConsts.AVOID_SHARP_TURN else null,
                if (service) RequestParamConsts.AVOID_SERVICE_ROAD else null,
            )
            requestParams = RouteRequestParams.builder()
                .alternatives(true)
                .language("en")
                .altCount(3)
                .option(RequestParamConsts.FLEXIBLE)
                .avoid(avoids)
                .crossBorder(countryBorder)
                .unit(unit)
                .hazmatType(hazmatTypes)
                .mode(currentTravelMode).build()

        } else {
            val avoids = listOfNotNull(
                if (highWay) RequestParamConsts.AVOID_HIGHWAY else null,
                if (tolls) RequestParamConsts.AVOID_TOLL else null,
                if (ferries) RequestParamConsts.AVOID_FERRY else null,

                )
            requestParams = RouteRequestParams.builder()
                .alternatives(true)
                .language("en")
                .altCount(3)
                .avoid(avoids)
                .unit(unit)
                .mode(currentTravelMode).build()
        }

        return requestParams
    }

    private fun fetchRouteWithWaypoints(
        context: Context,
        waypoints: MutableList<Waypoint>
    ): Boolean {
        currentWaypoints = waypoints
        val origin = waypoints.first().getPoint()
        val destination = waypoints.last().getPoint()
        val requestParams = buildRequest(context = context)
        NBNavigation.fetchRoute(origin, destination, requestParams, object :
            Callback<DirectionsResponse> {
            override fun onResponse(
                call: Call<DirectionsResponse>,
                response: Response<DirectionsResponse>
            ) {
                val body = response.body()
                if (body != null && body.routes().isNotEmpty()) {
                    setState { copy(isLoading = false, routes = body.routes()) }
                    updatePageState(HomePagePanelState.singleDes)
                } else {
                    setState { copy(isLoading = false) }
                    updatePageState(HomePagePanelState.empty)

                    val errorMessage = ErrorUtils.getErrorMessage(
                        response.errorBody()!!.charStream().readText()
                    )
                    Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DirectionsResponse>, t: Throwable) {
                setState { copy(isLoading = false) }
                updatePageState(HomePagePanelState.empty)
            }

        })
        return true
    }

    fun updatePageState(pageState: HomePagePanelState) {
        setState { copy(pageState = pageState) }
        if (pageState == HomePagePanelState.empty) {
            currentWaypoints.clear()
            navNextbillionMap?.removeRoute()
            navNextbillionMap?.clearMarkers()
            setState { copy(routes = mutableListOf()) }
        }
    }

    fun switchODPair(context: Context) {
        currentWaypoints.reverse()
        fetchRouteWithWaypoints(context, currentWaypoints)
    }

    @SuppressLint("MissingPermission")
    fun configLocationComponent(style: Style) {
        val locationComponent = nextbillionMap?.locationComponent ?: return
        locationComponent.activateLocationComponent(
            LocationComponentActivationOptions
                .builder(getApplicationContext(), style)
                .useDefaultLocationEngine(true)
                .locationComponentOptions(
                    LocationComponentOptions.builder(getApplicationContext())
                        .pulseEnabled(true)
                        .build()
                )
                .build()
        )

        locationComponent.isLocationComponentEnabled = true
        locationComponent.renderMode = RenderMode.NORMAL;
        locationComponent.cameraMode = CameraMode.TRACKING;
    }
}
