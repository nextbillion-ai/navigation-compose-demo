package ai.nextbillion.compose.example.kotlin.viewModel

import ai.nextbillion.compose.example.kotlin.Constants
import ai.nextbillion.navigation.core.routefetcher.RequestParamConsts

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.content.Context
import ai.nextbillion.compose.example.kotlin.Constants.KEY_SHARE_PREFERENCE
import ai.nextbillion.compose.example.kotlin.contract.RouteOptionsContract

open class RouteOptionsViewModel :
    BaseViewModel<RouteOptionsContract.Event, RouteOptionsContract.State, RouteOptionsContract.Effect>() {
    override fun createInitialState(): RouteOptionsContract.State {
        return RouteOptionsContract.State(
            tolls = false,
            ferries = false,
            uTurn = false,
            sharpTurn = false,
            countryBorder = false,
            serviceRoad = false,
            highway = false,
            hazmatTypes = mutableListOf(),
        )
    }
    override fun handleEvents(event: RouteOptionsContract.Event) {

    }

    fun loadSwitchStateFromSharedPreferences(context: Context) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val sharedPreferences =
                    context.getSharedPreferences(KEY_SHARE_PREFERENCE, Context.MODE_PRIVATE)
                val highWay = sharedPreferences.getBoolean(Constants.AvoidOptions.HIGHWAY, false)
                val tolls = sharedPreferences.getBoolean(Constants.AvoidOptions.TOLLS, false)
                val countryBorder = sharedPreferences.getBoolean(Constants.AvoidOptions.COUNTRY_BOARDER, false)
                val sharpTurn = sharedPreferences.getBoolean(Constants.AvoidOptions.SHARP_TURN, false)
                val ferries = sharedPreferences.getBoolean(Constants.AvoidOptions.FERRIES, false)
                val uTurn = sharedPreferences.getBoolean(Constants.AvoidOptions.U_TURN, false)
                val service = sharedPreferences.getBoolean(Constants.AvoidOptions.SERVICE_ROAD, false)

                val hazmatType = sharedPreferences.getStringSet(Constants.AvoidOptions.HAZMAT_TYPE, listOf<String>().toSet())

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

                setState {
                    copy(
                        highway = highWay,
                        tolls = tolls,
                        ferries = ferries,
                        uTurn = uTurn,
                        sharpTurn = sharpTurn,
                        countryBorder = countryBorder,
                        serviceRoad = service,
                        hazmatTypes = hazmatTypes,
                    )
                }
            }
        }
    }

    fun updateSharePreference(context: Context, key: String, value: Boolean) {
        if (key == Constants.AvoidOptions.HIGHWAY) {
            setState { copy(highway = value) }
        } else if (key == Constants.AvoidOptions.TOLLS) {
            setState { copy(tolls = value) }
        } else if (key == Constants.AvoidOptions.FERRIES) {
            setState { copy(ferries = value) }
        } else if (key == Constants.AvoidOptions.U_TURN) {
            setState { copy(uTurn = value) }
        } else if (key == Constants.AvoidOptions.SHARP_TURN) {
            setState { copy(sharpTurn = value) }
        } else if (key == Constants.AvoidOptions.COUNTRY_BOARDER) {
            setState { copy(countryBorder = value) }
        } else if (key == Constants.AvoidOptions.SERVICE_ROAD) {
            setState { copy(serviceRoad = value) }
        }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val sharedPreferences =
                    context.getSharedPreferences(KEY_SHARE_PREFERENCE, Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putBoolean(key, value)
                editor.apply()
            }
        }
    }

    fun updateHazmat(context: Context, key: String, value: Boolean) {
        val hazmatTypes = currentState.hazmatTypes
        val newHazmatTypes: List<String> = if (value) {
            hazmatTypes.plus(key)
        } else {
            hazmatTypes.minus(key)
        }
        setState { copy(hazmatTypes = newHazmatTypes) }

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val sharedPreferences =
                    context.getSharedPreferences(KEY_SHARE_PREFERENCE, Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putStringSet(Constants.AvoidOptions.HAZMAT_TYPE, newHazmatTypes.toSet())
                editor.apply()
            }
        }
    }

}
