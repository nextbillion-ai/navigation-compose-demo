package ai.nextbillion.compose.example.kotlin.contract

import ai.nextbillion.compose.example.kotlin.state.IUiEffect
import ai.nextbillion.compose.example.kotlin.state.IUiEvent
import ai.nextbillion.compose.example.kotlin.state.IUiState

class RouteOptionsContract {
    sealed class Event : IUiEvent
    data class State(
        val highway: Boolean,
        val tolls: Boolean,
        val ferries: Boolean,
        val uTurn: Boolean,
        val sharpTurn: Boolean,
        val countryBorder: Boolean,
        val serviceRoad: Boolean,

        val hazmatTypes: List<String>,
    ) : IUiState

    sealed class Effect : IUiEffect {

    }
}