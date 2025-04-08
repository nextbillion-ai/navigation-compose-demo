package ai.nextbillion.compose.example.kotlin.viewModel

import ai.nextbillion.compose.example.kotlin.contract.SettingsContract
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.content.Context
open class SettingViewModel :
    BaseViewModel<SettingsContract.Event, SettingsContract.State, SettingsContract.Effect>() {
    override fun createInitialState(): SettingsContract.State {
        return SettingsContract.State(
            simulation = false,
            flexible = true,
            language = "",
            unit = 1,
            units = listOf(
                ai.nextbillion.compose.example.kotlin.Constants.NavigationSettings.DistanceUnit.METRIC,
                ai.nextbillion.compose.example.kotlin.Constants.NavigationSettings.DistanceUnit.IMPERIAL
            )
        )
    }

    override fun handleEvents(event: SettingsContract.Event) {

    }

    fun loadSwitchStateFromSharedPreferences(context: Context) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val sharedPreferences =
                    context.getSharedPreferences(
                        ai.nextbillion.compose.example.kotlin.Constants.KEY_SHARE_PREFERENCE,
                        Context.MODE_PRIVATE
                    )
                val simulation =
                    sharedPreferences.getBoolean(ai.nextbillion.compose.example.kotlin.Constants.NavigationSettings.SIMULATION, false)
                val flexible =
                    sharedPreferences.getBoolean(ai.nextbillion.compose.example.kotlin.Constants.NavigationSettings.FLEXIBLE, false)
                val distanceUnit =
                    sharedPreferences.getString(
                        ai.nextbillion.compose.example.kotlin.Constants.NavigationSettings.DISTANCE_UNIT,
                        ai.nextbillion.compose.example.kotlin.Constants.NavigationSettings.DistanceUnit.METRIC
                    )
                val language =
                    sharedPreferences.getString(ai.nextbillion.compose.example.kotlin.Constants.NavigationSettings.LANGUAGE, "EN")
                val unit = currentState.units.indexOf(distanceUnit)

                setState {
                    copy(
                        simulation = simulation,
                        flexible = flexible,
                        unit = unit,
                        language = language!!
                    )
                }
            }
        }
    }
    fun updateSharePreference(context: Context, key: String, value: Boolean) {
        if (key == ai.nextbillion.compose.example.kotlin.Constants.NavigationSettings.SIMULATION) {
            setState { copy(simulation = value) }
        } else if (key == ai.nextbillion.compose.example.kotlin.Constants.NavigationSettings.FLEXIBLE) {
            setState { copy(flexible = value) }
        }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val sharedPreferences =
                    context.getSharedPreferences(
                        ai.nextbillion.compose.example.kotlin.Constants.KEY_SHARE_PREFERENCE,
                        Context.MODE_PRIVATE
                    )
                val editor = sharedPreferences.edit()
                editor.putBoolean(key, value)
                editor.apply()
            }
        }
    }
    fun updateUnit(context: Context, index: Int) {
        val unitName = currentState.units[index]
        setState { copy(unit = index) }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val sharedPreferences =
                    context.getSharedPreferences(
                        ai.nextbillion.compose.example.kotlin.Constants.KEY_SHARE_PREFERENCE,
                        Context.MODE_PRIVATE
                    )
                val editor = sharedPreferences.edit()
                editor.putString(ai.nextbillion.compose.example.kotlin.Constants.NavigationSettings.DISTANCE_UNIT, unitName)
                editor.apply()
            }
        }
    }
}
