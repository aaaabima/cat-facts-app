package id.aaaabima.chucknorrisjokes.ui.viewmodel

import id.aaaabima.chucknorrisjokes.ui.model.JokeModel

sealed class MainUiState {

    object None : MainUiState()

    object Loading : MainUiState()

    class Success(val jokes: List<JokeModel>) : MainUiState()

    class Error(val message: String) : MainUiState()
}