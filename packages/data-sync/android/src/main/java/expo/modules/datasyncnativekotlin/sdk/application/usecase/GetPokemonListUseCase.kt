package expo.modules.datasyncnativekotlin.sdk.application.usecase

import expo.modules.datasyncnativekotlin.sdk.domain.model.PokemonPage
import expo.modules.datasyncnativekotlin.sdk.domain.repository.PokemonRepository

class GetPokemonListUseCase(
    private val repository: PokemonRepository,
) {
    suspend operator fun invoke(
        limit: Int = 20,
        offset: Int = 0,
    ): Result<PokemonPage> = repository.getPokemonList(limit, offset)
}
