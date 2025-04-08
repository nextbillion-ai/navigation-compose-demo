package ai.nextbillion.compose.example.kotlin.contract

import ai.nextbillion.compose.example.kotlin.state.IUiEffect
import ai.nextbillion.compose.example.kotlin.state.IUiEvent
import ai.nextbillion.compose.example.kotlin.state.IUiState

class SettingsContract {

    sealed class Event : IUiEvent

    data class State(
        val simulation: Boolean,
        val flexible: Boolean,
        val language: String,
        val unit: Int,
        val units: List<String>
    ) : IUiState

    sealed class Effect : IUiEffect {

    }
}