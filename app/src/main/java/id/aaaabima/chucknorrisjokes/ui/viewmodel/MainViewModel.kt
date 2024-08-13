package id.aaaabima.chucknorrisjokes.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import id.aaaabima.chucknorrisjokes.domain.interactor.SearchJokes
import id.aaaabima.chucknorrisjokes.ui.model.toModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class MainViewModel @Inject constructor (
    private val searchJokes: SearchJokes
): ViewModel() {

    private val _uiState = MutableStateFlow<MainUiState>(MainUiState.None)
    val uiState = _uiState.asStateFlow()

    fun searchJokes(query: String) {
        _uiState.update { MainUiState.Loading }
        searchJokes.execute(
            param = query,
            onSuccess = { jokes ->
                Log.d("MainViewModel", "human")
                _uiState.update {
                    MainUiState.Success(jokes.map { it.toModel() })
                }
            },
            onError = { throwable ->
                _uiState.update {
                    MainUiState.Error(throwable.message ?: "Unknown Error")
                }
            }
        )
    }

    fun onDispose() {
        searchJokes.dispose()
    }
}