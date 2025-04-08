package ai.nextbillion.compose.example.kotlin.screens


import ai.nextbillion.compose.example.kotlin.component.CheckboxItem
import ai.nextbillion.compose.example.kotlin.component.SwitchItem
import ai.nextbillion.compose.example.kotlin.viewModel.RouteOptionsViewModel
import ai.nextbillion.navigation.core.routefetcher.RequestParamConsts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
internal fun RouteOptionScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: RouteOptionsViewModel = viewModel()
    val currentState by viewModel.uiState.collectAsState()
    viewModel.loadSwitchStateFromSharedPreferences(context = context)
    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Route Options") },
                    backgroundColor = Color.White,

                    navigationIcon = {
                        IconButton(onClick = {
                            navController.navigateUp()
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                )
            },
            content = { it ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    Column {
                        SwitchItem(
                            title = "Avoid Highway",
                            check = currentState.highway,
                            onCheckedChange = { check ->
                                viewModel.updateSharePreference(
                                    context,
                                    ai.nextbillion.compose.example.kotlin.Constants.AvoidOptions.HIGHWAY,
                                    check
                                )
                            })
                        SwitchItem(
                            title = "Avoid tolls",
                            check = currentState.tolls,
                            onCheckedChange = { check ->
                                viewModel.updateSharePreference(
                                    context,
                                    ai.nextbillion.compose.example.kotlin.Constants.AvoidOptions.TOLLS,
                                    check
                                )
                            })
                        SwitchItem(
                            title = "Avoid Ferries",
                            check = currentState.ferries,
                            onCheckedChange = { check ->
                                viewModel.updateSharePreference(
                                    context,
                                    ai.nextbillion.compose.example.kotlin.Constants.AvoidOptions.FERRIES,
                                    check
                                )
                            })
                        SwitchItem(
                            title = "Avoid u-turn",
                            check = currentState.uTurn,
                            onCheckedChange = { check ->
                                viewModel.updateSharePreference(
                                    context,
                                    ai.nextbillion.compose.example.kotlin.Constants.AvoidOptions.U_TURN,
                                    check
                                )
                            })
                        SwitchItem(
                            title = "Avoid sharp turn",
                            check = currentState.sharpTurn,
                            onCheckedChange = { check ->
                                viewModel.updateSharePreference(
                                    context,
                                    ai.nextbillion.compose.example.kotlin.Constants.AvoidOptions.SHARP_TURN,
                                    check
                                )
                            })
                        SwitchItem(
                            title = "Avoid country border",
                            check = currentState.countryBorder,
                            onCheckedChange = { check ->
                                viewModel.updateSharePreference(
                                    context,
                                    ai.nextbillion.compose.example.kotlin.Constants.AvoidOptions.COUNTRY_BOARDER,
                                    check
                                )
                            })
                        SwitchItem(
                            title = "Avoid service or living street",
                            check = currentState.serviceRoad,
                            onCheckedChange = { check ->
                                viewModel.updateSharePreference(
                                    context,
                                    ai.nextbillion.compose.example.kotlin.Constants.AvoidOptions.SERVICE_ROAD,
                                    check
                                )
                            })
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp), text = "Hazmat type"
                        )
                        CheckboxItem(title = "General", check = currentState.hazmatTypes.contains(
                            RequestParamConsts.HAZMAT_GENERAL
                        ), onCheckedChange = { check ->
                            viewModel.updateHazmat(
                                context,
                                RequestParamConsts.HAZMAT_GENERAL,
                                check
                            )
                        })
                        CheckboxItem(title = "Circumstantial",
                            check = currentState.hazmatTypes.contains(
                                RequestParamConsts.HAZMAT_CIRCUMSTANTIAL
                            ),
                            onCheckedChange = { check ->
                                viewModel.updateHazmat(
                                    context,
                                    RequestParamConsts.HAZMAT_CIRCUMSTANTIAL,
                                    check
                                )
                            })
                        CheckboxItem(title = "Explosive", check = currentState.hazmatTypes.contains(
                            RequestParamConsts.HAZMAT_EXPLOSIVE
                        ), onCheckedChange = { check ->
                            viewModel.updateHazmat(
                                context,
                                RequestParamConsts.HAZMAT_EXPLOSIVE,
                                check
                            )
                        })
                        CheckboxItem(title = "Harmful to water",
                            check = currentState.hazmatTypes.contains(
                                RequestParamConsts.HAZMAT_HARMFUL_TO_WATER
                            ),
                            onCheckedChange = { check ->
                                viewModel.updateHazmat(
                                    context,
                                    RequestParamConsts.HAZMAT_HARMFUL_TO_WATER,
                                    check
                                )
                            })

                    }
                }
            },
        )
    }

}
