package expo.modules.datasyncnativekotlin.domain.repository

import expo.modules.datasyncnativekotlin.domain.model.Pokemon

interface PokemonRepository {
    suspend fun getPokemonList(limit: Int, offset: Int): Result<List<Pokemon>>
}