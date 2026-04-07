package expo.modules.datasyncnativekotlin.sdk.api

import expo.modules.datasyncnativekotlin.presentation.model.PokemonPageJSDto

interface DataSyncSdk {
    suspend fun fetchPokemons(limit: Int): PokemonPageJSDto
}