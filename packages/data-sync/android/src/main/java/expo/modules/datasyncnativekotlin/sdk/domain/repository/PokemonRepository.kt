package expo.modules.datasyncnativekotlin.sdk.domain.repository

import expo.modules.datasyncnativekotlin.sdk.data.local.entities.PokemonEntity
import expo.modules.datasyncnativekotlin.sdk.domain.model.PokemonPage

interface PokemonRepository {
    suspend fun getPokemonList(
        limit: Int,
        offset: Int,
    ): Result<PokemonPage>

    suspend fun savePokemonWithEvent(
        pokemon: PokemonEntity,
        isFromSync: Boolean = false,
    )
}
