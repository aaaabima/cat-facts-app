package id.aaaabima.chucknorrisjokes.domain.base

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

abstract class BaseUseCase<Param, Result>(
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    abstract fun buildUseCase(param: Param): Flow<Result>

    fun execute(
        param: Param,
        onSuccess: (Result) -> Unit = {},
        onError: (Throwable) -> Unit = {},
        coroutineScope: CoroutineScope
    ) {
        buildUseCase(param)
            .flowOn(defaultDispatcher)
            .onEach { onSuccess.invoke(it) }
            .catch { onError.invoke(it) }
            .launchIn(coroutineScope)
    }
}