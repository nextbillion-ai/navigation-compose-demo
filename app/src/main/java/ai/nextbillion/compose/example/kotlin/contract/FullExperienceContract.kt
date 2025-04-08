package ai.nextbillion.compose.example.kotlin.contract

import ai.nextbillion.compose.example.kotlin.state.IUiEffect
import ai.nextbillion.compose.example.kotlin.state.IUiEvent
import ai.nextbillion.compose.example.kotlin.state.IUiState
import ai.nextbillion.compose.example.kotlin.viewModel.HomePagePanelState
import ai.nextbillion.kits.directions.models.DirectionsRoute
import ai.nextbillion.maps.geometry.LatLng

class FullExperienceContract {

    sealed class Event : IUiEvent {
    }

    data class State(
        val locationLatLng: LatLng?,
        val isLoading: Boolean,
        val routes: MutableList<DirectionsRoute> = mutableListOf(),
        val pageState: HomePagePanelState = HomePagePanelState.empty
    ) : IUiState

    sealed class Effect : IUiEffect {
        internal data class Toast(val msg: String?) : Effect()
    }
}