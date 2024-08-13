package id.aaaabima.chucknorrisjokes.data.repository.source

import id.aaaabima.chucknorrisjokes.data.repository.source.network.NetworkChuckNorrisJokesEntityData
import javax.inject.Inject

class ChuckNorrisJokesEntityDataFactory @Inject constructor(
    private val networkEntityData: NetworkChuckNorrisJokesEntityData,
) {

    fun createEntityData(): ChuckNorrisJokesEntityData = networkEntityData
}