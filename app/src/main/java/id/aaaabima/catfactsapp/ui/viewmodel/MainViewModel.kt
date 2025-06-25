package id.aaaabima.catfactsapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import id.aaaabima.catfactsapp.domain.base.NoParam
import id.aaaabima.catfactsapp.domain.interactor.SearchJokes
import id.aaaabima.catfactsapp.ui.model.toModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class MainViewModel @Inject constructor (
    private val searchJokes: SearchJokes
): ViewModel() {

    private val _uiState = MutableStateFlow<MainUiState>(MainUiState.None)
    val uiState = _uiState.asStateFlow()

    fun searchJokes() {
        _uiState.update { MainUiState.Loading }
        searchJokes.execute(
            param = NoParam,
            onSuccess = { jokes ->
                _uiState.update {
                    MainUiState.Success(jokes.toModel())
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