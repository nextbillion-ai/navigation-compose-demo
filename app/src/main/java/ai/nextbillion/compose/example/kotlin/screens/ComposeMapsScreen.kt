package ai.nextbillion.compose.example.kotlin.screens

import ai.nextbillion.compose.example.R
import ai.nextbillion.compose.example.kotlin.navigation.DemoScreen
import ai.nextbillion.compose.example.kotlin.repo.LocationTrackingRepository
import ai.nextbillion.compose.example.kotlin.ui.widget.RouteResultScreen
import ai.nextbillion.compose.example.kotlin.utils.CameraAnimateUtils
import ai.nextbillion.compose.example.kotlin.viewModel.FullExperienceViewModel
import ai.nextbillion.maps.camera.CameraPosition
import ai.nextbillion.maps.camera.CameraUpdateFactory
import ai.nextbillion.maps.extension.compose.NextBillionMap
import ai.nextbillion.maps.extension.compose.NextBillionMapEffect
import ai.nextbillion.maps.extension.compose.settings.GesturesSettings
import ai.nextbillion.maps.extension.compose.viewport.rememberCameraPositionState
import ai.nextbillion.maps.geometry.LatLng
import ai.nextbillion.navigation.ui.map.NavNextbillionMap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun ComposeMapsScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: FullExperienceViewModel = viewModel()
    val currentState by viewModel.uiState.collectAsState()
    val cameraPosition = rememberCameraPositionState {
        position =
            CameraPosition.Builder().tilt(0.0).bearing(0.0).zoom(15.0).target(LatLng(0.0, 0.0))
                .build()
    }

    val locationComponentSettings by remember {
        mutableStateOf(
            LocationTrackingRepository.initMapLocationComponentSettings(
                context
            ),
        )
    }

    LaunchedEffect(currentState.locationLatLng) {
        if (null == currentState.locationLatLng) return@LaunchedEffect
        cameraPosition.move(CameraPosition.Builder().target(currentState.locationLatLng!!).build())
    }

    Box(modifier = Modifier.fillMaxSize()) {
        NextBillionMap(
            cameraPositionState = cameraPosition,
            locationComponentSettings = locationComponentSettings,
            gesturesSettings = GesturesSettings {
                setZoomGesturesEnabled(true)
                setScrollGesturesEnabled(true)
            },
            onMapLoaded = viewModel::onMapLoaded,
            onMapLongClickListener = { latLng ->
                viewModel.fetchRoute(context = context, des = latLng)
            }
        ) {
            NextBillionMapEffect { mapView, nbMap ->
                nbMap.getStyle {
                    viewModel.nextbillionMap = nbMap
                    viewModel.navNextbillionMap = NavNextbillionMap(mapView, nbMap)
                    viewModel.configLocationComponent(it)
                }
            }
            currentState.routes.let { routes ->
                if (routes.isEmpty() || viewModel.lastKnowLocation() == null) {
                    return@let
                }

                viewModel.navNextbillionMap?.drawRoutes(routes)
                val padding: IntArray = CameraAnimateUtils.createPadding(context)
                CameraAnimateUtils.frameCameraToBounds(viewModel.navNextbillionMap, routes, padding)
            }

        }

        FloatingActionButton(
            onClick = {
                navController.navigate(DemoScreen.ComposeSettings.route)
            },
            modifier = Modifier
                .padding(16.dp, 64.dp)
                .align(Alignment.TopEnd)
        ) {
            Image(
                painter = painterResource(id = R.mipmap.setting_icon),
                contentDescription = "Setting"
            )
        }

        FloatingActionButton(
            onClick = {
                if (viewModel.lastKnowLocation() != null) {
                    val cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            viewModel.lastKnowLocation()!!.latitude,
                            viewModel.lastKnowLocation()!!.longitude
                        ),
                        15.0
                    )
                    viewModel.nextbillionMap?.animateCamera(cameraUpdate, 1000)
                }
            },
            modifier = Modifier
                .padding(16.dp, 64.dp)
                .align(Alignment.BottomEnd)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_my_location),
                contentDescription = "Tracking Location"
            )
        }

        if (currentState.routes.isNotEmpty()) {
            RouteResultScreen(viewModel, navController)
        }
    }
} 