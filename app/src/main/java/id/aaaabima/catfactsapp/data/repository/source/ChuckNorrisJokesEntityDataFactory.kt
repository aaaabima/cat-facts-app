package id.aaaabima.catfactsapp.data.repository.source

import id.aaaabima.catfactsapp.data.repository.source.network.NetworkChuckNorrisJokesEntityData
import javax.inject.Inject

class ChuckNorrisJokesEntityDataFactory @Inject constructor(
    private val networkEntityData: NetworkChuckNorrisJokesEntityData,
) {

    fun createEntityData(): ChuckNorrisJokesEntityData = networkEntityData
}