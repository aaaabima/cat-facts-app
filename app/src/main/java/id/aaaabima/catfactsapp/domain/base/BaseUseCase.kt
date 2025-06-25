package id.aaaabima.catfactsapp.domain.base

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.functions.Action
import io.reactivex.rxjava3.schedulers.Schedulers

abstract class BaseUseCase<Param, Result : Any> {

    private val disposable = CompositeDisposable()

    abstract fun buildUseCase(param: Param): Observable<Result>

    fun execute(
        param: Param,
        onSuccess: (Result) -> Unit = {},
        onError: (Throwable) -> Unit = {},
        onDispose: Action = Action { },
    ) {
        buildUseCase(param)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnDispose(onDispose)
            .subscribe({
                onSuccess(it)
            }, {
                onError(it)
                dispose()
            })
            .let { disposable.add(it) }

    }

    fun dispose() {
        disposable.clear()
    }
}