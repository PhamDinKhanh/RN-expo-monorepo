package expo.modules.datasyncnativekotlin.domain.repository

import expo.modules.datasyncnativekotlin.domain.model.PokemonPage

interface PokemonRepository {
    suspend fun getPokemonList(limit: Int, offset: Int): Result<PokemonPage>
}