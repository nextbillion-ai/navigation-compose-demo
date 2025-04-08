package ai.nextbillion.compose.example.kotlin.screens

import ai.nextbillion.compose.example.kotlin.Constants
import ai.nextbillion.compose.example.kotlin.component.HorizontalButtonsWithTitle
import ai.nextbillion.compose.example.kotlin.component.ItemCell
import ai.nextbillion.compose.example.kotlin.component.SwitchItem
import ai.nextbillion.compose.example.kotlin.navigation.DemoScreen
import ai.nextbillion.compose.example.kotlin.viewModel.SettingViewModel
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.foundation.layout.padding
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Icon
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.IconButton
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.MaterialTheme
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Scaffold
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
internal fun SettingsScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: SettingViewModel = viewModel()
    val currentState by viewModel.uiState.collectAsState()
    viewModel.loadSwitchStateFromSharedPreferences(context = context)
    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Setting") },
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
                            title = "Simulation Enable",
                            check = currentState.simulation,
                            onCheckedChange = { check ->
                                viewModel.updateSharePreference(context, Constants.NavigationSettings.SIMULATION, check)
                            })
                        SwitchItem(
                            title = "Flexible API enable",
                            check = currentState.flexible,
                            onCheckedChange = { check ->
                                viewModel.updateSharePreference(context, Constants.NavigationSettings.FLEXIBLE, check)
                            })
                        ItemCell(title = "Route options", onClick = {
                            navController.navigate(
                                DemoScreen.ComposeRouteOptions.route
                            )
                        } )
                        HorizontalButtonsWithTitle(
                            buttonTexts = currentState.units,
                            title = "Distance Unit",
                            select = currentState.unit ,
                            onSelect = {
                                viewModel.updateUnit(context, it)
                            }
                        )

                    }
                }
            },
        )
    }
}