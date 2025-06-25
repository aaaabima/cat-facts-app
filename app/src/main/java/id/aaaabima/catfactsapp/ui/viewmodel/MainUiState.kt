package id.aaaabima.catfactsapp.ui.viewmodel

import id.aaaabima.catfactsapp.ui.model.JokeModel

sealed class MainUiState {

    data object None : MainUiState()

    data object Loading : MainUiState()

    class Success(val jokes: JokeModel) : MainUiState()

    class Error(val message: String) : MainUiState()
}