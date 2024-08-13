package id.aaaabima.chucknorrisjokes.data.repository.source

import javax.inject.Inject

class ChuckNorrisJokesEntityDataFactory @Inject constructor(
    private val networkEntityData: ChuckNorrisJokesEntityData,
) {

    fun createEntityData() = networkEntityData
}